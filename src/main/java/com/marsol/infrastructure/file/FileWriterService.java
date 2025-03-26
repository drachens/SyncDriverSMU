package com.marsol.infrastructure.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriterService {

    @Value("${directory.pendings}")
    private String pendings;

    private static final Logger logger = LoggerFactory.getLogger(FileWriterService.class);

    public void writeToFile(String fileName, String content) {
        Path dirPath = Paths.get(pendings);
        if(!Files.exists(dirPath)) {
            try{
                Files.createDirectories(dirPath);
            }catch(IOException e){
                logger.error("Error creando {} : {}",dirPath,e.getMessage());
            }
            Path filePath = dirPath.resolve(fileName);
            try{
                Files.writeString(filePath, content);
            }catch(IOException e){
                logger.error("Error escribiendo contenido en {} : {}",filePath,e.getMessage());
            }

        }
    }


}
