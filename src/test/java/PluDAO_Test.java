import com.marsol.Main;
import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.repository.PluDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PluDAO_Test {

    private PluDAO pluDAO;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pluDAO = new PluDAO(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Test
    public void testGetAllArticles_ReturnsList(){
        //Creamos un objeto Scale simulado
        Scale scale = mock(Scale.class);
        when(scale.getBalId()).thenReturn(1);

        //Simulamos que el jdbcTemplate retorna una lista de Strings
        List<String> expectedIds = Arrays.asList("1","2","3","4","5","6","7","8","9");
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedIds);

        List<String> result = pluDAO.getAllArticles(scale);
        assertEquals(expectedIds,result);

        //Verificar que se llamo al metodo query del jdbcTemplate
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    public void testGetAllArticles_ReturnCatchError(){
        Scale scale = mock(Scale.class);
        when(scale.getBalId()).thenReturn(1);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(new RuntimeException("Error"));

        List<String> result = pluDAO.getAllArticles(scale);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllArticles_ReturnEmptyList(){
        Scale scale = mock(Scale.class);
        when(scale.getBalId()).thenReturn(1);

        List<String> expectedIds = Arrays.asList();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedIds);

        List<String> result = pluDAO.getAllArticles(scale);
        assertTrue(result.isEmpty());

    }

    @Test
    public void testGetListArticles_ReturnsList(){
        List<String> ids = Arrays.asList("1","2", "3");

        //Lista de ArticleDTO esperados
        List<ArticleDTO> expectedArticles = Arrays.asList(
                ArticleDTO.builder()
                        .id("1")
                        .price(100)
                        .description("SAS")
                        .conservacion("LOL")
                        .afeley(1)
                        .measure(1)
                        .duracion(10)
                        .tara(0.0)
                        .imprimir(1)
                        .build(),
                ArticleDTO.builder()
                        .id("2")
                        .price(200)
                        .description("MIJO")
                        .conservacion("RAR")
                        .afeley(2)
                        .measure(2)
                        .duracion(20)
                        .tara(1.1)
                        .imprimir(2)
                        .build()
        );

        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class) ,any(RowMapper.class))).thenReturn(expectedArticles);

        List<ArticleDTO> result = pluDAO.getListArticles(ids);
        assertEquals(expectedArticles,result);

        verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(SqlParameterSource.class) ,any(RowMapper.class));
    }

    @Test
    public void testGetListArticles_ReturnEmptyList(){
        List<String> ids = Arrays.asList();
        List<ArticleDTO> expectedArticles = Arrays.asList();
        lenient().when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class) ,any(RowMapper.class))).thenReturn(expectedArticles);
        List<ArticleDTO> result = pluDAO.getListArticles(ids);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetListArticles_ReturnCatchError(){
        List<String> ids = Arrays.asList("1","2", "3");
        List<ArticleDTO> expectedArticles = Arrays.asList();

        when(namedParameterJdbcTemplate.query(anyString(),any(SqlParameterSource.class) ,any(RowMapper.class))).thenThrow(new RuntimeException("Error"));
        List<ArticleDTO> result = pluDAO.getListArticles(ids);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetListArticles_WithMoreThen600Ids(){
        List<String> ids = new ArrayList<>();
        for(int i = 1; i <= 601; i++){
            ids.add(String.valueOf(i));
        }
        List<ArticleDTO> expectedArticles = Arrays.asList(
                ArticleDTO.builder()
                        .id("ID1")
                        .description("Artículo 1")
                        .price(100)
                        .conservacion("Conservación A")
                        .afeley(1)
                        .measure(1)
                        .duracion(10)
                        .tara(1.5)
                        .imprimir(0)
                        .build()
                // Agregar más elementos si se requiere...
        );

        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class) ,any(RowMapper.class))).thenReturn(expectedArticles);

        List<ArticleDTO> result = pluDAO.getListArticles(ids);

        //Capturar el parámtro pasado a la consulta
        ArgumentCaptor<SqlParameterSource> captor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(namedParameterJdbcTemplate).query(anyString(),captor.capture(),any(RowMapper.class));
        SqlParameterSource sqlParameterSource = captor.getValue();

        //Extraer lista de IDS utilizada en el query
        Object idsParam = ((MapSqlParameterSource) sqlParameterSource).getValue("ids");
        assertTrue(idsParam instanceof List);
        List<String> idsListUsed = (List<String>) idsParam;

        //Verificar que se estan utilizando solo 100 IDS
        assertEquals(100,idsListUsed.size());
        assertEquals(ids.subList(0,100),idsListUsed);

    }
}
