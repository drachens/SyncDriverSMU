import com.marsol.Main;
import com.marsol.config.DatabaseConfig;
import com.marsol.controller.PluController;
import com.marsol.controller.ResSanitariaController;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.Note;
import com.marsol.model.Scale;
import com.marsol.transform.TransformNote1;
import com.marsol.transform.TransformPlu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest(classes = Main.class)
@Import(DatabaseConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
public class TransformNote1Test {
    @Autowired
    DataSource ds;

    @Test
    public void test() {
        DatabaseService db = new DatabaseService(ds);
        PluController pc = new PluController(db);
        ResSanitariaController rsc = new ResSanitariaController(db,pc);
        TransformNote1 transformNote1 = new TransformNote1(rsc);

        Scale newScale = new Scale();
        newScale.setBalId(21);
        newScale.setIp("10.42.195.25");
        transformNote1.writeListNote1(newScale,"sas");
    }
}
