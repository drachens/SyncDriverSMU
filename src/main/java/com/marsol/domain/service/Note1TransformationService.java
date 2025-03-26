package com.marsol.domain.service;

import com.marsol.application.dto.Note1DTO;
import com.marsol.domain.model.Note;
import com.marsol.domain.model.Scale;
import com.marsol.utils.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class Note1TransformationService {
    private final static Logger logger = LoggerFactory.getLogger(Note1TransformationService.class);

    private final DataExtractionService dataExtractionService;

    @Value("${directory.pendings}")
    private String directoryPendings;

    @Autowired
    public Note1TransformationService(DataExtractionService dataExtractionService) {
        this.dataExtractionService = dataExtractionService;
    }

    public void transformData(Scale scale){
        int idBal = scale.getBalId();
        List<Note> notes = new ArrayList<>();
        List<String> ids;
        if(scale.getMasivo() == 1){
            ids = dataExtractionService.getListOfEnabledArticlesMassive(scale);
        }else{
            ids = dataExtractionService.getListOfEnabledArticles(scale);
        }
        //logger.info("{}",ids);
        List<Note1DTO> note1DTOS = dataExtractionService.getStoreInfo(ids);
        for(Note1DTO note1DTO : note1DTOS){
            String textoEmbasadoPor = String.join(" ", note1DTO.getEmbalaje().trim(), note1DTO.getNombre().trim());
            String value = String.join("{$0A}", textoEmbasadoPor, note1DTO.getDireccion().trim(), note1DTO.getResSanitaria().trim());
            int lfCode = note1DTO.getIdArticle();

            Note note = new Note();
            note.setLFCode(lfCode);
            note.setValue(value);
            notes.add(note);
        }
        logger.info("Total de Note1 a enviar a balanza {} : {}",idBal,notes.size());
        writeNote1(notes,String.valueOf(idBal));
    }

    private void writeNote1(List<Note> notes, String balId){
        logger.debug("Ejecutando escritura de Nota1 ({}) balanza: {}", notes.size(), balId);
        String filename = String.format("%snote1_%s.txt", directoryPendings, balId);

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            writer.write(String.join("\t", HeadersFilesHPRT.NoteHeader));
            writer.newLine();

            for(Note note : notes){
                writer.write(note.toString());
                writer.newLine();
            }
            logger.debug("Archivo escrito en: {}", filename);
        }catch (IOException e) {
            logger.error("Error durante escritura de note1.txt para balanza -> {}",balId);
        }
    }


}
