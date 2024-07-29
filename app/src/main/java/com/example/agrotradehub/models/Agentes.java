package com.example.agrotradehub.models;

import androidx.annotation.NonNull;

public class Agentes
{
    private int CIDAGENTE;
    private String CCODIGOAGENTE, CNOMBREAGENTE;

    public int getCIDAGENTE() {
        return CIDAGENTE;
    }

    public void setCIDAGENTE(int CIDAGENTE) {
        this.CIDAGENTE = CIDAGENTE;
    }

    public String getCCODIGOAGENTE() {
        return CCODIGOAGENTE;
    }

    public void setCCODIGOAGENTE(String CCODIGOAGENTE) {
        this.CCODIGOAGENTE = CCODIGOAGENTE;
    }

    public String getCNOMBREAGENTE() {
        return CNOMBREAGENTE;
    }

    public void setCNOMBREAGENTE(String CNOMBREAGENTE) {
        this.CNOMBREAGENTE = CNOMBREAGENTE;
    }

    @NonNull
    @Override
    public String toString() {
        return CNOMBREAGENTE;
    }
}
