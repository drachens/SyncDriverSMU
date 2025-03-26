/*
import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;
import com.marsol.domain.model.Scale;
import com.marsol.domain.repository.PLURepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PluServiceTest {

    @Mock
    private PLURepository pluRepository;

    @InjectMocks
    private PluService pluService;

    @Test
    public void testGetAllPlus_FiltraYTransformaIdsCorrectamente(){
        Scale scale = mock(Scale.class);

        List<String> allIds = Arrays.asList("123","12345","999","1234567","sas","s2f44");
        when(pluRepository.getAllArticles(scale)).thenReturn(allIds);

        List<ArticleDTO> articleDTOs = Arrays.asList(
                ArticleDTO.builder().id("123").description("Articulo 123").price(100).build(),
                ArticleDTO.builder().id("12345").description("Articulo 12345").price(200).build(),
                ArticleDTO.builder().id("999").description("Articulo 999").price(150).build()
        );
        when(pluRepository.getListArticles(Arrays.asList("123","12345","999"))).thenReturn(articleDTOs);

        List<PLU> result = pluService.getAllPlus(scale);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(123, result.get(0).getLFCode());
        assertEquals(12345, result.get(1).getLFCode());
        assertEquals(999, result.get(2).getLFCode());
    }
}


 */