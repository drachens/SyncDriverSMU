package Integration;

import com.marsol.Main;
import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.repository.ScaleDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(classes = Main.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
public class ScaleDAOTest {

    @Autowired
    ScaleDAO scaleDAO;

    @Test
    public void findAll() {
        List<Scale> scales = scaleDAO.findEnabledScales();
        for(Scale scale : scales) {
            System.out.println(scale.toString());
            System.out.println("----------------------------");
        }

    }
}
