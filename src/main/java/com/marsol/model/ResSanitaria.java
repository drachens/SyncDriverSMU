package com.marsol.model;

public class ResSanitaria {
    private String id;
    private String nombre_tienda;
    private String direccion;
    private String resSanitaria;
    private String embalaje;

    public ResSanitaria() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_tienda() {
        return nombre_tienda;
    }

    public void setNombre_tienda(String nombre_tienda) {
        this.nombre_tienda = nombre_tienda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getResSanitaria() {
        return resSanitaria;
    }

    public void setResSanitaria(String resSanitaria) {
        this.resSanitaria = resSanitaria;
    }

    public String getEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(String embalaje) {
        this.embalaje = embalaje;
    }

    //Funcion: Retorna el texto a imprimir en la Nota 1 de productos de vegetales, frutas y panes.
    public String getNote1Simple(){
        String value = "";

        value = value + getEmbalaje().replaceAll("\\s+$", " ") +
                getNombre_tienda() + "{$0A}" +
                getDireccion() + "{$0A}" +
                getResSanitaria();

        return value;
    }
}
