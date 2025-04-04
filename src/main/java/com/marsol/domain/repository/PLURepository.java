package com.marsol.domain.repository;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.Scale;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del repositorio en el dominio, definen metodos de acceso a datos desde la perspectiva del dominio
 */
public interface PLURepository {
    public List<String> getAllArticles(Scale scale);
    public Map<String,List<String>> getDisabledArticles(List<Scale> scales);
    public List<String> getEnabledArticles(Scale scale);
    public List<ArticleDTO> getListArticles(List<String> ids);
    public List<String> getDisabledArticles2(Scale scale);
}
