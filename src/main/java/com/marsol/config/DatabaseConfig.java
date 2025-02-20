package com.marsol.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${DBTYPE}")
    private String dbType;

    @Value("${INFORMIX_SERVER}")
    private String informixServer;

    @Value("${DBUSER}")
    private String dbUser;

    @Value("${DBPASSWORD}")
    private String dbPassword;

    @Value("${SERVERNAME}")
    private String serverName;

    @Value("${PORTNUMBER}")
    private String portNumber;

    @Value("${SID}")
    private String sid;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    /**
     * Configura el DataSource para la conexión a la base de datos Informix.
     * Se utiliza BasicDataSource de Apache Commons DBCP para la administración del pool de conexiones.
     * @return DataSource configurado.
     */
    @Bean
    public DataSource dataSource() {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.informix.jdbc.IfxDriver");

            String url = String.format("jdbc:informix-sqli://%s:%s/%s:INFORMIXSERVER=%s",
                    serverName, portNumber, sid, informixServer);

            dataSource.setUrl(url);
            dataSource.setUsername(dbUser);
            dataSource.setPassword(dbPassword);

            // Configuración del pool de conexiones
            dataSource.setInitialSize(5); // Número inicial de conexiones en el pool
            dataSource.setMaxTotal(20);   // Máximo de conexiones en el pool
            dataSource.setMaxIdle(10);    // Máximo de conexiones inactivas
            dataSource.setMinIdle(5);     // Mínimo de conexiones inactivas
            dataSource.setMaxWaitMillis(10000); // Tiempo máximo de espera para obtener una conexión

            logger.info("Conexión a la base de datos configurada correctamente.");
            return dataSource;
        } catch (Exception e) {
            logger.error("Error al configurar la conexión a la base de datos.", e);
            throw new RuntimeException("No se pudo configurar la conexión a la base de datos", e);
        }
    }

    /**
     * Configura un JdbcTemplate basado en el DataSource.
     * JdbcTemplate permite ejecutar consultas SQL de manera eficiente sin manejar conexiones manualmente.
     * @param dataSource Inyectado automáticamente por Spring.
     * @return JdbcTemplate configurado.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Método de prueba para imprimir los parámetros de configuración de la base de datos.
     */
    public void test() {
        System.out.println("DBTYPE: " + dbType);
        System.out.println("INFORMIX_SERVER: " + informixServer);
        System.out.println("DBUSER: " + dbUser);
        System.out.println("DBPASSWORD: " + dbPassword);
        System.out.println("SERVERNAME: " + serverName);
        System.out.println("PORTNUMBER: " + portNumber);
        System.out.println("SID: " + sid);
    }
}

