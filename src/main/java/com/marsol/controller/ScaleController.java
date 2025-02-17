package com.marsol.controller;

import com.marsol.config.DatabaseConfig;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ScaleController {
    private final DatabaseService databaseService;
    private static final Logger logger = LoggerFactory.getLogger(ScaleController.class);

    @Autowired
    public ScaleController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<Scale> getEnabledScales(){
        List<Scale> result = new ArrayList<>();
        List<Map<String,Object>> scales = databaseService.getEnabledScales();

        logger.info("Conectado a base de datos Informix.");

        for(Map<String,Object> scale : scales){
            Scale newScale = new Scale();
            if(scale.containsKey("nombre")){
                newScale.setNombre((String) scale.get("nombre"));
            }
            if(scale.containsKey("updated")){
                newScale.setUpdated((Timestamp) scale.get("updated"));
            }
            if(scale.containsKey("etiqueta_u")){
                newScale.setEtiqueta_u((int) scale.get("etiqueta_u"));
            }
            if(scale.containsKey("etiqueta_p")){
                newScale.setEtiqueta_p((int) scale.get("etiqueta_p"));
            }
            if(scale.containsKey("letraplu")){
                newScale.setLetraPlu((int) scale.get("letraplu"));
            }
            if(scale.containsKey("letraing")){
                newScale.setLetraIng((int) scale.get("letraing"));
            }
            if(scale.containsKey("ip")){
                newScale.setIp((String) scale.get("ip"));
            }
            if(scale.containsKey("puerto")){
                newScale.setPuerto((int) scale.get("puerto"));
            }
            if(scale.containsKey("masivo")){
                newScale.setMasivo((int) scale.get("masivo"));
            }
            if(scale.containsKey("estado")){
                newScale.setEstado((int) scale.get("estado"));
            }
            if(scale.containsKey("balid")){
                newScale.setBalId((int) scale.get("balid"));
            }
            result.add(newScale);
        }

        logger.info("Se encontraron {} balanzas habilitadas.", result.size());
        return result;
    }
}
