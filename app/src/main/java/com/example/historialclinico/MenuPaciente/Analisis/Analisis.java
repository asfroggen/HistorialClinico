package com.example.historialclinico.MenuPaciente.Analisis;

import java.io.Serializable;

public class Analisis implements Serializable {
    private String idAnalisis;
    private String idPaciente;
    private String fecha;
    private String tipo;

    private String creatinina;
    private String albumina;
    private String acido;

    private String glucosa;
    private String colesterol;
    private String hierro;
    private String nombre;

    private String leucocitos;
    private String eritrocitos;
    private String plaquetas;
    public Analisis() {

    }

    public Analisis(String tipo, String nombre,String idAnalisis, String idPaciente, String fecha, String creatinina, String albumina, String acido, String glucosa, String colesterol, String hierro, String leucocitos, String eritrocitos, String plaquetas) {
        this.tipo=tipo;
        this.nombre=nombre;
        this.idAnalisis = idAnalisis;
        this.idPaciente = idPaciente;
        this.fecha = fecha;
        this.creatinina = creatinina;
        this.albumina = albumina;
        this.acido = acido;
        this.glucosa = glucosa;
        this.colesterol = colesterol;
        this.hierro = hierro;
        this.leucocitos = leucocitos;
        this.eritrocitos = eritrocitos;
        this.plaquetas = plaquetas;
    }



    public String getTipoAnalisis() {
        return tipo;
    }

    public void setTipoAnalisis(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreAnalisis() {
        return nombre;
    }

    public void setNombreAnalisis(String nombre) {
        this.nombre = nombre;
    }

    public String getIdAnalisis() {
        return idAnalisis;
    }

    public void setIdAnalisis(String idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public String getIdPacienteAnalisis() {
        return idPaciente;
    }

    public void setIdPacienteAnalisis(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getFechaAnalisis() {
        return fecha;
    }

    public void setFechaAnalisis(String fecha) {
        this.fecha = fecha;
    }

    public String getCreatininaAnalisis() {
        return creatinina;
    }

    public void setCreatininaAnalisis(String creatinina) {
        this.creatinina = creatinina;
    }

    public String getAlbuminaAnalisis() {
        return albumina;
    }

    public void setAlbuminaAnalisis(String albumina) {
        this.albumina = albumina;
    }

    public String getAcidoAnalisis() {
        return acido;
    }

    public void setAcidoAnalisis(String acido) {
        this.acido = acido;
    }

    public String getGlucosaAnalisis() {
        return glucosa;
    }

    public void setGlucosaAnalisis(String glucosa) {
        this.glucosa = glucosa;
    }

    public String getColesterolAnalisis() {
        return colesterol;
    }

    public void setColesterolAnalisis(String colesterol) {
        this.colesterol = colesterol;
    }

    public String getHierroAnalisis() {
        return hierro;
    }

    public void setHierroAnalisis(String hierro) {
        this.hierro = hierro;
    }

    public String getLeucocitosAnalisis() {
        return leucocitos;
    }

    public void setLeucocitosAnalisis(String leucocitos) {
        this.leucocitos = leucocitos;
    }

    public String getEritrocitosAnalisis() {
        return eritrocitos;
    }

    public void setEritrocitosAnalisis(String eritrocitos) {
        this.eritrocitos = eritrocitos;
    }

    public String getPlaquetasAnalisis() {
        return plaquetas;
    }

    public void setPlaquetasAnalisis(String plaquetas) {
        this.plaquetas = plaquetas;
    }
}
