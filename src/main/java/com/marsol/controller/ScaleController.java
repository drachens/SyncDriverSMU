package com.marsol.controller;

import com.marsol.config.DatabaseConfig;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.Scale;
import com.marsol.repository.ScaleRepository;
import com.marsol.repository.impl.Scale.DBScaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ScaleController {
    private final ScaleRepository scaleRepo;
    private static final Logger logger = LoggerFactory.getLogger(ScaleController.class);

    @Autowired
    public ScaleController(ScaleRepository scaleRepo) {
        this.scaleRepo = scaleRepo;
    }

    public List<Scale> getEnabledScales(){
        return scaleRepo.findEnabledScales();
        }

    public void updateScaleCargaMaestra(Scale scale){
        scaleRepo.setUpdateCargaMaestra(scale);
    }

    public void setUpdateLastupdateScale(Scale scale, boolean isSucces, String message){
        scaleRepo.setUpdateLastupdateScale(scale, isSucces, message);
    }
}
