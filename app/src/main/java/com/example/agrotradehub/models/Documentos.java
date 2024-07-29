package com.example.agrotradehub.models;

import java.util.Date;

public class Documentos {
    private String CCODIGOCLIENTE, CCODIGOAGENTE;
    private int CIDDOCUMENTO, CFOLIO;
    private boolean CCANCELADO, Surtido;
    private double CTOTAL;
    private Date CFECHA;

    public Date getCFECHA() {
        return CFECHA;
    }

    public void setCFECHA(Date CFECHA) {
        this.CFECHA = CFECHA;
    }

    public String getCCODIGOCLIENTE() {
        return CCODIGOCLIENTE;
    }

    public void setCCODIGOCLIENTE(String CCODIGOCLIENTE) {
        this.CCODIGOCLIENTE = CCODIGOCLIENTE;
    }

    public String getCCODIGOAGENTE() {
        return CCODIGOAGENTE;
    }

    public void setCCODIGOAGENTE(String CCODIGOAGENTE) {
        this.CCODIGOAGENTE = CCODIGOAGENTE;
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

    public boolean isCCANCELADO() {
        return CCANCELADO;
    }

    public void setCCANCELADO(boolean CCANCELADO) {
        this.CCANCELADO = CCANCELADO;
    }

    public boolean isSurtido() {
        return Surtido;
    }

    public void setSurtido(boolean surtido) {
        Surtido = surtido;
    }

    public double getCTOTAL() {
        return CTOTAL;
    }

    public void setCTOTAL(double CTOTAL) {
        this.CTOTAL = CTOTAL;
    }
}
