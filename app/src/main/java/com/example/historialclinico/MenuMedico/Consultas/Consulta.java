package com.example.historialclinico.MenuMedico.Consultas;

import android.graphics.Bitmap;

public class Consulta {

    private String idConsulta;
    private String token;
    private String idPaciente;
    private String nombreConsulta;
    private String nombrePaciente;
    private String correo;
    private String fechaNacimiento;
    private Bitmap imagen;
    private String sexo;
    private String ubicacion;

    public Consulta(String token,String ubicacion,String idConsulta, String idPaciente, String nombreConsulta, String nombrePaciente, String correo, String fechaNacimiento, Bitmap imagen, String sexo) {
        this.token=token;
        this.ubicacion=ubicacion;
        this.idConsulta = idConsulta;
        this.idPaciente = idPaciente;
        this.nombreConsulta = nombreConsulta;
        this.nombrePaciente = nombrePaciente;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.imagen = imagen;
        this.sexo = sexo;
    }

    public Consulta() {

    }

    public String getTokenPaciente() {
        return token;
    }

    public void setTokenPaciente(String token) {
        this.token = token;
    }

    public String getUbicacionConsulta() {
        return ubicacion;
    }

    public void setUbicacionConsulta(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getIdPacienteConsulta() {
        return idPaciente;
    }

    public void setIdPacienteConsulta(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombreConsulta() {
        return nombreConsulta;
    }

    public void setNombreConsulta(String nombreConsulta) {
        this.nombreConsulta = nombreConsulta;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getCorreoConsulta() {
        return correo;
    }

    public void setCorreoConsulta(String correo) {
        this.correo = correo;
    }

    public String getFechaNacimientoConsulta() {
        return fechaNacimiento;
    }

    public void setFechaNacimientoConsulta(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Bitmap getImagenConsulta() {
        return imagen;
    }

    public void setImagenConsulta(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getSexoConsulta() {
        return sexo;
    }

    public void setSexoConsulta(String sexo) {
        this.sexo = sexo;
    }
}
