package com.icsk.comandaoperadorv5;

public class CActividadesOperador {

    String codigo ;
    String codigoNombre ;
    int     n;
    public CActividadesOperador(String codigo, String codigoNombre) {
        this.codigo = codigo;
        this.codigoNombre = codigoNombre;
        n=4;
    }



    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoNombre() {
        return codigoNombre;
    }

    public void setCodigoNombre(String codigoNombre) {
        this.codigoNombre = codigoNombre;
    }



}
