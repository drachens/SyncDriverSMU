import com.marsol.Main;
import com.marsol.config.DatabaseConfig;
import com.marsol.controller.PluController;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.Article;
import com.marsol.model.Scale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = Main.class)
@Import(DatabaseConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
public class PluControllerTest {

    @Autowired
    DataSource ds;

    @Test
    public void test(){

        DatabaseService db = new DatabaseService(ds);
        PluController pc = new PluController(db);

        Scale newScale = new Scale();
        newScale.setBalId(33);

        List<String> sas = pc.getPluToLoad(newScale);

        List<Article> sas_2 = db.getPluItems(sas);

        for(Article article : sas_2){
            System.out.println("Nuevo Articulo:");
            for (Field field : article.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Permite acceder a atributos privados
                try {
                    String name = field.getName(); // Nombre del atributo
                    Object value = field.get(article); // Valor del atributo
                    System.out.println(name + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-------------------------");
        }
    }

}
