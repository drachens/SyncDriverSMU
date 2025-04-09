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
import java.util.*;

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

    public void reloadAndFilterNote1(List<Note> filteredNotes, List<Note1DTO> updateNotes1, String balId){
        logger.debug("Iniciando Reload And Filter Note1");
        logger.debug("filteredNotes:{}, updateNotes:{}",filteredNotes.size(),updateNotes1.size());
        List<Note> notes = new ArrayList<>();
        if(!updateNotes1.isEmpty()){
            try{
                for(Note1DTO note1DTO : updateNotes1){
                    String textoEmbasadoPor = String.join(" ", note1DTO.getEmbalaje().trim(), note1DTO.getNombre().trim());
                    String value = String.join("{$0A}", textoEmbasadoPor, note1DTO.getDireccion().trim(), note1DTO.getResSanitaria().trim());
                    int lfCode = note1DTO.getIdArticle();

                    Note note = new Note();
                    note.setLFCode(lfCode);
                    note.setValue(value);
                    notes.add(note);
                }
                logger.debug("notes1:{}",notes.size());
            }catch(Exception e){
                logger.error(e.getMessage());
            }
        }else{
            logger.debug("Note1DTO vacio.");
            notes = Collections.emptyList();
        }
        logger.debug("filteredNotes:{}",filteredNotes);
        logger.debug("updateNotes:{}",updateNotes1);
        List<Note> newNotes = updateNote1List(filteredNotes,notes);
        writeNote1(newNotes,balId);
    }

    private void writeNote1(List<Note> notes, String balId){
        if(notes.isEmpty()){
            logger.info("No hay Nota1 nuevos para la balanza -> {}",balId);
            return;
        }
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

    private List<Note> updateNote1List(List<Note> original, List<Note> update){
        if(update.isEmpty()){
            return original;
        }
        try{
            Map<Integer, Note> mapNotes = new HashMap<>();
            for(Note note : original){
                mapNotes.put(note.getLFCode(),note);
            }
            for(Note note2 : update){
                mapNotes.put(note2.getLFCode(),note2);
            }
            List<Note> newNote = new ArrayList<>(mapNotes.values());
            logger.debug("newNote.size:{}",newNote.size());
            return newNote;
        }catch(Exception e){
            logger.error("Error al combinar listas original y update de Notes1. {}",e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
