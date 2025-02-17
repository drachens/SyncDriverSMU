package com.marsol.controller;

import com.marsol.extraction.DatabaseService;
import com.marsol.model.Article;
import com.marsol.model.PLU;
import com.marsol.model.Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PluController {
    private final DatabaseService databaseService;

    @Autowired
    public PluController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    // Función principal: Retorna una lista de información sobre los productos asociados a una balanza.
    public List<Article> getPluArticleInfo(Scale scale){
        List<String> pluIds = getPluToLoad(scale);
        if(pluIds.isEmpty()){
            System.out.println("No plu loaded");
            return null;
        }
        return databaseService.getPluItems(pluIds);
    }

    // Función: Retorna una lista de IDs de artículos que deben ser eliminados de acuerdo a las condiciones.
    public List<String> getPluToDelete(Scale scale) {
        int idBal = scale.getBalId();
        List<Map<String,Object>> disabledZmateriales = databaseService.getDisabledZmateriales(idBal);
        List<Map<String,Object>> disabledZmatebalanza = databaseService.getDisabledZmatebalanza(idBal);
        List<Map<String,Object>> disabledArticles = databaseService.getDisabledArticles(idBal);

        Set<String> zmaterialesIds = extractIdFromMap(disabledZmateriales);
        Set<String> zmatebalanzaIds = extractIdFromMap(disabledZmatebalanza);
        Set<String> articleIds = extractIdFromMap(disabledArticles);

        zmaterialesIds.retainAll(zmatebalanzaIds);
        zmaterialesIds.retainAll(articleIds);

        return new ArrayList<>(zmaterialesIds);
    }

    // Función: Retorna una lista de IDs de artículos a cargar, diferenciando entre cargas masivas y filtradas.
    public List<String> getPluToLoad(Scale scale){
        int masivo = scale.getMasivo();
        switch (masivo){
            case 0:
                return getFilteredPluId(scale); // Carga filtrada
            case 1:
                return getMassivePluId(scale); // Carga masiva
            default:
                System.out.println("Error");
                return Collections.emptyList();
        }
    }

    // Función: Retorna una lista de IDs de productos filtrados a cargar (usada en getPluToLoad).
    public List<String> getFilteredPluId(Scale scale){
        int idBal = scale.getBalId();
        List<Map<String,Object>> enabledZmateriales = databaseService.getEnabledZmateriales(idBal);
        List<Map<String,Object>> enabledZmatebalanza = databaseService.getEnabledZmatebalanza(idBal);
        List<Map<String,Object>> enabledArticles = databaseService.getEnabledArticles(idBal);

        Set<String> zmaterialesIds = extractIdFromMap(enabledZmateriales);
        Set<String> zmatebalanzaIds = extractIdFromMap(enabledZmatebalanza);
        Set<String> articleIds = extractIdFromMap(enabledArticles);

        zmaterialesIds.retainAll(zmatebalanzaIds);
        zmaterialesIds.retainAll(articleIds);

        //System.out.println(zmaterialesIds);

        return new ArrayList<>(zmaterialesIds);
    }

    // Función: Retorna una lista de IDs de artículos masivos a cargar (usada en getPluToLoad).
    public List<String> getMassivePluId(Scale scale){
        int idBal = scale.getBalId();
        List<Map<String,Object>> idsToLoad = databaseService.getMasiveArticles(idBal);
        return new ArrayList<>(extractIdFromMap(idsToLoad));
    }

    // Función privada: Extrae los IDs de una lista de mapas y los devuelve en un Set (sin duplicados).
    private Set<String> extractIdFromMap(List<Map<String,Object>> dataList){
        return dataList.stream()
                .map(map -> (String) map.get("id"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
