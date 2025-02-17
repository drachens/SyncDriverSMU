package com.marsol.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;


import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Value("${DBTYPE}")
    private String dbType;

    @Value("${INFORMIX_SERVER}")
    private String informix_server;

    @Value("${DBUSER}")
    private String dbuser;

    @Value("${DBPASSWORD}")
    private String dbpassword;

    @Value("${SERVERNAME}")
    private String serverName;

    @Value("${PORTNUMBER}")
    private String portNumber;

    @Value("${SID}")
    private String sid;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    public DataSource dataSource() {
        try{
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.informix.jdbc.IfxDriver");

            String url = String.format("jdbc:informix-sqli://%s:%s/%s:INFORMIXSERVER=%s", serverName, portNumber, sid, informix_server);

            dataSource.setUrl(url);

            dataSource.setUsername(dbuser);
            dataSource.setPassword(dbpassword);

            logger.info("Archivo de parametros cargado correctamente.");

            return dataSource;
        }catch(Exception e){
            logger.error("Error al cargar archivo de parametros.");
            throw new RuntimeException(e);
        }
    }

    public void test(){
        System.out.println("DBTYPE: " + dbType);
        System.out.println("INFORMIX_SERVER: " + informix_server);
        System.out.println("DBUSER: " + dbuser);
        System.out.println("DBPASSWORD: " + dbpassword);
        System.out.println("SERVERNAME: " + serverName);
        System.out.println("PORTNUMBER: " + portNumber);
        System.out.println("SID: " + sid);
    }
}
