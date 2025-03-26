package com.marsol.domain.service;

import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.integration.SyncDataLoader;
import com.marsol.infrastructure.repository.ScaleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataLoadService {
    private static final Logger logger = LoggerFactory.getLogger(DataLoadService.class);
    private final SyncDataLoader syncDataLoader;
    private final ScaleDAO scaleDAO;

    @Autowired
    public DataLoadService(ScaleDAO scaleDAO) {
        this.syncDataLoader = new SyncDataLoader();
        this.scaleDAO = scaleDAO;
    }

    @Value("${directory.pendings}")
    private String pendings;


    public void loadPlu(Scale scale){
        String idBal = String.valueOf(scale.getBalId());
        String ip = scale.getIp();
        String filename = String.format("%splu_%s.txt", pendings, idBal);

        boolean boolPlu = syncDataLoader.loadPLU(filename, ip);
        /**
         * si los items cargados son 0 entonces no se debería retornar true en boolPlu
         * Se debe comportar dependiendo si es carga masiva o no. Si es carga masiva, entonces
         * no se debería desactivar la carga masiva.
         */
        if(boolPlu){
            scaleDAO.disabledMassive(scale);
            scaleDAO.setUpdateLastupdateScale(scale,true,"ARTICLES");
            scaleDAO.setUpdateLastupdateScale(scale,true,"ZMATERIALES");
            scaleDAO.setUpdateLastupdateScale(scale,true,"ZMATEBALANZA");
        }else{
            logger.error("Error durante la carga de PLU");
            scaleDAO.setUpdateLastupdateScale(scale,false,"Error carga PLUs");
        }
    }

    public void loadNote1(Scale scale){
        String idBal = String.valueOf(scale.getBalId());
        String ip = scale.getIp();
        String filename = String.format("%snote1_%s.txt", pendings, idBal);

        boolean boolNote = syncDataLoader.loadNotes(filename,ip,1);
        if(boolNote){
            scaleDAO.disabledMassive(scale);
            scaleDAO.setUpdateLastupdateScale(scale,true,"ZMAETIENDA");
        }else{
            logger.error("Error durante la carga de NOTE1");
            scaleDAO.setUpdateLastupdateScale(scale,false,"Error carga NOTE1");
        }
    }
}
