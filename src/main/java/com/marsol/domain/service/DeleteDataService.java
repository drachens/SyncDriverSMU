package com.marsol.domain.service;


import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.integration.SyncDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class DeleteDataService {
    private static final Logger logger = LoggerFactory.getLogger(DeleteDataService.class);
    private SyncDataLoader syncDataLoader;


    private final DataExtractionService dataExtractionService;

    @Value("${directory.pendings}")
    private String pendings;

    @Autowired
    public DeleteDataService(DataExtractionService dataExtractionService) {
        this.dataExtractionService = dataExtractionService;
        this.syncDataLoader = new SyncDataLoader();
    }

    /**
     * Primero hay que leer el archivo plu_.txt
     * Guardar los id de los articulos habilitados.
     * Consultar los id de los productos deshabilitados en la nueva carga.
     * Comparar ambas listas, si hay un match se deben borrar los datos, al menos de PLU
     */



    public Map<String,Integer> getScalesToClean(List<Scale> scales){
        Map<String, List<Integer>> lfCodesByScale = new LinkedHashMap<>();

        for(Scale scale : scales){
            String idBal = String.valueOf(scale.getBalId());
            String filename = String.format("%splu_%s.txt",pendings,idBal);

            List<Integer> lfCodes = extractFromFile(filename);
            lfCodesByScale.put(idBal,lfCodes);
        }

        Map<String, List<String>> disabledArticlesId = getDisabledArticles(scales);
        return findMatchingScales(lfCodesByScale, disabledArticlesId);
    }

    public void clearScale(String ip){
       try{
           syncDataLoader.clearPlu(ip);

       }catch(Exception e){
           logger.error(e.getMessage());
       }
    }



    private Map<String,Integer> findMatchingScales(Map<String, List<Integer>> lfCodesByScale,
                                            Map<String, List<String>> disabledArticlesId){
        Map<String, Integer> matchingScales = new LinkedHashMap<>();

        for(Map.Entry<String, List<Integer>> entry : lfCodesByScale.entrySet()){
            String scaleId = entry.getKey();
            List<Integer> lfCodes = entry.getValue();
            int countArticles = lfCodes.size();
            //Verificar si hay al menos 1 match
            if(disabledArticlesId.containsKey(scaleId)){
                List<String> disabledArticles = disabledArticlesId.get(scaleId);
                for(Integer lfcode : lfCodes){
                    if(disabledArticles.contains(lfcode)){
                        matchingScales.put(scaleId,countArticles);
                        break;
                    }
                }
            }
        }
        return matchingScales;
    }

    private List<Integer> extractFromFile(String filename){

        List<Integer> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            boolean firstLine = true; //Omitir cabecera

            while((line = br.readLine()) != null){
                if(firstLine){
                    firstLine = false;
                    continue;
                }
                String[] values = line.split("\t");
                if(values.length > 0){
                    try{
                        int lfCode = Integer.parseInt(values[0]);
                        result.add(lfCode);
                    }catch(NumberFormatException e){
                        logger.error("Error durante lectura de ultimos PLU cargados. {}",e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Map<String,List<String>> getDisabledArticles(List<Scale> scales){
        return dataExtractionService.getDisabledArticles(scales);

    }
}
