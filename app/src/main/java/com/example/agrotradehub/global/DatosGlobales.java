package com.example.agrotradehub.global;

import android.app.Application;

import com.example.agrotradehub.models.Clientes;
import com.example.agrotradehub.models.PermisosUsr;

public class DatosGlobales extends Application {
    private String idproducto, moneda, nomProducto, entorno;
    private Clientes cliente;
    private Boolean conExistencia;
    private Double precioDolar;
    private String documentoid;
    private PermisosUsr permisosUsr;

    public String getEntorno() {
        return entorno;
    }

    public void setEntorno(String entorno) {
        this.entorno = entorno;
    }

    public String getDocumentoid() {
        return documentoid;
    }

    public void setDocumentoid(String documentoid) {
        this.documentoid = documentoid;
    }

    public PermisosUsr getPermisosUsr() {
        return permisosUsr;
    }

    public void setPermisosUsr(PermisosUsr permisosUsr) {
        this.permisosUsr = permisosUsr;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Boolean getConExistencia() {
        return conExistencia;
    }

    public void setConExistencia(Boolean conExistencia) {
        this.conExistencia = conExistencia;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Double getPrecioDolar() {
        return precioDolar;
    }

    public void setPrecioDolar(Double precioDolar) {
        this.precioDolar = precioDolar;
    }
}
