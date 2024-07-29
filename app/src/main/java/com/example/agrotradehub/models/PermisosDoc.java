package com.example.agrotradehub.models;

public class PermisosDoc {
    private int idDoc, cancelacion, creacion, impresion;
    private String nombreDoc;

    public int getImpresion() {
        return impresion;
    }

    public void setImpresion(int impresion) {
        this.impresion = impresion;
    }

    public int getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public int getCancelacion() {
        return cancelacion;
    }

    public void setCancelacion(int cancelacion) {
        this.cancelacion = cancelacion;
    }

    public int getCreacion() {
        return creacion;
    }

    public void setCreacion(int creacion) {
        this.creacion = creacion;
    }

    public String getNombreDoc() {
        return nombreDoc;
    }

    public void setNombreDoc(String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }
}
