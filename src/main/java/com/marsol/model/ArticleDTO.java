package com.marsol.model;

import java.sql.Timestamp;

public class ArticleDTO {
    private final String id;
    private final String description;
    private final int price;
    private final int measure;
    private final String ressanitaria;
    private final String conservacion;
    private final int duracion;
    private final Double tara;
    private final int imprimir;
    private final String procedencia;
    private final int idFrigo;
    private final Timestamp fecben;
    private final String faenado;
    private final String embalaje;
    private final int afeley;

    private ArticleDTO(Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.price = builder.price;
        this.measure = builder.measure;
        this.ressanitaria = builder.ressanitaria;
        this.conservacion = builder.conservacion;
        this.duracion = builder.duracion;
        this.tara = builder.tara;
        this.imprimir = builder.imprimir;
        this.procedencia = builder.procedencia;
        this.idFrigo = builder.idFrigo;
        this.fecben = builder.fecben;
        this.faenado = builder.faenado;
        this.embalaje = builder.embalaje;
        this.afeley = builder.afeley;
    }

    // Getters
    public String getId() { return id; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getMeasure() { return measure; }
    public String getRessanitaria() { return ressanitaria; }
    public String getConservacion() { return conservacion; }
    public int getDuracion() { return duracion; }
    public Double getTara() { return tara; }
    public int getImprimir() { return imprimir; }
    public String getProcedencia() { return procedencia; }
    public int getIdFrigo() { return idFrigo; }
    public Timestamp getFecben() { return fecben; }
    public String getFaenado() { return faenado; }
    public String getEmbalaje() { return embalaje; }
    public int getAfeley() { return afeley; }

    // Builder Pattern
    public static class Builder {
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

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder setMeasure(int measure) {
            this.measure = measure;
            return this;
        }

        public Builder setRessanitaria(String ressanitaria) {
            this.ressanitaria = ressanitaria;
            return this;
        }

        public Builder setConservacion(String conservacion) {
            this.conservacion = conservacion;
            return this;
        }

        public Builder setDuracion(int duracion) {
            this.duracion = duracion;
            return this;
        }

        public Builder setTara(Double tara) {
            this.tara = tara;
            return this;
        }

        public Builder setImprimir(int imprimir) {
            this.imprimir = imprimir;
            return this;
        }

        public Builder setProcedencia(String procedencia) {
            this.procedencia = procedencia;
            return this;
        }

        public Builder setIdFrigo(int idFrigo) {
            this.idFrigo = idFrigo;
            return this;
        }

        public Builder setFecben(Timestamp fecben) {
            this.fecben = fecben;
            return this;
        }

        public Builder setFaenado(String faenado) {
            this.faenado = faenado;
            return this;
        }

        public Builder setEmbalaje(String embalaje) {
            this.embalaje = embalaje;
            return this;
        }

        public Builder setAfeley(int afeley) {
            this.afeley = afeley;
            return this;
        }

        public ArticleDTO build() {
            return new ArticleDTO(this);
        }
    }
}
