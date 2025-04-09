package com.marsol.domain.service;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.handler.*;
import com.marsol.domain.model.PLU;
import com.marsol.domain.model.Scale;
import com.marsol.utils.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PluTransformationService {
    private final static Logger logger = LoggerFactory.getLogger(PluTransformationService.class);
    private final DataExtractionService dataExtractionService;
    @Value("${directory.pendings}")
    private String directoryPendings;

    @Autowired
    public PluTransformationService(DataExtractionService dataExtractionService) {
        this.dataExtractionService = dataExtractionService;
    }

    private BaseHandler initializeHandlerChain(){
        BaseHandler wheightUnitHandler = new WeightUnitHandler();
        BaseHandler barcodeHandler = new BarcodeHandler();
        BaseHandler itemCodeHandler = new ItemCodeHandler();
        wheightUnitHandler.setNext(barcodeHandler).setNext(itemCodeHandler);
        return wheightUnitHandler;
    }
    private BaseHandler initializeDatesHandlerChain(){
        return new DatesHandler();
    }

    public void transformDataPlu(Scale scale) throws FileNotFoundException {
        int idBal = scale.getBalId();
        BaseHandler wheightUnitHandler = initializeHandlerChain();
        BaseHandler datesHandler = initializeDatesHandlerChain();
        List<ArticleDTO> articlesDTOS = new ArrayList<>();
        /**
         * Si la carga es masiva, debo enviar TODOS los articulos.
         * Cuando la carga no es masiva, entonces se deben enviar solo las actualizaciones.
         */
        if(scale.getMasivo() == 1){
            articlesDTOS = dataExtractionService.getEnabledArticlesMasive(scale);
        }else{
            articlesDTOS = dataExtractionService.getEnabledArticles(scale);
        }
        List<PLU> plus = new ArrayList<>();
        for (ArticleDTO articleDTO : articlesDTOS) {
            PLU plu = new PLU();
            plu.setLFCode(Integer.parseInt(articleDTO.getId()));
            plu.setName1(articleDTO.getDescription());
            plu.setName2(articleDTO.getBrand());
            plu.setName3(articleDTO.getAdditionalDescription());
            plu.setProducedDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss")));
            plu.setUnitPrice(articleDTO.getPrice());
            plu.setDepartment(1);
            plu.setLabel1(32);
            plu.setValidDays(articleDTO.getDuracion());
            //plu.setTareWeight((int) articleDTO.getTara());
            wheightUnitHandler.handle(plu,articleDTO);
            plu.setDiscountStartDateTime("");
            plu.setDiscountEndDateTime("");
            datesHandler.handle(plu,articleDTO);
            plus.add(plu);
        }
        logger.info("Total de PLUs a enviar a balanza -> {} : {}", idBal, plus.size());
        writeListPlu(plus,String.valueOf(scale.getBalId()));
    }

    public void reloadAndFilterDataPlu(List<PLU> filteredPlus, List<ArticleDTO> updateArticles, String balId) throws FileNotFoundException {
        logger.debug("Iniciando Reload And Filter Data PLU");
        logger.debug("filteredPlus:{}, updateArticles:{}", filteredPlus.size(), updateArticles.size());
        BaseHandler wheightUnitHandler = initializeHandlerChain();
        BaseHandler datesHandler = initializeDatesHandlerChain();
        List<PLU> plus = new ArrayList<PLU>();
        if(!updateArticles.isEmpty()){
            try{
                for (ArticleDTO articleDTO : updateArticles) {
                    PLU plu = new PLU();
                    plu.setLFCode(Integer.parseInt(articleDTO.getId()));
                    plu.setName1(articleDTO.getDescription());
                    plu.setName2(articleDTO.getBrand());
                    plu.setName3(articleDTO.getAdditionalDescription());
                    plu.setProducedDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss")));
                    plu.setUnitPrice(articleDTO.getPrice());
                    plu.setDepartment(1);
                    plu.setLabel1(32);
                    plu.setValidDays(articleDTO.getDuracion());
                    //plu.setTareWeight((int) articleDTO.getTara());
                    wheightUnitHandler.handle(plu,articleDTO);
                    plu.setDiscountStartDateTime(null);
                    plu.setDiscountEndDateTime(null);
                    datesHandler.handle(plu,articleDTO);
                    plus.add(plu);
                }
                logger.debug("plus:{}",plus.size());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }else{
            logger.debug("Articles vacio.");
            plus = Collections.emptyList();
        }
        logger.debug("filteredPlu:{}",filteredPlus);
        logger.debug("updateArticles:{}",plus);
        List<PLU> newPLUs = updatePluList(filteredPlus,plus);
        writeListPlu(newPLUs,String.valueOf(balId));
    }

    private void writeListPlu(List<PLU> plus, String balId) throws FileNotFoundException {
        if(plus.isEmpty()){
            logger.warn("No hay PLUS nuevos para balanza -> {}", balId);
            return;
        }
        logger.debug("Ejecutando escritura de PLUs ({})",plus.size());
        String filename = String.format("%splu_%s.txt", directoryPendings, balId);

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
            writer.write(String.join("\t", HeadersFilesHPRT.PLUHeader));
            writer.newLine();
            for (PLU plu : plus) {
                writer.write(plu.toString());
                writer.newLine();
                //logger.info("plu ({}) escrito.",plu.getLFCode());
            }
            logger.debug("Archivo escrito en: {}",filename);
        } catch (IOException e) {
            logger.error("Error durante la escritura de plu.txt para balanza -> {}",balId);
            throw new RuntimeException(e);
        }
    }

    private List<PLU> updatePluList(List<PLU> original, List<PLU> update){
        if(update.isEmpty()){
            return original;
        }
        try{
            Map<Integer,PLU> mapPlu = new HashMap<>();
            logger.debug("Agregando a mapPlu lista original de PLUs.");
            for(PLU plu : original){
                mapPlu.put(plu.getLFCode(),plu);
            }
            logger.debug("mapPlu.size:{}",mapPlu.size());
            logger.debug("Agregando a mapPlu lista update de PLUs.");
            for(PLU plu:update){
                mapPlu.put(plu.getLFCode(),plu);
            }
            logger.debug("mapPlu.size:{}",mapPlu.size());
            List<PLU> newPLUs = new ArrayList<>(mapPlu.values());
            logger.debug("original: {}", newPLUs.size());
            return newPLUs;
        }catch(Exception e){
            logger.error("Error al combinar listas original y update de plus. {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
