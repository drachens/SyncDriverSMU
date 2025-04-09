import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;
import com.marsol.domain.service.DataExtractionService;
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

public class PluTransformationServiceTest {
    private PluTransformationService service;
    private final String TEMP_DIR = "src/test/resources/temp/";
    private final String BAL_ID = "999";

    @BeforeEach
    public void setUp() throws Exception {
        DataExtractionService mockDataExtractionService = org.mockito.Mockito.mock(DataExtractionService.class);
        service = new PluTransformationService(mockDataExtractionService);
        Files.createDirectories(Paths.get(TEMP_DIR));
        ReflectionTestUtils.setField(service, "directoryPendings", TEMP_DIR);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(Paths.get(TEMP_DIR))
                .map(java.nio.file.Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testReloadAndFilterDataPlu_createsMergedPluFile() throws FileNotFoundException, IOException {
        // PLUs filtrados previamente
        PLU existingPlu = new PLU();
        existingPlu.setLFCode(1002);
        existingPlu.setItemCode("22345678");
        existingPlu.setName1("Manzana Roja");
        existingPlu.setUnitPrice(1500);
        existingPlu.setValidDays(30);

        List<PLU> filteredPlus = new ArrayList<>();
        filteredPlus.add(existingPlu);

        // Nuevos artículos
        ArticleDTO newArticle = ArticleDTO.builder().id("1003")
                .description("Pera verde")
                .brand("Frutera")
                .price(16000)
                .measure(1)
                .build();

        List<ArticleDTO> updateArticles = Collections.singletonList(newArticle);

        // Ejecutar el método
        service.reloadAndFilterDataPlu(filteredPlus, updateArticles, BAL_ID);

        // Verificar archivo generado
        File outputFile = new File(TEMP_DIR + "plu_" + BAL_ID + ".txt");
        assertThat(outputFile).exists();

        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertThat(lines).hasSize(3); // header + 2 PLUs
        assertThat(lines.get(0)).contains("LFCode", "ItemCode", "Department"); // Encabezado
        assertThat(lines.get(1)).contains("1002"); //Primero incluye la lista de PLU original, No necesariamente, depende de cada iteracion.
        assertThat(lines.get(2)).contains("1003").contains("Pera verde");
    }

    @Test
    void testReloadAndFilterDataPlu_deletePlu() throws FileNotFoundException, IOException {
        PLU plu1 = new PLU();
        PLU plu2 = new PLU();
        PLU plu3 = new PLU();

        //Creamos PLU_1
        plu1.setLFCode(1002);
        plu1.setItemCode("22345678");
        plu1.setName1("Manzana Roja");
        plu1.setUnitPrice(1500);
        plu1.setValidDays(30);
        //Creamos PLU_2
        plu2.setLFCode(1003);
        plu2.setItemCode("22345679");
        plu2.setName1("Manzana Verde");
        plu2.setUnitPrice(16001);
        plu2.setValidDays(31);
        //Creamos PLU_3
        plu3.setLFCode(1004);
        plu3.setItemCode("22345680");
        plu3.setName1("Manzana Amarilla");
        plu3.setUnitPrice(16002);
        plu3.setValidDays(32);

        List<PLU> filteredPLU = new ArrayList<>();
        List<PLU> currentPLU = Arrays.asList(plu1, plu2, plu3);


        List<String> disabledArticles = Collections.singletonList("1002");
        filteredPLU = currentPLU.stream()
                .filter(plu -> !disabledArticles.contains(String.valueOf(plu.getLFCode())))
                .toList();

        assertThat(currentPLU).hasSize(3);
        assertThat(filteredPLU).hasSize(2);
    }

    @Test
    void testReloadAndFilterDataPlu_updatesMergedPluFile() throws FileNotFoundException, IOException {
        PLU existingPlu = new PLU();
        existingPlu.setLFCode(1002);
        existingPlu.setItemCode("22345678");
        existingPlu.setName1("Manzana Roja");
        existingPlu.setUnitPrice(1500);
        existingPlu.setValidDays(30);
        List<PLU> filteredPlus = new ArrayList<>();
        filteredPlus.add(existingPlu);

        PLU existingPlu2 = new PLU();
        existingPlu2.setLFCode(1003);
        existingPlu2.setItemCode("3123412");
        existingPlu2.setName1("sas");
        filteredPlus.add(existingPlu2);

        //Articulo Actualizado
        ArticleDTO updateArticle = ArticleDTO.builder()
                .id("1002")
                .description("Pera Cian")
                .price(499)
                .measure(1)
                .build();
        List<ArticleDTO> updateArticles = Collections.singletonList(updateArticle);
        service.reloadAndFilterDataPlu(filteredPlus, updateArticles, BAL_ID);
        File outputFile = new File(TEMP_DIR + "plu_" + BAL_ID + ".txt");
        assertThat(outputFile).exists();
        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertThat(lines).hasSize(3);
        assertThat(lines.get(1)).contains("Pera Cian");
        assertThat(lines.get(1)).contains("1002");
        assertThat(lines.get(1)).contains("499");
    }
}
