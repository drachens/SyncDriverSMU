package com.marsol.application.controller;

import com.marsol.domain.model.Scale;
import com.marsol.domain.service.DataExtractionService;
import com.marsol.domain.service.SyncManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceController2 implements CommandLineRunner {

    private final SyncManagerService syncManagerService;
    private final static Logger logger = LoggerFactory.getLogger(ServiceController2.class);
    private final DataExtractionService dataExtractionService;

    @Autowired
    public ServiceController2(SyncManagerService syncManagerService, DataExtractionService dataExtractionService) {
        this.syncManagerService = syncManagerService;
        this.dataExtractionService = dataExtractionService;
    }

    @Override
    public void run(String... args) throws Exception {
        load();
    }

    public void load(){
        List<Scale> scales = new ArrayList<>();
        try{
            logger.info("Consultando balanzas habilitadas...");
            scales = dataExtractionService.getEnabledScales();
        } catch (Exception e) {
            logger.error("Error al obtener balanzas.");
            return;
        }


        if(scales.isEmpty()){
            logger.error("No hay balanzas habilitadas.");
            return;
        }

        for(Scale scale : scales){
            try{
                logger.info("Comenzando carga de balanza -> {}",scale.getBalId());
                syncManagerService.sync(scale);
            } catch (Exception e) {
                logger.error("Error al cargar balanza -> {} : {}",scale.getBalId(),e.getMessage());
                break;
            }
        }
        logger.info("Proceso finalizado.");
    }
}
