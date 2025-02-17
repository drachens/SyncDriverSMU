package com.marsol.transform;

import com.marsol.controller.PluController;
import com.marsol.controller.ServiceController;
import com.marsol.model.Article;
import com.marsol.model.PLU;
import com.marsol.model.Scale;
import com.marsol.utils.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransformPlu {

    private PluController m_PluController;
    @Value("${directory.pendings}")
    private static String directoryPendings;

    private static final Logger logger = LoggerFactory.getLogger(TransformPlu.class);


    @Autowired
    public TransformPlu(PluController m_PluController) {
        this.m_PluController = m_PluController;
    }

    public List<PLU> getPluTransformed(Scale scale){
        List<Article> articles = m_PluController.getPluArticleInfo(scale);
        List<PLU> plus = new ArrayList<PLU>();
        int idBal = scale.getBalId();
        for(Article article : articles){
            PLU plu = new PLU();
            String pluFill = getFillPLU(article.getId());

            plu.setLFCode(Integer.parseInt(article.getId()));
            plu.setName1(article.getDescription());
            plu.setUnitPrice(article.getPrice());

            switch (article.getMeasure()){
                case 1: //Si es 1 es por unitario
                    plu.setWeightUnit(8);
                    plu.setBarcodeType1(101);
                    String itemCode = "27"+pluFill;
                    System.out.println(itemCode);
                    plu.setItemCode(itemCode);
                    break;
                case 2: // Si es 2 es por fraccion
                    plu.setWeightUnit(0);
                    plu.setBarcodeType1(100);
                    String itemCode2 = "25"+pluFill;
                    System.out.println(itemCode2);
                    plu.setItemCode(itemCode2);
                    break;
                default:
                    System.out.println("Error");
            }
            plus.add(plu);
        }
        logger.info("Total de PLUs a enviar balanza {}: {}",idBal,plus.size());
        return plus;
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

    public void writeListPlu(Scale scale, String name){
        List<PLU> plus = getPluTransformed(scale);
        System.out.println(directoryPendings);
        System.out.println(scale.getIp());
        String filename = directoryPendings + name;
        //String filename = String.format("%splu_%s.txt",directoryPendings,scale.getIp());


        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
            writer.write(String.join("\t", HeadersFilesHPRT.PLUHeader));
            writer.newLine();
            try{
                for(PLU plu : plus){
                    writer.write(plu.toString());
                    writer.newLine();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
