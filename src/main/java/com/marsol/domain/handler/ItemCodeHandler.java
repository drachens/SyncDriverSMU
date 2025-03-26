package com.marsol.domain.handler;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;

public class ItemCodeHandler extends BaseHandler{
    @Override
    public void handle(PLU plu, ArticleDTO article) {
        int option = plu.getWeightUnit();
        String pluFill = getFillPLU(String.valueOf(plu.getLFCode()));
        String itemCode;
        switch (option) {
            case 0:
                itemCode = "25"+pluFill;
                plu.setItemCode(itemCode);
                break;
            case 8:
                itemCode = "27"+pluFill;
                plu.setItemCode(itemCode);
                break;
            default:
                logger.error("Error al asignar el codigo del producto {}",plu.getLFCode());
        }
        if(nextHandler != null){
            nextHandler.handle(plu, article);
        }
    }

    private String getFillPLU(String id){
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        if (id.length() > 5) {
            throw new IllegalArgumentException("El ID no puede tener más de 5 caracteres");
        }

        // Rellenar con ceros a la izquierda hasta completar 5 caracteres
        return String.format("%05d", Integer.parseInt(id));
    }
}
