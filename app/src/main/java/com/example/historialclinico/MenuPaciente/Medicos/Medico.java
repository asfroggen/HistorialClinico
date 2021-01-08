package com.example.historialclinico.MenuPaciente.Medicos;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Medico implements Serializable {
    private String id;
    private String nombre;
    private String correo;
    private String especialidad;
    private String ubicacion;
    private Bitmap imagen;
    private String estado;

    public Medico(){

    }

    public Medico(String id,String nombre, String correo, String especialidad,String ubicacion,Bitmap imagen,String estado) {
        this.id=id;
        this.nombre = nombre;
        this.correo = correo;
        this.especialidad = especialidad;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
        this.estado=estado;
    }

    public String getEstadoMedico() {
        return estado;
    }

    public void setEstadoMedico(String estado) {
        this.estado = estado;
    }

    public String getIdMedico() {
        return id;
    }

    public void setIdMedico(String id) {
        this.id = id;
    }

    public String getNombreMedico() {
        return nombre;
    }

    public void setNombreMedico(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoMedico() {
        return correo;
    }

    public void setCorreoMedico(String correo) {
        this.correo = correo;
    }

    public String getEspecialidadMedico() {
        return especialidad;
    }

    public void setEspecialidadMedico(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setUbicacionMedico(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUbicacionMedico() {
        return ubicacion;
    }

    public Bitmap getImagenMedico() {
        return imagen;
    }

    public void setImagenMedico(Bitmap imagen) {
        this.imagen = imagen;
    }
}
