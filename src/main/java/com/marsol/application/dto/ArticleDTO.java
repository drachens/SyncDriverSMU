package com.marsol.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
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
    private final String brand;
    private final String additionalDescription;
}
