package com.marsol.domain.handler;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;

public class DatesHandler extends BaseHandler {
    @Override
    public void handle(PLU plu, ArticleDTO articleDTO) {
        int option = articleDTO.getImprimir();
        switch (option) {
            case 0:
                plu.setProducedDateF(0);
                plu.setValidDateF(1);
                break;
            case 2:
                plu.setProducedDateF(0);
                plu.setValidDateF(3);
                break;
            case 4:
                plu.setProducedDateF(3);
                plu.setValidDateF(0);
                break;
            case 7:
                plu.setProducedDateF(3);
                plu.setValidDateF(3);
                break;
            default:
                logger.error("No se pudo asignar la impresión de fechas para el producto: {}, con opcion: {}",plu.getLFCode(),option);
        }

        if(nextHandler!=null){
            nextHandler.handle(plu, articleDTO);
        }
    }
}
