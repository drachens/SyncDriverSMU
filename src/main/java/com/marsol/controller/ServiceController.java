package com.marsol.controller;

import com.marsol.load.SyncDataLoader;
import com.marsol.load.SyncManager;
import com.marsol.model.Scale;
import com.marsol.transform.TransformNote1;
import com.marsol.transform.TransformPlu;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceController implements CommandLineRunner {

    private TransformPlu transformPlu;
    private TransformNote1 transformNote1;
    private final PluController pluController;
    private final ResSanitariaController resSanitariaController;
    private final ScaleController scaleController;
    private SyncDataLoader syncDataLoader;

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);


    @Value("${directory.pendings}")
    private static String directoryPendings;

    private List<Scale> scales;
    @Autowired
    public ServiceController(PluController pluController,
                             ResSanitariaController resSanitariaController,
                             ScaleController scaleController) {
        this.pluController = pluController;
        this.resSanitariaController = resSanitariaController;
        this.scaleController = scaleController;
    }

    @PostConstruct
    public void init() {
        this.scales = scaleController.getEnabledScales();
        this.transformPlu = new TransformPlu(pluController);
        this.transformNote1 = new TransformNote1(resSanitariaController);
        this.syncDataLoader = new SyncDataLoader();
    }

    @Override
    public void run(String... args) throws Exception {
        load();
    }

    public void load(){
        for(Scale scale : scales){
            scale.setIp("10.42.195.25");
            System.out.println("Scale: ");
            System.out.println(scale.getBalId());
            System.out.println(scale.getIp());
            System.out.println(scale.getUpdated());
            System.out.println("-------------------------------");
            //Primero hay que eliminar los articulos
            //Desactualizados o deshabilitados

            String ip = scale.getIp();
            String idBal = String.valueOf(scale.getBalId());

            logger.info("Procesando balanza {}",idBal);

            String nameFilePLU = idBal+"_PLU.txt";
            String nameFileNOTE1 = idBal+"_NOTE1.txt";
            String nameFileNOTE2 = idBal+"_NOTE2.txt";
            String nameFileNOTE3 = idBal+"_NOTE3.txt";
            String nameFileNOTE4 = idBal+"_NOTE4.txt";

            try{
                transformPlu.writeListPlu(scale, nameFilePLU);
                logger.info("Archivo de PLU generado para balanza {}",idBal);
            }catch(Exception e){
                logger.error("Error al generar archivo de PLU para balanza {}",idBal);
            }

            try{
                transformNote1.writeListNote1(scale, nameFileNOTE1);
                logger.info("Archivo de NOTE1 generado para balanza {}",idBal);
            }catch(Exception e){
                logger.error("Error al generar archivo de NOTE1 para balanza {}",idBal);
            }

            logger.info("Inicia envio de PLU a balanza {}",idBal);
            boolean boolPLU = syncDataLoader.loadPLU(directoryPendings+nameFilePLU, ip);

            logger.info("Inicia envio de NOTE1 a balanza {}",idBal);
            boolean boolNOTE1 = syncDataLoader.loadNotes(directoryPendings+nameFileNOTE1,ip,1);

            if(boolPLU){
                logger.info("Carga de PLU realizada en balanza {}",idBal);
            }
            else{
                logger.error("Error al cargar archivo de PLU en balanza {}", idBal);
            }
            if(boolNOTE1){
                logger.info("Carga de NOTE1 realizada en balanza {}",idBal);
            }
            else{
                logger.error("Error al cargar archivo de NOTE1 en balanza {}", idBal);
            }


        }
    }
}
