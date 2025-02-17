package com.marsol.model;


import java.sql.Timestamp;

public class Article {
    private String id;
    private String description;
    private int price;
    private int measure;
    private String ressanitaria;
    private String conservacion;
    private int duracion;
    private Double tara;
    private int imprimir;
    private String procedencia;
    private int idFrigo;
    private Timestamp fecben;
    private String faenado;
    private String embalaje;
    private int afeley;

    public Article() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }

    public String getRessanitaria() {
        return ressanitaria;
    }

    public void setRessanitaria(String ressanitaria) {
        this.ressanitaria = ressanitaria;
    }

    public String getConservacion() {
        return conservacion;
    }

    public void setConservacion(String conservacion) {
        this.conservacion = conservacion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Double getTara() {
        return tara;
    }

    public void setTara(Double tara) {
        this.tara = tara;
    }

    public int getImprimir() {
        return imprimir;
    }

    public void setImprimir(int imprimir) {
        this.imprimir = imprimir;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public int getIdFrigo() {
        return idFrigo;
    }

    public void setIdFrigo(int idFrigo) {
        this.idFrigo = idFrigo;
    }

    public Timestamp getFecben() {
        return fecben;
    }

    public void setFecben(Timestamp fecben) {
        this.fecben = fecben;
    }

    public String getFaenado() {
        return faenado;
    }

    public void setFaenado(String faenado) {
        this.faenado = faenado;
    }

    public String getEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(String embalaje) {
        this.embalaje = embalaje;
    }

    public int getAfeley() {
        return afeley;
    }

    public void setAfeley(int afeley) {
        this.afeley = afeley;
    }
}
