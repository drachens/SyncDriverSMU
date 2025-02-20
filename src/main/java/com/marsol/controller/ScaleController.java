package com.marsol.controller;

import com.marsol.model.Scale;
import com.marsol.repository.ScaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        scaleRepo.disabledMassive(scale);
    }

    public void setUpdateLastupdateScale(Scale scale, boolean isSucces, String message){
        scaleRepo.setUpdateLastupdateScale(scale, isSucces, message);
    }
}
