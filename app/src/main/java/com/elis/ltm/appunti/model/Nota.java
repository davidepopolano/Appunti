package com.elis.ltm.appunti.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by davide on 13/03/17.
 */

public class Nota {
    private String titolo;
    private String testo;
    private String colore;
    private String dataCreazione;
    private int id;

    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Nota(){
        date = Calendar.getInstance().getTime();
        dataCreazione = dateFormat.format(date);
    }


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
