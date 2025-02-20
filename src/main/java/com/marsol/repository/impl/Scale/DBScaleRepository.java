package com.marsol.repository.impl.Scale;

import com.marsol.model.Scale;
import com.marsol.repository.ScaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class DBScaleRepository implements ScaleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(DBScaleRepository.class);

    public DBScaleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /**
     * Obtiene la lista de balanzas habilitadas desde la base de datos.
     * Filtra por estado 0 y puerto 7000.
     *
     * @return Lista de objetos Scale
     */
    @Override
    public List<Scale> findEnabledScales() {
        List<Scale> scales = new ArrayList<>();
        String query = "SELECT * FROM zbalanza WHERE estado = 0 AND port = 7000";
        try{
            // Ejecutamos la consulta y mapeamos cada fila a un objeto Scale
            return jdbcTemplate.query(query, (rs, rowNum) -> new Scale.Builder()
                    .balId(rs.getInt("balid"))
                    .nombre(rs.getString("nombre"))
                    .updated(rs.getTimestamp("updated"))
                    .ip(rs.getString("ip"))
                    .puerto(rs.getInt("puerto"))
                    .masivo(rs.getInt("masivo"))
                    .estado(rs.getInt("estado"))
                    .build());
        } catch (Exception e) {
            logger.error("Error obteniendo balanzas disponibles: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void setUpdateLastupdateScale(Scale scale, boolean isSuccess, String message) {
        String query = isSuccess ?
                "UPDATE zbalanzalastupdated SET lastupdated = ?, loadtype = ? WHERE balid = ?" :
                "UPDATE zbalanzalastupdated SET error = ? WHERE balid = ?";
        try{
            if(isSuccess){
                jdbcTemplate.update(query, scale.getBalId(), scale.getNombre(), scale.getUpdated());
            }else {
                jdbcTemplate.update(query,message,scale.getBalId());
            }
        }catch (Exception e){
            logger.error("Error actualizando zbalanzalastupdated en balanza {} : {}",scale.getBalId(), e.getMessage());
        }
    }

    @Override
    public void disabledMassive(Scale scale) {
        try{
            if(scale.getMasivo() == 1){
                String query = "UPDATE zbalanza SET masivo = 0 WHERE balid = ?";
                jdbcTemplate.update(query, scale.getBalId());
                logger.info("Carga Masiva desactivada en balanza {}", scale.getBalId());
            }
        } catch (Exception e) {
            logger.error("Error desactivando carga masiva en balanza {} : {}",scale.getBalId(), e.getMessage());
        }

    }
}
