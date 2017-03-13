package com.elis.ltm.appunti.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.elis.ltm.appunti.model.Nota;

import java.util.ArrayList;

/**
 * Created by davide on 13/03/17.
 */

public class Databasehandler extends SQLiteOpenHelper {

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_COLOR = "color";
    private static final String KEY_DATE = "creationDate";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "notes";

    private static final String TABLE_NOTES = "note";


    public Databasehandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_COLOR + " TEXT"
                + ")";
        db.execSQL(CREATE_NOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        onCreate(db);
    }

    public ArrayList<Nota> getAllNotes() {
        ArrayList<Nota> notesList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Nota nota = new Nota();
                nota.setId(Integer.parseInt(cursor.getString(0)));
                nota.setTitolo(cursor.getString(1));
                nota.setTesto(cursor.getString(2));
                nota.setDataCreazione(cursor.getString(3));
                nota.setColore(cursor.getString(4));
                notesList.add(nota);
            } while (cursor.moveToNext());
        }
        return notesList;
    }

    public long addNote(Nota note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitolo());
        values.put(KEY_TEXT, note.getTesto());
        values.put(KEY_DATE, note.getDataCreazione());
        values.put(KEY_COLOR, note.getColore());

        long insert = db.insert(TABLE_NOTES, null, values);
        note.setId((int) insert);
        db.close();
        return insert;
    }

    public int deleteNote(Nota note) {
        SQLiteDatabase db = this.getWritableDatabase();
        int x = db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return x;
    }

    public int updateNote(Nota note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitolo());
        values.put(KEY_TEXT, note.getTesto());
        values.put(KEY_DATE, note.getDataCreazione());
        values.put(KEY_COLOR, note.getColore());

        return db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }
}
