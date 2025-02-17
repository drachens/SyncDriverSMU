package com.marsol.controller;

import com.marsol.extraction.DatabaseService;
import com.marsol.model.Article;
import com.marsol.model.ResSanitaria;
import com.marsol.model.Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResSanitariaController {
    private final DatabaseService databaseService;
    private final PluController pluController;
    @Autowired
    public ResSanitariaController(DatabaseService databaseService, PluController pluController) {
        this.databaseService = databaseService;
        this.pluController = pluController;
    }

    public List<Map<String,Object>> getStoreInfo(){
        List<Map<String,Object>> storeInfo = new ArrayList<>();
        storeInfo = databaseService.getAllStores();
        return storeInfo;
    }

    public List<Article> getArticleInfo(Scale scale){
        List<Article> articleInfo = new ArrayList<>();
        articleInfo = pluController.getPluArticleInfo(scale);
        return articleInfo;
    }

    public List<ResSanitaria> getResSanitaria(Scale scale){
        List<Map<String,Object>> storeInfo = getStoreInfo();
        List<Article> articleInfo = getArticleInfo(scale);
        List<ResSanitaria> resSanitariaList = new ArrayList<>();

        String direccion = storeInfo.get(0).get("direccion").toString();
        String nombre_tienda = storeInfo.get(0).get("nombre").toString();

        for(Article article : articleInfo){
            ResSanitaria resSanitaria = new ResSanitaria();
            resSanitaria.setId(article.getId());
            resSanitaria.setEmbalaje(article.getEmbalaje());
            resSanitaria.setResSanitaria(article.getRessanitaria());
            resSanitaria.setDireccion(direccion);
            resSanitaria.setNombre_tienda(nombre_tienda);
            resSanitariaList.add(resSanitaria);
        }
        return resSanitariaList;
    }
}
