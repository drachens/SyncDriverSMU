import com.marsol.config.DatabaseConfig;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.ArticleDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@SpringBootTest(classes = Main.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfig.class)
@Import({DatabaseConfig.class})
@TestPropertySource(locations = "classpath:application.properties")
public class DatabaseQueryTest {

    @Autowired
    private DataSource ds;

    @Test
    public void test() throws SQLException {

        DatabaseService db = new DatabaseService(ds);
        List<Map<String,Object>> test = db.getEnabledScales();
        for(Map<String,Object> map : test){
            for(String i : map.keySet()){
                System.out.println(i+" : "+map.get(i));
            }
            System.out.println("------------------");
        }
    }


    public void getAllArticlesTest(){
        DatabaseService db = new DatabaseService(ds);
        List<Map<String,Object>> test = db.getAllArticles(21);
        for(Map<String,Object> map : test){
            System.out.println("Nuevo Articulo:");
            for(Map.Entry<String,Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }


    public void test_2() throws SQLException {
        DatabaseService db = new DatabaseService(ds);
        List<Map<String,Object>> test = db.getEnabledArticles(33);

        for(Map<String,Object> map : test){
            System.out.println("Nuevo Articulo:");
            for(Map.Entry<String,Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void testGetPluItems(){
        DatabaseService db = new DatabaseService(ds);
        List<String> ids = new ArrayList<>();
        ids.add("97676");
        ids.add("97678");
        ids.add("97679");
        ids.add("97682");
        List<ArticleDTO> test = null;//db.getPluItems(ids);

        for(ArticleDTO articleDTO : test){
            System.out.println("Nuevo Articulo:");
            for (Field field : articleDTO.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Permite acceder a atributos privados
                try {
                    String name = field.getName(); // Nombre del atributo
                    Object value = field.get(articleDTO); // Valor del atributo
                    System.out.println(name + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-------------------------");
        }
    }


    public void testGetAllStores(){
        DatabaseService db = new DatabaseService(ds);
        List<Map<String,Object>> test = db.getAllStores();

        for(Map<String,Object> map : test){
            System.out.println("Nuevo Tienda:");
            for(Map.Entry<String,Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }


}
