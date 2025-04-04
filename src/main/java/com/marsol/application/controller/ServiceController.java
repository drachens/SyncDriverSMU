package com.marsol.application.controller;

import com.marsol.domain.service.*;
import com.marsol.domain.model.Scale;
import com.marsol.domain.service.DeleteDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Component
public class ServiceController implements CommandLineRunner {
    private final DataExtractionService dataExtractionService;
    private final DeleteDataService deleteDataService;
    private final DataLoadService dataLoadService;
    private final PluTransformationService pluTransformationService;
    private final Note1TransformationService note1TransformationService;

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    public ServiceController(DataExtractionService dataExtractionService,
                             DeleteDataService deleteDataService,
                             DataLoadService dataLoadService,
                             PluTransformationService pluTransformationService,
                             Note1TransformationService note1TransformationService) {
        this.dataExtractionService = dataExtractionService;
        this.deleteDataService = deleteDataService;
        this.dataLoadService = dataLoadService;
        this.pluTransformationService = pluTransformationService;
        this.note1TransformationService = note1TransformationService;
    }

    @Override
    public void run(String... args) throws Exception {
        load();
    }

    public void load() throws FileNotFoundException {

        //Consultar balanzas habilitadas
        logger.info("Consultando balanzas habilitadas...");
        List<Scale> scales = dataExtractionService.getEnabledScales();

        if(scales.isEmpty()){
            logger.error("No hay balanzas habilitadas.");
            return;
        }

        logger.info("Se encontraron {} balanzas habilitadas.",scales.size());

        //Balanzas que requieren borrarse
        /**
         * @String = idBal
         * @Integer = Cantidad de productos a eliminar.
         */
        Map<String,Integer> scalesToClear = deleteDataService.getScalesToClean(scales);
        logger.info("SAS {}",scalesToClear);
        for(Scale scale : scales){
            String balid = String.valueOf(scale.getBalId());
            logger.info("Procesando balanza {}",balid);
            //Si hay balanzas con carga masiva, borrar y cargar todos.
            if(scale.getMasivo() == 1){
                logger.info("Balanza {} solicita carga masiva.",balid);
                deleteDataService.clearScale(scale.getIp());

                //Transformacion
                pluTransformationService.transformDataPlu(scale);
                note1TransformationService.transformData(scale);

                //Cargar Datos
                dataLoadService.loadPlu(scale);
                dataLoadService.loadNote1(scale);
                logger.info("Carga realizada para balanza -> {}",balid);
                break;
            }

            try{
                //Revisar las balanzas que eliminar
                if(scalesToClear.containsKey(balid)){
                    logger.info("Total de PLUs a eliminar para balanza -> {} : {}",balid,scalesToClear.get(balid));
                    String ip = scale.getIp();
                    try{
                        //Eliminar
                        deleteDataService.clearScale(ip);
                        logger.info("PLUs eliminados para balanza -> {}",balid);
                    } catch (Exception e) {
                        logger.error("Error durante el borrado de balanza -> {}",balid);
                    }
                }

                //Transformacion
                logger.warn("Empezando transformacion de PLUS para balanza -> {}", balid);
                pluTransformationService.transformDataPlu(scale);
                logger.warn("Empezando transformacion de notas para balanza -> {}",scale.getBalId());
                note1TransformationService.transformData(scale);

                //Cargar Datos
                logger.warn("Empezando carga de PLU para balanza -> {}",balid);
                dataLoadService.loadPlu(scale);
                logger.warn("Empezando carga de Nota1 para balanza -> {}",balid);
                dataLoadService.loadNote1(scale);
                //logger.info("Carga realizada para balanza -> {}",balid);
            }catch (Exception e){
                logger.error("Error durante la carga de la balanza -> {}",balid);
            }
        }
    }
}
