import com.marsol.Main;
import com.marsol.config.DatabaseConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(classes = Main.class)
@Import(DatabaseConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
public class DatabaseConfigTest {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Test
    public void loadPropertiesTest(){
        databaseConfig.test();
    }
}
