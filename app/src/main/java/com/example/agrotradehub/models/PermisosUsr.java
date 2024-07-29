package com.example.agrotradehub.models;

import java.util.ArrayList;

public class PermisosUsr {
    private ArrayList<Permisos> Listapermisos;
    private double proteCost;
    private int nivel, idUsuario, agID;
    private String nombre, fnCode, usuario, agCode;
    private ArrayList<PermisosDoc> listapermisosDocs;

    public String getAgCode() {
        return agCode;
    }

    public void setAgCode(String agCode) {
        this.agCode = agCode;
    }

    public int getAgID() {
        return agID;
    }

    public void setAgID(int agID) {
        this.agID = agID;
    }

    public double getProteCost() {
        return proteCost;
    }

    public void setProteCost(double proteCost) {
        this.proteCost = proteCost;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFnCode() {
        return fnCode;
    }

    public void setFnCode(String fnCode) {
        this.fnCode = fnCode;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Permisos> getListapermisos() {
        return Listapermisos;
    }

    public void setListapermisos(ArrayList<Permisos> listapermisos) {
        Listapermisos = listapermisos;
    }

    public ArrayList<PermisosDoc> getListapermisosDocs() {
        return listapermisosDocs;
    }

    public void setListapermisosDocs(ArrayList<PermisosDoc> listapermisosDocs) {
        this.listapermisosDocs = listapermisosDocs;
    }
}
