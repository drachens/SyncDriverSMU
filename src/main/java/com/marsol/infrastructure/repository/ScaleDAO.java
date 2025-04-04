package com.marsol.infrastructure.repository;

import com.marsol.domain.model.Scale;
import com.marsol.domain.repository.ScaleDAORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Repository
public class ScaleDAO implements ScaleDAORepository {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(ScaleDAO.class);

    public ScaleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Obtiene la lista de balanzas habilitadas desde la base de datos.
     * Filtra por estado 0 y puerto 7001.
     *
     * @return Lista de objetos Scale
     */
    @Override
    @Transactional(readOnly = true)
    public List<Scale> findEnabledScales() {
        String query = "SELECT * FROM zbalanza WHERE estado = 0 AND zbalanza.puerto=7001";
        try{
            // Ejecutamos la consulta y mapeamos cada fila a un objeto Scale
            return jdbcTemplate.query(query, (rs, rowNum) -> new Scale.Builder()
                    .balId(rs.getInt("balid"))
                    .nombre(rs.getString("nombre").trim().toUpperCase())
                    .updated(rs.getTimestamp("updated"))
                    .ip(rs.getString("ip").trim())
                    .etiqueta_p(rs.getInt("etiqueta_p"))
                    .etiqueta_u(rs.getInt("etiqueta_u"))
                    .letraIng(rs.getInt("letraing"))
                    .letraPlu(rs.getInt("letraplu"))
                    .puerto(rs.getInt("puerto"))
                    .masivo(rs.getInt("masivo"))
                    .estado(rs.getInt("estado"))
                    .typeLastUpdate("")
                    .lastUpdate(Timestamp.valueOf(LocalDateTime.of(0,1,1,0,0,0)))
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza los estados de las tablas segun el éxito o fracaso de la carga realizada.
     *
     * @param scale: Balanza a actualizar
     * @param isSuccess: Fue o no cargada
     * @param message: en caso de éxito: Tabla actualizada. en caso de error: Error registrado.
     */

    @Override
    public void setUpdateLastupdateScale(Scale scale, boolean isSuccess, String message) {
        String query = isSuccess ?
                "UPDATE zbalanzalastupdated SET lastupdated = ?, error = '0' WHERE balid = ? AND loadtype = ?" :
                "UPDATE zbalanzalastupdated SET error = ? WHERE balid = ?";
        try{
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(isSuccess){

                jdbcTemplate.update(query,time, scale.getBalId(),message);
            }else {
                jdbcTemplate.update(query,message,scale.getBalId());
            }
        }catch (Exception e){
            logger.error("Error actualizando zbalanzalastupdated en balanza {} : {}",scale.getBalId(), e.getMessage());
        }
    }

    /**
     * Desactiva la carga masiva de una balanza.
     * @param scale: Balanza objetivo
     */

    @Override
    public void disabledMassive(Scale scale) {
        try{
            if(scale.getMasivo() == 1) {
                String query = "UPDATE zbalanza SET masivo = 0 WHERE balid = ?";
                jdbcTemplate.update(query, scale.getBalId());
                logger.info("Carga Masiva desactivada en balanza {}", scale.getBalId());
            }else {
                return;
            }
        } catch (Exception e) {
            logger.error("Error desactivando carga masiva en balanza {} : {}",scale.getBalId(), e.getMessage());
        }

    }
}
