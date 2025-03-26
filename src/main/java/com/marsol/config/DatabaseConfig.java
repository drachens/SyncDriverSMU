package com.marsol.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${DB_TYPE}")
    private String dbType;

    @Value("${INFORMIX_SERVER}")
    private String informixServer;

    @Value("${DB_USER}")
    private String dbUser;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${SERVERNAME}")
    private String serverName;

    @Value("${PORT_NUMBER}")
    private String portNumber;

    @Value("${SID}")
    private String sid;

    /**
     * Configura el DataSource para la conexión a Informix usando HikariCP.
     * @return DataSource configurado.
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        String url = String.format("jdbc:informix-sqli://%s:%s/%s:INFORMIXSERVER=%s",
                serverName, portNumber, sid, informixServer);

        dataSource.setJdbcUrl(url);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName("com.informix.jdbc.IfxDriver");

        logger.info("Conectando a base de datos Informix, usando pool de HikariCP.");
        return dataSource;
    }

    /**
     * Configura un JdbcTemplate basado en el DataSource.
     * @param dataSource Inyectado automáticamente por Spring.
     * @return JdbcTemplate configurado.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /*
     * Metodo de prueba para imprimir los parámetros de configuración de la base de datos.

    public void test() {
        logger.info("DB_TYPE: {}", dbType);
        logger.info("INFORMIX_SERVER: {}", informixServer);
        logger.info("DB_USER: {}", dbUser);
        logger.info("DB_PASSWORD: {}", dbPassword);
        logger.info("SERVERNAME: {}", serverName);
        logger.info("PORT_NUMBER: {}", portNumber);
        logger.info("SID: {}", sid);
    }
     */
}

