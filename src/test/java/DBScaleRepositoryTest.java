import com.marsol.Main;
import com.marsol.model.Scale;
import com.marsol.repository.impl.Scale.DBScaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;


@SpringBootTest(classes = Main.class)
@ExtendWith(MockitoExtension.class)
public class DBScaleRepositoryTest {

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DBScaleRepository dbScaleRepository;

    @Test
    public void testFindEnabledScales(){
        //Simulacion de datos
        Scale scale = new Scale.Builder()
                .ip("10.0.0.10")
                .nombre("HPRT")
                .balId(1)
                .build();

        List<Scale> mockScales = Collections.singletonList(scale);

        //Simulamos resultado del query
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(mockScales);

        //Ejecutamos el metodo a probar
        List<Scale> scales = dbScaleRepository.findEnabledScales();

        //Validamos que la consulta devolvio datos
        assertFalse(scales.isEmpty());
        assertEquals(1, scales.size());
        assertEquals(scale, scales.get(0));
        assertEquals("HPRT", scales.get(0).getNombre());

        // Verificamos que se llamó a jdbcTemplate.query()
        verify(jdbcTemplate, times(2)).query(anyString(), any(RowMapper.class));
    }
}
