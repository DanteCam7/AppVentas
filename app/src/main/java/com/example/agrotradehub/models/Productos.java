package com.example.agrotradehub.models;

import android.graphics.Bitmap;

import java.util.List;

public class Productos {
    String id,name, linkfichatecnica, ingredienteActivo, unidad, marca, tipo, imagen;
    Double costo, precioSelect, importe, existencia2;
    boolean inHistory;
    int existencia, totalCarrito, movImpp1iva, movImpp2ieps;
    List<Double> precios;

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public int getMovImpp1iva() {
        return movImpp1iva;
    }

    public void setMovImpp1iva(int movImpp1iva) {
        this.movImpp1iva = movImpp1iva;
    }

    public int getMovImpp2ieps() {
        return movImpp2ieps;
    }

    public void setMovImpp2ieps(int movImpp2ieps) {
        this.movImpp2ieps = movImpp2ieps;
    }

    public String getFotoProducto() {
        return imagen;
    }

    public void setFotoProducto(String imagen) {
        this.imagen = imagen;
    }

    public Double getPrecioSelect() {
        return precioSelect;
    }

    public void setPrecioSelect(Double precioSelect) {
        this.precioSelect = precioSelect;
    }

    public int getTotalCarrito() {
        return totalCarrito;
    }

    public void setTotalCarrito(int totalCarrito) {
        this.totalCarrito = totalCarrito;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public void setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return linkfichatecnica;
    }

    public void setDescripcion(String descripcion) {
        this.linkfichatecnica = descripcion;
    }

    public List<Double> getPrecios() {
        return precios;
    }

    public void setPrecios(List<Double> precios) {
        this.precios = precios;
    }

    public String getLinkfichatecnica() {
        return linkfichatecnica;
    }

    public void setLinkfichatecnica(String linkfichatecnica) {
        this.linkfichatecnica = linkfichatecnica;
    }

    public String getIngredienteActivo() {
        return ingredienteActivo;
    }

    public void setIngredienteActivo(String ingredienteActivo) {
        this.ingredienteActivo = ingredienteActivo;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public double getExistencia2() {
        return existencia2;
    }

    public void setExistencia2(double existencia) {
        this.existencia2 = existencia;
    }
}
