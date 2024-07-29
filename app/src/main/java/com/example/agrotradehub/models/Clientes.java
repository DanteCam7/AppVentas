package com.example.agrotradehub.models;

import androidx.annotation.NonNull;

import java.sql.Date;

public class Clientes {
    private String codigo, razonsocial, RFC, usoCFDI, email, regimenFiscal;
    private int id, moneda, listapreciocliente, limiteCredito, diasCredito, agenteVenta, codPostal;

    public Clientes() {
    }
    public Clientes(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getUsoCFDI() {
        return usoCFDI;
    }

    public void setUsoCFDI(String usoCFDI) {
        this.usoCFDI = usoCFDI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegimenFiscal() {
        return regimenFiscal;
    }

    public void setRegimenFiscal(String regimenFiscal) {
        this.regimenFiscal = regimenFiscal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public int getListapreciocliente() {
        return listapreciocliente;
    }

    public void setListapreciocliente(int listapreciocliente) {
        this.listapreciocliente = listapreciocliente;
    }

    public int getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(int limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public int getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(int diasCredito) {
        this.diasCredito = diasCredito;
    }

    public int getAgenteVenta() {
        return agenteVenta;
    }

    public void setAgenteVenta(int agenteVenta) {
        this.agenteVenta = agenteVenta;
    }

    public int getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(int codPostal) {
        this.codPostal = codPostal;
    }

    @NonNull
    @Override
    public String toString() {
        return razonsocial;
    }
}
