package com.marsol.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note1DTO {
    private int idArticle; //ID Articulo
    private String nombre;
    private String direccion;
    private String resSanitaria;
    private String embalaje;

}

