package com.marsol.model;

import java.sql.Timestamp;

public class Scale {
    private final int balId;
    private final Timestamp updated;
    private final String nombre;
    private final int etiqueta_u;
    private final int etiqueta_p;
    private final int letraPlu;
    private final int letraIng;
    private String ip;
    private final int puerto;
    private final int masivo;
    private final int estado;
    private String typeLastUpdate;
    private Timestamp lastUpdate;

    // Constructor privado para que sólo se pueda crear a través del Builder
    public Scale(Builder builder) {
        this.balId = builder.balId;
        this.updated = builder.updated;
        this.nombre = builder.nombre;
        this.etiqueta_u = builder.etiqueta_u;
        this.etiqueta_p = builder.etiqueta_p;
        this.letraPlu = builder.letraPlu;
        this.letraIng = builder.letraIng;
        this.ip = builder.ip;
        this.puerto = builder.puerto;
        this.masivo = builder.masivo;
        this.estado = builder.estado;
        this.typeLastUpdate = builder.typeLastUpdate;
        this.lastUpdate = builder.lastUpdate;
    }

    // Getters
    public int getBalId() {
        return balId;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEtiqueta_u() {
        return etiqueta_u;
    }

    public int getEtiqueta_p() {
        return etiqueta_p;
    }

    public int getLetraPlu() {
        return letraPlu;
    }

    public int getLetraIng() {
        return letraIng;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public int getMasivo() {
        return masivo;
    }

    public int getEstado() {
        return estado;
    }

    public String getTypeLastUpdate() {return typeLastUpdate;}

    public Timestamp getLastUpdate() {return lastUpdate;}

    //Setters
    public void setTypeLastUpdate(String typeLastUpdate) {this.typeLastUpdate = typeLastUpdate;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setIp(String ip) {this.ip = ip;}

    @Override
    public String toString() {
        return "Scale{" +
                "balId=" + balId +
                ", updated=" + updated +
                ", nombre='" + nombre + '\'' +
                ", etiqueta_u=" + etiqueta_u +
                ", etiqueta_p=" + etiqueta_p +
                ", letraPlu=" + letraPlu +
                ", letraIng=" + letraIng +
                ", ip='" + ip + '\'' +
                ", puerto=" + puerto +
                ", masivo=" + masivo +
                ", estado=" + estado +
                ", typeLastUpdate='" + typeLastUpdate + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    // Clase estática interna Builder
    public static class Builder {
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
        private String typeLastUpdate;
        private Timestamp lastUpdate;

        public Builder() {
            // Constructor por defecto del builder
        }

        public Builder balId(int balId) {
            this.balId = balId;
            return this;
        }

        public Builder updated(Timestamp updated) {
            this.updated = updated;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder etiqueta_u(int etiqueta_u) {
            this.etiqueta_u = etiqueta_u;
            return this;
        }

        public Builder etiqueta_p(int etiqueta_p) {
            this.etiqueta_p = etiqueta_p;
            return this;
        }

        public Builder letraPlu(int letraPlu) {
            this.letraPlu = letraPlu;
            return this;
        }

        public Builder letraIng(int letraIng) {
            this.letraIng = letraIng;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder puerto(int puerto) {
            this.puerto = puerto;
            return this;
        }

        public Builder masivo(int masivo) {
            this.masivo = masivo;
            return this;
        }

        public Builder estado(int estado) {
            this.estado = estado;
            return this;
        }

        public Builder typeLastUpdate(String typeLastUpdate) {
            this.typeLastUpdate = typeLastUpdate;
            return this;
        }

        public Builder lastUpdate(Timestamp lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Scale build() {
            return new Scale(this);
        }
    }

}
