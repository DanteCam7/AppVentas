package com.example.agrotradehub.models;

import java.util.ArrayList;
import java.util.Date;

public class DetalleDoc {
    private ArrayList<Productos> productosDetalles;
    private String CRAZONSOCIAL, COBSERVACIONES, CNOMBRECONCEPTO, CRFC, CSERIEDOCUMENTO;
    private int CIDDOCUMENTO, CFOLIO;
    private double CIMPUESTO1IVA, CIMPUESTO2IEPS, DocTotal;
    private boolean CCANCELADO, CIMPRESO;
    private Date docFecha;

    public Date getDocFecha() {
        return docFecha;
    }

    public void setDocFecha(Date docFecha) {
        this.docFecha = docFecha;
    }

    public String getCSERIEDOCUMENTO() {
        return CSERIEDOCUMENTO;
    }

    public void setCSERIEDOCUMENTO(String CSERIEDOCUMENTO) {
        this.CSERIEDOCUMENTO = CSERIEDOCUMENTO;
    }

    public double getDocTotal() {
        return DocTotal;
    }

    public ArrayList<Productos> getProductosDetalles() {
        return productosDetalles;
    }

    public void setProductosDetalles(ArrayList<Productos> productosDetalles) {
        this.productosDetalles = productosDetalles;
    }

    public String getCRAZONSOCIAL() {
        return CRAZONSOCIAL;
    }

    public void setCRAZONSOCIAL(String CRAZONSOCIAL) {
        this.CRAZONSOCIAL = CRAZONSOCIAL;
    }

    public String getCOBSERVACIONES() {
        return COBSERVACIONES;
    }

    public void setCOBSERVACIONES(String COBSERVACIONES) {
        this.COBSERVACIONES = COBSERVACIONES;
    }

    public String getCNOMBRECONCEPTO() {
        return CNOMBRECONCEPTO;
    }

    public void setCNOMBRECONCEPTO(String CNOMBRECONCEPTO) {
        this.CNOMBRECONCEPTO = CNOMBRECONCEPTO;
    }

    public String getCRFC() {
        return CRFC;
    }

    public void setCRFC(String CRFC) {
        this.CRFC = CRFC;
    }

    public int getCIDDOCUMENTO() {
        return CIDDOCUMENTO;
    }

    public void setCIDDOCUMENTO(int CIDDOCUMENTO) {
        this.CIDDOCUMENTO = CIDDOCUMENTO;
    }

    public int getCFOLIO() {
        return CFOLIO;
    }

    public void setCFOLIO(int CFOLIO) {
        this.CFOLIO = CFOLIO;
    }

    public double getCIMPUESTO1IVA() {
        return CIMPUESTO1IVA;
    }

    public void setCIMPUESTO1IVA(double CIMPUESTO1IVA) {
        this.CIMPUESTO1IVA = CIMPUESTO1IVA;
    }

    public double getCIMPUESTO2IEPS() {
        return CIMPUESTO2IEPS;
    }

    public void setCIMPUESTO2IEPS(double CIMPUESTO2IEPS) {
        this.CIMPUESTO2IEPS = CIMPUESTO2IEPS;
    }

    public boolean isCCANCELADO() {
        return CCANCELADO;
    }

    public void setCCANCELADO(boolean CCANCELADO) {
        this.CCANCELADO = CCANCELADO;
    }

    public boolean isCIMPRESO() {
        return CIMPRESO;
    }

    public void setCIMPRESO(boolean CIMPRESO) {
        this.CIMPRESO = CIMPRESO;
    }

    public double isDocTotal() {
        return DocTotal;
    }

    public void setDocTotal(double docTotal) {
        DocTotal = docTotal;
    }
}
