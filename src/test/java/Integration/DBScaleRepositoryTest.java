package Integration;

import com.marsol.Main;
import com.marsol.model.Scale;
import com.marsol.repository.impl.Scale.DBScaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(classes = Main.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
public class DBScaleRepositoryTest {

    @Autowired
    DBScaleRepository dbScaleRepository;

    @Test
    public void findAll() {
        List<Scale> scales = dbScaleRepository.findEnabledScales();
        for(Scale scale : scales) {
            System.out.println(scale.toString());
            System.out.println("----------------------------");
        }

    }
}
