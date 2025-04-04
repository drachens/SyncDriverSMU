package com.marsol.infrastructure.repository;

import com.marsol.application.dto.Note1DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreDAO {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(StoreDAO.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StoreDAO(JdbcTemplate jdbcTemplate,
                    NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * private getStoreInfo()
     * Extrae la direccion y nombre del local.
     * @return
     */

    private Note1DTO getStoreInfo() {
        String query = "SELECT FIRST 1 * FROM zmaetienda";

        return jdbcTemplate.queryForObject(query,(rs, rowNum) -> {
            Note1DTO note1DTO = new Note1DTO();
            note1DTO.setDireccion(rs.getString("direccion"));
            note1DTO.setNombre(rs.getString("nombre"));
            return note1DTO;
        });
    }

    /**
     * public getAllStoresInfo()
     *
     * Utiliza la información extraida del local y además incluye: La resolucion sanitaria
     * , el id del articulo y el tipo de embalaje de cada articulo habilitado.
     * @param ids: Corresponde a la lista de articulos habilitados para la tienda.
     * @return
     */

    public List<Note1DTO> getAllStoresInfo(List<String> ids) {
        Note1DTO note1DTOTemplate = getStoreInfo();
        String direccion = note1DTOTemplate.getDireccion();
        String nombre = note1DTOTemplate.getNombre();
        List<Note1DTO> stores = new ArrayList<>();
        if(ids.isEmpty()){
            logger.warn("No existen ids.");
            return stores;
        }
        String query = "SELECT id,embalaje, ressanitaria FROM zmateriales " +
                "WHERE zmateriales.id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids",ids);

        try{
            stores = namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> {
                Note1DTO note1DTO = new Note1DTO();
                note1DTO.setDireccion(direccion);
                note1DTO.setNombre(nombre);
                note1DTO.setResSanitaria(rs.getString("ressanitaria"));
                note1DTO.setIdArticle(rs.getInt("id"));
                note1DTO.setEmbalaje(rs.getString("embalaje"));
               return note1DTO;
            });
        }catch(Exception e){
            logger.error("Error al extraer embalaje.");
            return stores;
        }
        return stores;
    }
}
