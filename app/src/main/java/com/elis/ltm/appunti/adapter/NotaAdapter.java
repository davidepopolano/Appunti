package com.elis.ltm.appunti.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elis.ltm.appunti.R;
import com.elis.ltm.appunti.activity.MainActivity;
import com.elis.ltm.appunti.model.Nota;

import java.util.ArrayList;

/**
 * Created by davide on 13/03/17.
 */

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaVH> {

    ArrayList<Nota> dataset = new ArrayList<>();
    private int position;

    @Override
    public NotaVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaVH(v);
    }

    @Override
    public void onBindViewHolder(NotaVH holder, int position) {
        holder.titolo.setText(dataset.get(position).getTitolo());
        holder.testo.setText(dataset.get(position).getTesto());
        holder.dataCreazione.setText(dataset.get(position).getDataCreazione());

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setDataset(ArrayList<Nota> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    public void addNota(Nota nota) {
        dataset.add(nota);
        notifyDataSetChanged();
    }

    public void removeNota(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public Nota editNota(int position, String titolo, String testo) {
        Nota nota = getNota(position);
        nota.setTitolo(titolo);
        nota.setTesto(testo);
//        nota.setColore(colore);
        notifyItemChanged(position);
        return nota;
    }

    public ArrayList<Nota> srcNota(String src) {
        ArrayList<Nota> search = new ArrayList<>();
        int i = dataset.size();
        for (int a = 0; a < i; a++) {
            if (dataset.get(a).getTitolo().contains(src)) {
                search.add(dataset.get(a));
            }
        }
        return search;
    }

    public Nota getNota(int position) {
        return dataset.get(position);
    }

    public int getPosition() {
        return position;
    }

    public class NotaVH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView titolo, testo, dataCreazione;

        public NotaVH(View itemView) {
            super(itemView);
            titolo = (TextView) itemView.findViewById(R.id.titolo_tv);
            testo = (TextView) itemView.findViewById(R.id.testo_tv);
            dataCreazione = (TextView) itemView.findViewById(R.id.dataCreazione_tv);
            itemView.setOnCreateContextMenuListener(this);
//            if (dataset.get(getAdapterPosition()).getColore() ==) {
//                itemView.setBackgroundResource(R.color.material_blue_100);
//            } else if (dataset.get(getAdapterPosition()).getColore() ==) {
//                itemView.setBackgroundResource(R.color.material_red_100);
//            } else if (dataset.get(getAdapterPosition()).getColore() ==) {
//                itemView.setBackgroundResource(R.color.material_yellow_100);
//            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MainActivity activity = (MainActivity) v.getContext();
            MenuInflater menuInflater = activity.getMenuInflater();
            menuInflater.inflate(R.menu.context_menu, menu);
            position = getAdapterPosition();
        }
    }
}
