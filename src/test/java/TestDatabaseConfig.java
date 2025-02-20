import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestDatabaseConfig {
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return mock(JdbcTemplate.class);
    }
}
