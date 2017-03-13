package com.elis.ltm.appunti.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elis.ltm.appunti.db.Databasehandler;
import com.elis.ltm.appunti.adapter.NotaAdapter;
import com.elis.ltm.appunti.R;
import com.elis.ltm.appunti.model.Nota;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * Created by davide on 13/03/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rv;
    FloatingActionButton fab;
    NotaAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Databasehandler db;

    private static final String LAYOUT_MANAGER_KEY = "LAYOUT_MANAGER_KEY";
    private int STAGGERED_LAYOUT = 20;
    private int LINEAR_LAYOUT = 21;
    private int layoutManagerType = LINEAR_LAYOUT;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new NotaAdapter();
        layoutManager = getSavedLayoutManager();
        db = new Databasehandler(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        adapter.setDataset(db.getAllNotes());


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        registerForContextMenu(rv);

    }
    @Override
    public void onClick(View v) {
        showDialogAdd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.layout_action:
                if (getLayoutManagerType() == STAGGERED_LAYOUT) {
                    setLayoutManagerType(LINEAR_LAYOUT);
                    rv.setLayoutManager(new LinearLayoutManager(this));
                    item.setIcon(R.drawable.layout);


                } else {
                    setLayoutManagerType(STAGGERED_LAYOUT);
                    rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.linearlayout);

                }
                break;
            case R.id.src_action:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                LayoutInflater inflate = this.getLayoutInflater();
                View da = inflate.inflate(R.layout.alert_dialog_src, null);
                dialog.setView(da);
                final EditText src;
                src = (EditText) da.findViewById(R.id.title_et);
                dialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Nota> arraysrc = adapter.srcNota(src.getText().toString());
                        adapter.setDataset(arraysrc);
                        fab.setVisibility(View.INVISIBLE);
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posizione = adapter.getPosition();
        switch (item.getItemId()) {
            case R.id.delete_item:
                Nota nota_rmv = adapter.getNota(posizione);
                long check = db.deleteNote(nota_rmv);
                if(check!=-1){ adapter.removeNota(posizione);}
                break;
            case R.id.edit_item:
                Nota nota = adapter.getNota(posizione);
                showDialogEdit(nota.getTitolo(), nota.getTesto());
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void showDialogAdd(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        View da = inflate.inflate(R.layout.alert_dialog_layout, null);
        dialog.setView(da);
        String color;
        final EditText title, text;
        final Button colore_blu, colore_giallo, colore_rosso;
//        colore_blu = (Button) da.findViewById(R.id.colore_blu);
//        colore_giallo = (Button) da.findViewById(R.id.colore_giallo);
//        colore_rosso = (Button) da.findViewById(R.id.colore_rosso);
//        colore_blu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        title = (EditText) da.findViewById(R.id.title_et);
        text = (EditText) da.findViewById(R.id.text_et);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (title.getText().toString().isEmpty() ||
                        text.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
                }else{
                    Nota nota = new Nota();
                    nota.setTitolo(title.getText().toString());
                    nota.setTesto(text.getText().toString());
//                nota.setColore(coloreSfondo);
                    long check = db.addNote(nota);
                    if(check!=-1){adapter.addNota(nota);}
                }
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }
        public void showDialogEdit(String titolo, String testo){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        View da = inflate.inflate(R.layout.alert_dialog_layout, null);
        dialog.setView(da);
        final TextView x;
        final EditText title, text;
        x = (TextView) da.findViewById(R.id.titolo_inflater);
        x.setText("Edit Nota");
        title = (EditText) da.findViewById(R.id.title_et);
        title.setText(titolo);
        text = (EditText) da.findViewById(R.id.text_et);
        text.setText(testo);
        dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (title.getText().toString().isEmpty() ||
                        text.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
                }else {
                    Nota nota = adapter.editNota(adapter.getPosition(),
                            title.getText().toString(),
                            text.getText().toString());
                    long check = db.updateNote(nota);
                }
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    private RecyclerView.LayoutManager getSavedLayoutManager() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int layoutManager = sharedPrefs.getInt(LAYOUT_MANAGER_KEY, -1);
        if (layoutManager == STAGGERED_LAYOUT) {
            setLayoutManagerType(layoutManager);
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }
        if (layoutManager == LINEAR_LAYOUT) {
            setLayoutManagerType(layoutManager);
            return new LinearLayoutManager(this);
        }
        return new LinearLayoutManager(this);

    }


    public int getLayoutManagerType() {

        return layoutManagerType;
    }

    public void setLayoutManagerType(int layoutManagerType) {
        this.layoutManagerType = layoutManagerType;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        adapter.setDataset(db.getAllNotes());
        fab.setVisibility(View.VISIBLE);
    }
}
