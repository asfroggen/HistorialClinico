package com.example.historialclinico.MenuMedico.Pacientes;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Paciente implements Serializable {

    private String id;
    private String nombre;
    private String correo;
    private String fechaNacimiento;
    private Bitmap imagen;
    private String sexo;

    public Paciente(){

    }

    public Paciente(String id, String nombre, String correo, String fechaNacimiento, Bitmap imagen, String sexo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.imagen = imagen;
        this.sexo = sexo;
    }

    public String getIdPacientes() {
        return id;
    }

    public void setIdPacientes(String id) {
        this.id = id;
    }

    public String getNombrePacientes() {
        return nombre;
    }

    public void setNombrePacientes(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoPacientes() {
        return correo;
    }

    public void setCorreoPacientes(String correo) {
        this.correo = correo;
    }

    public String getFechaNacimientoPacientes() {
        return fechaNacimiento;
    }

    public void setFechaNacimientoPacientes(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Bitmap getImagenPacientes() {
        return imagen;
    }

    public void setImagenPacientes(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getSexoPacientes() {
        return sexo;
    }

    public void setSexoPacientes(String sexo) {
        this.sexo = sexo;
    }
}
