package com.marsol.domain.handler;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;

public class BarcodeHandler extends BaseHandler{
    @Override
    public void handle(PLU plu, ArticleDTO article) {
        int option = plu.getWeightUnit();
        //System.out.println("s");
        switch (option) {
            case 0:
                plu.setBarcodeType1(100);
                break;
            case 8:
                plu.setBarcodeType1(101);
                break;
            default:
                logger.error("Error al asignar el codigo de barras para producto: {}",plu.getLFCode());
        }
        if(nextHandler != null){
            nextHandler.handle(plu, article);
        }
    }
}
