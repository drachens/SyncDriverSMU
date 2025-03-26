package Integration;

import com.marsol.Main;
import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.Scale;
import com.marsol.infrastructure.repository.PluDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(classes = Main.class)
@TestPropertySource(locations = "classpath:application.properties")
public class PluDAOTest {

    @Autowired
    private PluDAO repository;


    @Test
    public void getEnabledArticles(){
        Scale scale = new Scale.Builder()
                .balId(33)
                .ip("10.42.195.25")
                .build();

        List<String> ids = repository.getAllArticles(scale);
        List<ArticleDTO> articleDTOS = repository.getListArticles(ids);

        for(ArticleDTO articleDTO : articleDTOS){
            System.out.println(articleDTO.toString());
        }

    }
}
