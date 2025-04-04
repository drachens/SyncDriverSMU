import com.marsol.application.dto.ArticleDTO;
import com.marsol.application.dto.Note1DTO;
import com.marsol.domain.model.Note;
import com.marsol.domain.model.PLU;
import com.marsol.domain.service.DataExtractionService;
import com.marsol.domain.service.Note1TransformationService;
import com.marsol.domain.service.PluTransformationService;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class Note1TransformationServiceTest {
    private Note1TransformationService service;
    private final String TEMP_DIR = "src/test/resources/temp/";
    private final String BAL_ID = "998";

    @BeforeEach
    public void setUp() throws Exception {
        DataExtractionService mockDataExtractionService = org.mockito.Mockito.mock(DataExtractionService.class);
        service = new Note1TransformationService(mockDataExtractionService);
        Files.createDirectories(Paths.get(TEMP_DIR));
        ReflectionTestUtils.setField(service, "directoryPendings", TEMP_DIR);
    }

    @AfterEach
    public void tearDown() throws Exception {
        Files.walk(Paths.get(TEMP_DIR))
                .map(java.nio.file.Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testReloadAndFilterNote1_createsMergedPluFile() throws FileNotFoundException, IOException {
        //Notes filtrados previamente
        Note existingNote = new Note();
        existingNote.setLFCode(1001);
        existingNote.setValue("Los pollitos dicen pio pio pio");

        List<Note> filteredNotes = new ArrayList<>();
        filteredNotes.add(existingNote);

        //nuevas notas
        Note1DTO note1 = new Note1DTO();
        note1.setIdArticle(1002);
        note1.setEmbalaje("los");
        note1.setNombre("nombre");
        note1.setDireccion("direccion");
        note1.setResSanitaria("resSanitaria");

        List<Note1DTO> updateNote = Collections.singletonList(note1);

        service.reloadAndFilterNote1(filteredNotes,updateNote, BAL_ID);
        File outputFile = new File(TEMP_DIR + "note1_" + BAL_ID + ".txt");
        assertThat(outputFile).exists();

        List<String> lines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        assertThat(lines).hasSize(3);
        assertThat(lines.get(0)).contains("LFCode","Value");
        assertThat(lines.get(1)).contains("1001");
        assertThat(lines.get(2)).contains("1002").contains("resSanitaria");
    }

    @Test
    void testReloadAndFilterNote1_updatesMergedNote1() throws FileNotFoundException, IOException {
        Note existingNote = new Note();
        existingNote.setLFCode(1001);
        existingNote.setValue("Los pollitos dicen pio pio pio");
        List<Note> filteredNotes = new ArrayList<>();
        filteredNotes.add(existingNote);

        //Nota actualizada
        Note1DTO note1 = new Note1DTO();
        note1.setIdArticle(1001);
        note1.setEmbalaje("los");
        note1.setNombre("nombre");
        note1.setDireccion("direccion");
        note1.setResSanitaria("resSanitaria");
        List<Note1DTO> updateNote = Collections.singletonList(note1);
        service.reloadAndFilterNote1(filteredNotes,updateNote, BAL_ID);
        File outputFile = new File(TEMP_DIR + "note1_" + BAL_ID + ".txt");
        assertThat(outputFile).exists();
        List<String> lines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        assertThat(lines).hasSize(2);
        assertThat(lines.get(1)).contains("1001").contains("resSanitaria");
    }
}
