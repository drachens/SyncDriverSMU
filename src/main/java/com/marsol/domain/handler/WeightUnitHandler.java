package com.marsol.domain.handler;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;

public class WeightUnitHandler extends BaseHandler{
    @Override
    public void handle(PLU plu, ArticleDTO article) {
        int option = article.getMeasure();
        //System.out.println(option);
        switch (option) {
            case 1: //Unitario
                plu.setWeightUnit(8);
                //System.out.print("\npluWeight: "+plu.getWeightUnit()+"\n");
                break;
            case 2: //Fraccion
                plu.setWeightUnit(0);
                break;
            default:
                logger.error("Error al asignar tipo de pesaje para el producto: {}", plu.getLFCode());
        }

        if(nextHandler != null){
            nextHandler.handle(plu, article);
        }
    }
}
