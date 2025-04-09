package com.marsol.domain.service;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.application.dto.Note1DTO;
import com.marsol.domain.model.Note;
import com.marsol.domain.model.PLU;
import com.marsol.domain.model.Scale;
import com.marsol.domain.repository.PLURepository;
import com.marsol.infrastructure.repository.ScaleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncManagerService {

    private final static Logger logger = LoggerFactory.getLogger(SyncManagerService.class);

    private final PLURepository pluRepository;
    private final DeleteDataService deleteDataService;
    private final DataExtractionService dataExtractionService;
    private final PluTransformationService pluTransformationService;
    private final Note1TransformationService note1TransformationService;
    private final DataLoadService dataLoadService;
    private final ScaleDAO scaleDAO;

    @Autowired
    public SyncManagerService(PLURepository pluRepository,
                              DeleteDataService deleteDataService,
                              DataExtractionService dataExtractionService,
                              PluTransformationService pluTransformationService,
                              Note1TransformationService note1TransformationService,
                              DataLoadService dataLoadService,
                              ScaleDAO scaleDAO) {
        this.pluRepository = pluRepository;
        this.deleteDataService = deleteDataService;
        this.dataExtractionService = dataExtractionService;
        this.pluTransformationService = pluTransformationService;
        this.note1TransformationService = note1TransformationService;
        this.dataLoadService = dataLoadService;
        this.scaleDAO = scaleDAO;
    }

    public void sync(Scale scale){
        String balId = String.valueOf(scale.getBalId());
        List<String> disabledArticles;
        List<PLU> currentPLU;
        List<Note> currentNote1 = List.of();
        List<ArticleDTO> updateArticles;
        List<Note1DTO> updateNote1 = List.of();
        List<PLU> filteredPLU;
        List<Note> filteredNote1;
        //Obtener articulos desactivados
        try{
            disabledArticles = pluRepository.getDisabledArticles2(scale);
            if(disabledArticles.isEmpty()){
                logger.info("No hay productos desactivados para balanza -> {}",balId);
            }
        }catch (Exception e){
            logger.error("Error al obtener articulos desactivados");
            throw new RuntimeException(e);
        }
        //Obtener lista de articulos cargados en la balanza
        try{
            logger.debug("Obteniendo lista de articulos cargados en la balanza");
            currentPLU = deleteDataService.getActualPLU(scale);
            if(!currentPLU.isEmpty()){
                logger.debug("Existen {} articulos cargados en la balanza.",currentPLU.size());
            }
        } catch (Exception e) {
            logger.error("Error al obtener articulos cargados en la balanza ");
            throw new RuntimeException(e);
        }
        //Obtener lista de notas cargados en la balanza
        try{
            logger.debug("Obteniendo lista de Notas1 cargados en la balanza");
            currentNote1 = deleteDataService.getActualNote1(scale);
            if(!currentNote1.isEmpty()){
                logger.debug("Existen {} notas 1 cargadas en la balanza.",currentNote1.size());
            }
        }catch (Exception e){
            logger.error("Error al obtener notas1 cargadas en la balanza.");
        }
        //Obtener lista de articulos que requieren actualizacion
        try{
            logger.debug("Obteniendo lista de articulos a actualizar.");
            updateArticles = dataExtractionService.getEnabledArticles(scale);
            if(!updateArticles.isEmpty()){
                logger.debug("Existen {} articulos a actualizar.",updateArticles.size());
            }
        } catch (Exception e) {
            logger.error("Error al obtener lista de articulos habilitados para actualizar");
            throw new RuntimeException(e);
        }
        //Obtener lista de notas 1 que requieren actualizacion
        try{
            logger.debug("Obteniendo lista de notas1 a actualizar.");
            List<String> ids = dataExtractionService.getListOfEnabledArticles(scale);
            if(!ids.isEmpty()){
                updateNote1 = dataExtractionService.getStoreInfo(ids);
                if(!updateNote1.isEmpty()){
                    logger.debug("Existen {} notas1 a actualizar",updateNote1.size());
                }
            }
        }catch (Exception e){
            logger.error("Error al obtener lista de notas1 a actualizar");
            throw new RuntimeException(e);
        }
        //Obtener lista de plus filtrados (eliminando los desactivados)
        try{
            logger.debug("Obteniendo lista de PLU filtrados");
            filteredPLU = currentPLU.stream()
                        .filter(plu -> !disabledArticles.contains(String.valueOf(plu.getLFCode())))
                        .toList();
            filteredNote1 = currentNote1.stream()
                            .filter(note -> !disabledArticles.contains(String.valueOf(note.getLFCode())))
                            .toList();
            logger.debug("La cantidad de articulos final a cargar será de {}",filteredPLU.size());
            logger.debug("La cantidad de notas final a cargar será de {}",filteredNote1.size());
        } catch (Exception e) {
            logger.error("Error al filtrar los plus desactivados de la balanza.");
            throw new RuntimeException(e);
        }


        //Carga masiva
        if(scale.getMasivo() == 1){
            try{
                logger.info("Preparando carga masiva para balanza -> {}",balId);
                deleteDataService.clearScale(scale.getIp());
                pluTransformationService.transformDataPlu(scale);
                note1TransformationService.transformData(scale);
                dataLoadService.loadPlu(scale);
                dataLoadService.loadNote1(scale);
                logger.info("Carga masiva realizada para balanza -> {}",balId);
            } catch (FileNotFoundException e) {
                logger.error("Error durante carga masiva.");
                throw new RuntimeException(e);
            }

        }else{ //Si no es carga masiva.
            //Si existen articulos eliminados en BD
            if(currentPLU.size() > filteredPLU.size()){
                int aEliminated = currentPLU.size() - filteredPLU.size();
                try{
                    logger.info("Se van a eliminar {} articulos de la balanza -> {}",aEliminated,balId);
                    deleteDataService.clearScale(scale.getIp());
                    pluTransformationService.reloadAndFilterDataPlu(filteredPLU,updateArticles,balId);
                    note1TransformationService.reloadAndFilterNote1(filteredNote1,updateNote1,balId);
                    //note1TransformationService.transformData(scale);
                    dataLoadService.loadPlu(scale);
                    dataLoadService.loadNote1(scale);
                } catch (FileNotFoundException e) {
                    logger.error("Error al intentar escribir plu.txt para balanza -> {}", balId);
                    throw new RuntimeException(e);
                }
            }else if(currentPLU.size() == filteredPLU.size()){ //No hay que eliminar nada
                if(!updateArticles.isEmpty()){ //Hay articulos que actualizar
                    try{
                        logger.info("Se actualizaran {} articulos de la balanza -> {}",updateArticles.size(),balId);
                        pluTransformationService.reloadAndFilterDataPlu(currentPLU,updateArticles,balId);
                        note1TransformationService.reloadAndFilterNote1(filteredNote1,updateNote1,balId);
                        //note1TransformationService.transformData(scale);
                        dataLoadService.loadPlu(scale);
                        dataLoadService.loadNote1(scale);
                    }catch (FileNotFoundException e){
                        logger.error("No se han podido actualizar los articulos de la balanza -> {}", balId);
                        throw new RuntimeException(e);
                    }
                }else{
                    logger.info("No existen articulos que actualizar para la balanza -> {}", balId);
                    //No hacer ninguna carga.
                    scaleDAO.setUpdateLastupdateScale(scale,true,"ARTICLES");
                    scaleDAO.setUpdateLastupdateScale(scale,true,"ZMATERIALES");
                    scaleDAO.setUpdateLastupdateScale(scale,true,"ZMATEBALANZA");
                    scaleDAO.setUpdateLastupdateScale(scale,true,"ZMAETIENDA");
                }
            }
        }
    }
}
