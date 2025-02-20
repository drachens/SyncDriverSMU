package Integration;

import com.marsol.Main;
import com.marsol.model.ArticleDTO;
import com.marsol.model.Scale;
import com.marsol.repository.impl.PLU.DBPLURepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(classes = Main.class)
@TestPropertySource(locations = "classpath:application.properties")
public class DBPLURepositoryTest {

    @Autowired
    private DBPLURepository repository;


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
