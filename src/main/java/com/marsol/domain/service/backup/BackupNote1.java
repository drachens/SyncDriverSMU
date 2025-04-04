package com.marsol.domain.service.backup;

import com.marsol.domain.model.Note;
import com.marsol.utils.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BackupNote1 {

    public static List<Note> extractNotes(String filename){
        return FileReaderUtil.readFileAndMap(filename, values -> {
            Note note = new Note();
            note.setLFCode(Integer.parseInt(values[0]));
            note.setValue(values[1]);
            return note;
        });
    }

    public static List<Integer> extractLFCode(String filename){
        return FileReaderUtil.readFileAndMap(filename, values -> Integer.parseInt(values[0]));
    }

}
