package com.marsol.domain.service;


import com.marsol.domain.model.Note;
import com.marsol.domain.model.PLU;
import com.marsol.domain.model.Scale;
import com.marsol.domain.service.backup.BackupNote1;
import com.marsol.domain.service.backup.BackupPLU;
import com.marsol.infrastructure.integration.SyncDataDownloader;
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
    private final SyncDataDownloader syncDataDownloader;
    private final DataExtractionService dataExtractionService;

    @Value("${directory.pendings}")
    private String pendings;
    @Value("${directory_backup}")
    private String backup;

    @Autowired
    public DeleteDataService(DataExtractionService dataExtractionService) {
        this.dataExtractionService = dataExtractionService;
        this.syncDataLoader = new SyncDataLoader();
        this.syncDataDownloader = new SyncDataDownloader();
    }

    /**
     * Primero hay que leer el archivo plu_.txt
     * Guardar los id de los articulos habilitados.
     * Consultar los id de los productos deshabilitados en la nueva carga.
     * Comparar ambas listas, si hay un match se deben borrar los datos, al menos de PLU
     */


    /**
     *
     * @param scales
     * @return Mapa con llave scaleID y value Cantidad de productos deshabilitados.
     */
    public Map<String,Integer> getScalesToClean(List<Scale> scales){
        Map<String, List<Integer>> lfCodesByScale = new LinkedHashMap<>();

        for(Scale scale : scales){
            if(!backUpFiles(scale)){
                logger.error("No se pudieron obtener los PLU de balanza -> {}",scale.getBalId());
                return Collections.emptyMap();
            }
            String idBal = String.valueOf(scale.getBalId());
            String filename = String.format("%splu_%s.txt",backup,idBal);

            List<Integer> lfCodes = BackupPLU.extractLFCode(filename);
            lfCodesByScale.put(idBal,lfCodes);
        }

        Map<String, List<String>> disabledArticlesId = getDisabledArticles(scales);
        return findMatchingScales(lfCodesByScale, disabledArticlesId);
    }

    public void clearScale(String ip){
       try{
           syncDataLoader.clearPlu(ip);
           syncDataLoader.clearNote1(ip);
       }catch(Exception e){
           logger.error(e.getMessage());
       }
    }

    private boolean backUpFiles(Scale scale){
        try{
            return syncDataDownloader.downloadPLU(scale);
        } catch (Exception e) {
            logger.error("No se pudo descargar PLUS de balanza -> {}",scale.getBalId());
            return false;
        }
    }

    /**
     *
     * @param lfCodesByScale Mapa de balanzas que almacenan una lista de los LFCodes que tiene.
     * @param disabledArticlesId Mapa de Balanzas con una lista de articulos deshabilitados que contiene.
     * @return retorna un Mapa de balanzas con la cantidad de productos a eliminar.
     *
     */
    private Map<String,Integer> findMatchingScales(Map<String, List<Integer>> lfCodesByScale,
                                            Map<String, List<String>> disabledArticlesId){

        Map<String, Integer> matchingScales = new LinkedHashMap<>();

        //Recorrer todas las balanzas con su lista de LFCodes.
        for(Map.Entry<String, List<Integer>> entry : lfCodesByScale.entrySet()){
            String scaleId = entry.getKey();
            List<Integer> lfCodes = entry.getValue();
            int countArticles = lfCodes.size();
            int count = 0;
            //Verificar si hay al menos 1 match
            if(disabledArticlesId.containsKey(scaleId)){ //Si existen articulos deshabilitados para esa balanza entonces,
                List<String> disabledArticles = disabledArticlesId.get(scaleId);
                for(Integer lfcode : lfCodes){
                    if(disabledArticles.contains(String.valueOf(lfcode))){
                        count++;
                        //matchingScales.put(scaleId,countArticles);
                        //break;
                    }
                }
                if(count>0){ //Si hay al menos 1 producto deshabilitado en la balanza
                    matchingScales.put(scaleId,count);
                }
            }
        }
        return matchingScales;
    }

    private Map<String,List<String>> getDisabledArticles(List<Scale> scales){
        return dataExtractionService.getDisabledArticles(scales);

    }

    public List<PLU> getActualPLU(Scale scale){
        try{
            syncDataDownloader.downloadPLU(scale);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        int idbal = scale.getBalId();
        String filename = String.format("%splu_%s.txt",backup,idbal);
        return BackupPLU.extractPLUs(filename);
    }
    public List<Note> getActualNote1(Scale scale){
        try {
            syncDataDownloader.downloadNotes(scale,1);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        int idbal = scale.getBalId();
        String filename = String.format("%snote1_%s.txt",backup,idbal);
        return BackupNote1.extractNotes(filename);
    }

    public void processDeletePLUS(Scale scale){




        /**
         * Obtener la lista de PLUS a eliminar
         */
    }
}
