/*
import com.marsol.Main;
import com.marsol.config.DatabaseConfig;
import com.marsol.domain.service.PluController;
import com.marsol.extraction.DatabaseService;
import com.marsol.domain.model.PLU;
import com.marsol.domain.model.Scale;
import com.marsol.transform.TransformPlu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = Main.class)
@Import(DatabaseConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
public class TrasnformPluTest {
    @Autowired
    DataSource ds;

    @Test
    public void test(){
        DatabaseService db = new DatabaseService(ds);
        PluController pc = new PluController(db);
        TransformPlu tf = new TransformPlu(pc);

        Scale newScale = new Scale();
        newScale.setBalId(21);
        newScale.setIp("10.42.195.25");

        tf.writeListPlu(newScale, "sas.txt");

    }
}

 */
