package com.marsol.transform;

import com.marsol.controller.ResSanitariaController;
import com.marsol.model.Note;
import com.marsol.model.PLU;
import com.marsol.model.ResSanitaria;
import com.marsol.model.Scale;
import com.marsol.utils.HeadersFilesHPRT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransformNote1 {
    private ResSanitariaController rsController;

    @Value("${directory.pendings}")
    private static String directoryPendings;

    @Autowired
    public TransformNote1(ResSanitariaController rsController) {
        this.rsController = rsController;
    }

    public List<Note> getNote1Transformed(Scale scale){
        List<Note> notes = new ArrayList<Note>();
        List<ResSanitaria> resSanitaria = rsController.getResSanitaria(scale);

        for(ResSanitaria res : resSanitaria){
            Note note = new Note();
            note.setLFCode(Integer.parseInt(res.getId()));
            note.setValue(res.getNote1Simple());
            notes.add(note);
        }
        return notes;
    }

    public void writeListNote1(Scale scale, String name){
        List<Note> notes = getNote1Transformed(scale);
        System.out.println(directoryPendings);
        System.out.println(scale.getIp());
        String filename = directoryPendings + name;
        //String filename = String.format("%splu_%s.txt",directoryPendings,scale.getIp());


        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
            writer.write(String.join("\t", HeadersFilesHPRT.NoteHeader));
            writer.newLine();
            try{
                for(Note note : notes){
                    writer.write(note.toString());
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
