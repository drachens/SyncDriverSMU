import com.marsol.Main;
import com.marsol.config.DatabaseConfig;
import com.marsol.controller.PluController;
import com.marsol.controller.ResSanitariaController;
import com.marsol.controller.ScaleController;
import com.marsol.controller.ServiceController;
import com.marsol.extraction.DatabaseService;
import com.marsol.model.Scale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Main.class)
@Import(DatabaseConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
public class ServiceTest {

    @Autowired
    DataSource dataSource;

    DatabaseService databaseService = new DatabaseService(dataSource);
    ScaleController scaleController = new ScaleController(databaseService);
    PluController pluController = new PluController(databaseService);
    ResSanitariaController resSanitariaController = new ResSanitariaController(databaseService, pluController);
    ServiceController serviceController = new ServiceController(pluController,resSanitariaController,scaleController);

    @BeforeEach
    public void setUp() {
        Scale scale = new Scale();
        scale.setIp("10.42.195.25");
        scale.setBalId(33);
        List<Scale> scales = new ArrayList<>();
        scales.add(scale);
    }

    @Test
    public void testLoad(){
        serviceController.load();
    }

}
