package com.marsol.infrastructure.repository;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.Scale;
import com.marsol.domain.repository.PLURepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PluDAO implements PLURepository {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(PluDAO.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PluDAO(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<String> getAllArticles(Scale scale){
        int balid = scale.getBalId();
        List<String> list = new ArrayList<>();
        String query = "SELECT a.id FROM articles a " +
                //"JOIN zmatebalanza zmb ON zmb.id = a.id " +
                "WHERE a.eliminated = 0";
        try{
            list = jdbcTemplate.query(query, (rs, rowNum) -> (rs.getString("id")));
        }catch (Exception e){
            logger.error("Error al obtener la IDS de todos los articulos para la balanza {} : {}",balid,e.getMessage());
            return Collections.emptyList();
        }
        if(list.isEmpty()){
            logger.warn("No existen articulos para la balanza id {}",balid);
        }
        return list;
    }

    /**
     *
     * @param scales Balanza objetivo
     * @return
     */

    @Override
    public Map<String,List<String>> getDisabledArticles(List<Scale> scales) {

        if(scales.isEmpty()){
            logger.warn("Lista de balanzas vacia.");
            return Collections.emptyMap();
        }

        List<Integer> ids = scales.stream().map(Scale::getBalId)
                .toList();

        logger.info("Obteniendo productos deshabilitados para la balanzaz {}", ids);


        String query = "SELECT ids.balid, ids.id FROM TABLE(MULTISET(" +
                "   SELECT zb.balid AS balid, zm.id AS id " +
                "   FROM zmateriales zm " +
                "   JOIN zbalanzalastupdated zb ON zm.updated > zb.lastupdated " +
                "   WHERE zm.estado = 1 AND zb.loadtype='ZMATERIALES' AND zb.balid IN (:ids) " +
                "   UNION " +
                "   SELECT zb.balid AS balid, zmb.id AS id FROM zmatebalanza zmb " +
                "   JOIN zbalanzalastupdated zb ON zmb.updated > zb.lastupdated " +
                "   WHERE zmb.estado = 1 AND zb.loadtype='ZMATEBALANZA' AND zb.balid IN (:ids) " +
                "   UNION " +
                "   SELECT zb.balid AS balid, a.id AS id FROM articles a " +
                "   JOIN zbalanzalastupdated zb ON a.updated > zb.lastupdated " +
                "   WHERE a.eliminated = 1 AND zb.loadtype='ARTICLES' AND zb.balid IN (:ids) " +
                ")) AS ids";
        System.out.print(ids);
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        Map<String,List<String>> map = new LinkedHashMap<>();

        try{
            namedParameterJdbcTemplate.query(query,parameters,
                    (rs) -> {
                        String balid = String.valueOf(rs.getInt("balid"));
                        String articleId = rs.getString("id");

                        //Agregar el ID al mapa agrupado por balanza
                        map.computeIfAbsent(balid, k -> new ArrayList<>()).add(articleId);
                    });
        }catch(Exception e){
            logger.error("Error al obtener lista de articulos deshabilitados para balanza: ({})",e.getMessage());
            return Collections.emptyMap();
        }
        return map;
    }
    @Override
    public List<String> getDisabledArticles2(Scale scale) {
        if (scale == null) {
            logger.warn("La balanza proporcionada es nula.");
            return Collections.emptyList();
        }

        int balId = scale.getBalId();
        logger.info("Obteniendo productos deshabilitados para la balanza {}", balId);

        String query = "SELECT ids.id FROM TABLE(MULTISET(" +
                "   SELECT zm.id AS id " +
                "   FROM zmateriales zm " +
                "   JOIN zbalanzalastupdated zb ON zm.updated > zb.lastupdated " +
                "   WHERE zm.estado = 1 AND zb.loadtype='ZMATERIALES' AND zb.balid = :balId " +
                "   UNION " +
                "   SELECT zmb.id AS id FROM zmatebalanza zmb " +
                "   JOIN zbalanzalastupdated zb ON zmb.updated > zb.lastupdated " +
                "   WHERE zmb.estado = 1 AND zb.loadtype='ZMATEBALANZA' AND zb.balid = :balId " +
                "   UNION " +
                "   SELECT a.id AS id FROM articles a " +
                "   JOIN zbalanzalastupdated zb ON a.updated > zb.lastupdated " +
                "   WHERE a.eliminated = 1 AND zb.loadtype='ARTICLES' AND zb.balid = :balId " +
                ")) AS ids";

        SqlParameterSource parameters = new MapSqlParameterSource("balId", balId);
        List<String> disabledArticles;

        try {
            disabledArticles = namedParameterJdbcTemplate.query(query, parameters,
                    (rs, rowNum) -> rs.getString("id"));
        } catch (Exception e) {
            logger.error("Error al obtener lista de articulos deshabilitados para balanza {}: {}", balId, e.getMessage());
            return Collections.emptyList();
        }

        return disabledArticles;
    }


    /**
     *
     * @param scale Balanza objetivo
     * @return Retorna una lista de ID, donde se incluyen TODOS los productos habilitados para la balanza.
     */
    public List<String> getEnabledArticlesMasive(Scale scale){
        int balid = scale.getBalId();
        logger.info("Obteniendo productos habilitados para carga masiva de balanza {}", balid);
        String query = "SELECT a.id FROM articles a " +
                "WHERE a.eliminated = 0 AND a.id IN (" +
                "SELECT zm.id FROM zmateriales zm " +
                "JOIN zmatebalanza zmb ON zmb.id = zm.id " +
                "WHERE zm.estado = 0 AND zmb.balid = :balid " +
                "AND zmb.estado = 0 AND zm.brand = 'FRUTAS Y VERDURAS')";
        MapSqlParameterSource parameters = new MapSqlParameterSource("balid", balid);
        try{
            return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> rs.getString("id"));
        }catch (Exception e){
            logger.error("error");
        }
        return List.of();
    }

    /**
     *
     * @param scale Balanza objetivo
     * @return Retorna una lista de String que representan los ID de los artículos habilitados
     * que han cambiado, es decir, donde la fecha de actualización del articulo es posterior a la ultima
     * fecha de actualización de la balanza.
     */
    @Override
    public List<String> getEnabledArticles(Scale scale) {
        int balid = scale.getBalId();
        logger.info("Obteniendo productos habilitados para la balanza -> {}", balid);
        String query = "SELECT id, lastupdated FROM TABLE(MULTISET(" +
                "SELECT a.id AS id, zb1.lastupdated AS lastupdated FROM articles a " +
                "JOIN zbalanzalastupdated zb1 ON a.updated > zb1.lastupdated " +
                "JOIN zmateriales zm ON zm.id = a.id " +
                "WHERE a.eliminated = 0 AND zb1.loadtype = 'ARTICLES' AND zm.brand = 'FRUTAS Y VERDURAS' AND zb1.balid = :balid " +
                "UNION " +
                "SELECT zm1.id AS id, zb2.lastupdated AS lastupdated FROM zmateriales zm1 " +
                "JOIN zbalanzalastupdated zb2 ON zm1.updated > zb2.lastupdated " +
                "WHERE zm1.estado = 0 AND zb2.loadtype = 'ZMATERIALES' AND zm1.brand = 'FRUTAS Y VERDURAS' AND zb2.balid = :balid " +
                "UNION " +
                "SELECT zmb.id AS id, zb3.lastupdated AS lastupdated FROM zmatebalanza zmb " +
                "JOIN zbalanzalastupdated zb3 ON zmb.updated > zb3.lastupdated " +
                "JOIN zmateriales zm2 ON zm2.id = zmb.id " +
                "WHERE zmb.estado = 0 AND zb3.loadtype = 'ZMATEBALANZA' AND zm2.brand = 'FRUTAS Y VERDURAS' AND zb3.balid = :balid " +
                ")) AS ids";
        String query3 = "SELECT a.id FROM articles a, zbalanzalastupdated zblu " +
                "WHERE a.eliminated = 0 AND a.updated > zblu.lastupdated " +
                "AND a.id IN (" +
                "SELECT ids.id FROM TABLE(MULTISET(" +
                "SELECT zm.id AS id FROM zmateriales zm " +
                "JOIN zbalanzalastupdated zb1 ON zm.updated > zb1.lastupdated " +
                "WHERE zm.estado = 0 AND zb1.balid = :balid AND zb1.loadtype='ZMATERIALES'" +
                "AND zm.brand = 'FRUTAS Y VERDURAS' " +
                "UNION " +
                "SELECT zmb.id AS id FROM zmatebalanza zmb " +
                "JOIN zbalanzalastupdated zb2 ON zmb.updated > zb2.lastupdated " +
                "JOIN zmateriales zm2 ON zm2.id = zmb.id " +
                "WHERE zmb.estado = 0 AND zb2.balid = :balid AND zm2.brand = 'FRUTAS Y VERDURAS')) AS ids" +
                ")";
        String query2 = "SELECT ids.id FROM TABLE(MULTISET( " +
                "SELECT a.id AS id FROM articles a " +
                "JOIN zbalanzalastupdated zb3 ON a.updated > zb3.lastupdated " +
                "JOIN zmateriales zmate ON zmate.id = a.id " +
                "WHERE a.eliminated = 0 AND zb3.balid = :balid AND zmate.brand = 'FRUTAS Y VERDURAS' " +
                "UNION " +
                "SELECT zm.id AS id FROM zmateriales zm " +
                "JOIN zbalanzalastupdated zb1 ON zm.updated > zb1.lastupdated " +
                "WHERE zm.estado = 0 AND zb1.balid = :balid AND zm.brand = 'FRUTAS Y VERDURAS' " +
                "UNION " +
                "SELECT zmb.id AS id FROM zmatebalanza zmb " +
                "JOIN zbalanzalastupdated zb2 ON zmb.updated > zb2.lastupdated " +
                "JOIN zmateriales zmate2 ON zmb.id = zmate2.id " +
                "WHERE zmb.estado = 0 AND zb2.balid = :balid AND zmate2.brand = 'FRUTAS Y VERDURAS' " +
                ")) AS ids";

        //SqlParameterSource parameters = new MapSqlParameterSource("balid", balid);

        MapSqlParameterSource parameters = new MapSqlParameterSource("balid", balid);
        try {
            return namedParameterJdbcTemplate.query(query, parameters, (rs, rowNum) -> rs.getString("id"));
        }catch (DataAccessException dae){
            logger.error("Error de acceso a datos para balanza {} : {}",balid,dae.getMessage(),dae);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error al obtener productos habilitados para la balanza {}: {}", balid, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     *
     * @param ids Lista de ID de artículos Habilitados.
     * @return Retorna una lista de Articulos según los ID habilitados
     */

    @Override
    public List<ArticleDTO> getListArticles(List<String> ids){

        List<ArticleDTO> list = new ArrayList<>();
        if(ids == null || ids.isEmpty()){
            logger.warn("No hay productos habilitados a cargar.");
            return list;
        }
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids.subList(0,Math.min(ids.size(),100)));
        String query = "SELECT " +
                "a.id AS plu_id, a.price AS plu_price, a.description AS plu_description, " +
                "a.measure AS plu_measure, " +
                "zm.ressanitaria AS plu_ressanitaria, zm.conservacion AS plu_conservacion, " +
                "zm.duracion AS plu_duracion, zm.tara AS plu_tara, zm.imprimir AS plu_imprimir, " +
                "zm.idfrigo AS plu_idfrigo, zm.fecben AS plu_fecben, zm.procedencia AS plu_procedencia, " +
                "zm.faenado AS plu_faenado, zm.embalaje AS plu_embalaje, zm.afe_ley AS plu_afe_ley, " +
                "zm.brand AS plu_brand, zm.additionaldescription AS plu_additionalDesc, "+
                "zm.soliliq AS plu_soliliq FROM articles a " +
                "JOIN zmateriales zm ON a.id = zm.id " +
                "WHERE a.id IN (:ids)";
        try{
            list = namedParameterJdbcTemplate.query(query,
                    parameters,
                    (rs,rowNum) -> ArticleDTO.builder()
                    .description(rs.getString("plu_description"))
                    .price(rs.getInt("plu_price"))
                    .id(rs.getString("plu_id"))
                    .conservacion(rs.getString("plu_conservacion"))
                    .afeley(rs.getInt("plu_afe_ley"))
                    .measure(rs.getInt("plu_measure"))
                    .duracion(rs.getInt("plu_duracion"))
                    .tara(rs.getDouble("plu_tara"))
                    .imprimir(rs.getInt("plu_imprimir"))
                    .brand(rs.getString("plu_brand"))
                    .additionalDescription(rs.getString("plu_additionalDesc"))
                    .build()
            );
        }catch(Exception e){
            logger.error("Error al extraer lista de articulos : {}",e.getMessage());
            return Collections.emptyList();
        }
        return list;
    }

}
