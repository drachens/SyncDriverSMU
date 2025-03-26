package com.marsol.domain.service;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.application.dto.Note1DTO;
import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.repository.PluDAO;
import com.marsol.infrastructure.repository.ScaleDAO;
import com.marsol.infrastructure.repository.StoreDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataExtractionService {
    private final PluDAO pluDAO;
    private final ScaleDAO scaleDAO;
    private final StoreDAO storeDAO;

    private static final Logger logger = LoggerFactory.getLogger(DataExtractionService.class);

    @Autowired
    public DataExtractionService(PluDAO pluDAO, ScaleDAO scaleDAO, StoreDAO storeDAO) {
        this.pluDAO = pluDAO;
        this.scaleDAO = scaleDAO;
        this.storeDAO = storeDAO;
    }

    public List<String> getListOfEnabledArticles(Scale scale) {
        return pluDAO.getEnabledArticles(scale);
    }

    public List<String> getListOfEnabledArticlesMassive(Scale scale){
        return pluDAO.getEnabledArticlesMasive(scale);
    }

    public List<ArticleDTO> getEnabledArticles(Scale scale) {
        List<String> listIdsEnabled = pluDAO.getEnabledArticles(scale).stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        return pluDAO.getListArticles(listIdsEnabled);
    }

    public List<ArticleDTO> getEnabledArticlesMasive(Scale scale){
        List<String> listIdsEnabled = pluDAO.getEnabledArticlesMasive(scale).stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        return pluDAO.getListArticles(listIdsEnabled);
    }

    public Map<String,List<String>> getDisabledArticles(List<Scale> scales) {
        return pluDAO.getDisabledArticles(scales);
    }

    public List<Scale> getEnabledScales() {
        return scaleDAO.findEnabledScales();
    }

    public List<Note1DTO> getStoreInfo(List<String> ids){
        try{
            return storeDAO.getAllStoresInfo(ids);
        }catch(Exception e){
            logger.error("Error durante la obtencion de información de tienda: {}", e.getMessage());
            return null;
        }
    }


    public void getEnabledInfonut(){

    }


}
