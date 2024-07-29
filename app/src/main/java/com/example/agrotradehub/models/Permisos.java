package com.example.agrotradehub.models;

import java.util.Objects;

public class Permisos {
    private String name;
    private boolean activo;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
