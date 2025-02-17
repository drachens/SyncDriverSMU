package com.marsol.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Scale {
    private int balId;
    private Timestamp updated;
    private String nombre;
    private int etiqueta_u;
    private int etiqueta_p;
    private int letraPlu;
    private int letraIng;
    private String ip;
    private int puerto;
    private int masivo;
    private int estado;

    public Scale(){

    }

    public int getBalId() {
        return balId;
    }

    public void setBalId(int balId) {
        this.balId = balId;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEtiqueta_u() {
        return etiqueta_u;
    }

    public void setEtiqueta_u(int etiqueta_u) {
        this.etiqueta_u = etiqueta_u;
    }

    public int getEtiqueta_p() {
        return etiqueta_p;
    }

    public void setEtiqueta_p(int etiqueta_p) {
        this.etiqueta_p = etiqueta_p;
    }

    public int getLetraPlu() {
        return letraPlu;
    }

    public void setLetraPlu(int letraPlu) {
        this.letraPlu = letraPlu;
    }

    public int getLetraIng() {
        return letraIng;
    }

    public void setLetraIng(int letraIng) {
        this.letraIng = letraIng;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public int getMasivo() {
        return masivo;
    }

    public void setMasivo(int masivo) {
        this.masivo = masivo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}


