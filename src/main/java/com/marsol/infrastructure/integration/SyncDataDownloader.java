package com.marsol.infrastructure.integration;

import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.adapter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SyncDataDownloader {

    @Value("${directory_backup}")
    private String backup;

    private final SyncSDKIntf sync;
    private static final Logger logger = LoggerFactory.getLogger(SyncDataDownloader.class);


    public SyncDataDownloader() {
        this.sync = SyncManager.getInstance();
        this.backup = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto SMU\\backup_test\\";
    }


    public boolean downloadPLU(Scale scale){
        logger.error("Directory backup: {}",backup);
        final boolean[] isSuccessful = {true};
        long result;
        String ipString = scale.getIp();
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> {
            //var1 : ErrorCode
            //var2 : nIndex
            //var3 : nTotal
            //var4 : nUserDataCode
            String errorMessage = ErrorTranslator.getErrorMessage(var1);
            if(var1 != 0 && var1 != 1 && var1 != 2){
                logger.error("[ERROR EN CARGA DE BALANZA] ErrorCode {}: {} en indice: {} de {} elementos.",var1,errorMessage,var2,var3);
                isSuccessful[0] = false;
            }
            if(var1 == 0){
                if(var3 == 0){
                    logger.debug("Se intentó cargar un archivo vacio.");
                    isSuccessful[0] = false;
                }
                logger.info("[CARGA DE BALANZA REALIZADA] Se han cargado todos los elementos ({}).",var3);
            }
            if(var1 == -1){
                logger.error("[ERROR EN CARGA DE BALANZA] Se ha producido un error inesperado durante la carga de la balanaza IP: {}",ipString);
                isSuccessful[0] = false;
            }
        };

        try{
            String filename = String.format("%splu_%s.txt",backup,scale.getBalId());
            logger.info("Descargando información de PLU -> balanza {}",ipString);
            result = sync.SDK_ExecTaskA(ip,1,0,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            return isSuccessful[0];
        }catch(Exception e){
            logger.error("Error al descargar PLU para balanza: {}, error: {}",ipString,e);
            return false;
        }
    }

    public boolean downloadNotes(Scale scale, int noteType){
        if(noteType > 4 || noteType < 0){
            logger.error("Numero de nota {} inválido.",noteType);
            return false;
        }

        final boolean[] isSuccessful = {true};
        long result;
        int noteOperationType;
        String ipString = scale.getIp();
        int ip = SyncSDKDefine.ipToLong(ipString);
        switch(noteType){
            case 1:
                noteOperationType = 5;
                break;
            case 2:
                noteOperationType = 6;
                break;
            case 3:
                noteOperationType = 7;
                break;
            case 4:
                noteOperationType = 8;
                break;
            default:
                logger.error("Número de nota {} inválido.",noteType);
                return false;
        }
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> {
            //var1 : ErrorCode
            //var2 : nIndex
            //var3 : nTotal
            //var4 : nUserDataCode
            String errorMessage = ErrorTranslator.getErrorMessage(var1);
            if(var1 != 0 && var1 != 1 && var1 != 2){
                logger.error("[ERROR EN CARGA DE BALANZA] ErrorCode {}: {} en indice: {} de {} elementos.",var1,errorMessage,var2,var3);
                isSuccessful[0] = false;
            }
            if(var1 == 0){
                if(var3 == 0){
                    logger.debug("Se intentó cargar un archivo vacio.");
                    isSuccessful[0] = false;
                }
                logger.info("[CARGA DE BALANZA REALIZADA] Se han cargado todos los elementos ({}).",var3);
            }
            if(var1 == -1){
                logger.error("[ERROR EN CARGA DE BALANZA] Se ha producido un error inesperado durante la carga de la balanaza IP: {}",ipString);
                isSuccessful[0] = false;
            }
        };

        try{
            String filename = String.format("%snote%s_%s.txt",backup,noteType,scale.getBalId());
            logger.info("Descargando información de Nota{} -> balanza {}",noteType,ipString);
            result = sync.SDK_ExecTaskA(ip,1,noteOperationType,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            return isSuccessful[0];
        }catch(Exception e){
            logger.error("Error al descargar Nota{} para balanza: {}, error: {}",noteType,ipString,e);
            return false;
        }
    }
}
