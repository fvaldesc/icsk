package com.icsk.comandaoperadorv5;

public class CIngresoActividades {

    String codigoActividad;
    String nombreActividad;

    int    eliminarActividad;
    int    modificarActividad;


    public CIngresoActividades(String nombreActividad, int eliminarActividad, int modificarActividad) {
        this.nombreActividad = nombreActividad;
        this.eliminarActividad = eliminarActividad;
        this.modificarActividad = modificarActividad;
    }


    public int getEliminarActividad() {
        return eliminarActividad;
    }

    public void setEliminarActividad(int eliminarActividad) {
        this.eliminarActividad = eliminarActividad;
    }

    public int getModificarActividad() {
        return modificarActividad;
    }

    public void setModificarActividad(int modificarActividad) {
        this.modificarActividad = modificarActividad;
    }


    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }
}
