import com.marsol.Main;
import com.marsol.config.DatabaseConfig;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = Main.class)
@Import(DatabaseConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
public class DatabaseQueryTest {

    @Autowired
    DataSource ds;


    @Test
    public void test() throws SQLException {
        DatabaseService db = new DatabaseService(ds);
        List<Map<String,Object>> test = db.getEnabledScales();

        for(Map<String,Object> map : test){
            System.out.println("Nuevo Articulo:");
            for(Map.Entry<String,Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("-----------------------------------------");
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
        List<Article> test = db.getPluItems(ids);

        for(Article article : test){
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
