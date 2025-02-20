package com.marsol.repository.impl.PLU;

import com.marsol.model.ArticleDTO;
import com.marsol.model.Scale;
import com.marsol.repository.PLURepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DBPLURepository implements PLURepository {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(DBPLURepository.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DBPLURepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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
        return list;
    }

    @Override
    public List<Integer> getDisabledArticles(Scale scale){
        /*
        int balid = scale.getBalId();
        List<Integer> list = new ArrayList<>();
        String query = "SELECT id FROM (" +
                "   SELECT zm.id FROM zmateriales zm " +
                "   JOIN zbalanzalastupdated zb ON zm.updated > zb.lastupdated " +
                "   WHERE zm.estado = 1 AND zb.balid = ? " +
                "   UNION " +
                "   SELECT zmb.id FROM zmatebalanza zmb " +
                "   JOIN zbalanzalastupdated zb ON zmb.updated > zb.lastupdated " +
                "   WHERE zmb.estado = 1 AND zb.balid = ? " +
                "   UNION " +
                "   SELECT a.id FROM articles a " +
                "   JOIN zbalanzalastupdated zb ON a.updated > zb.lastupdated " +
                "   WHERE a.eliminated = 1 AND zb.balid = ?" +
                ") AS ids";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)){
            for(int i = 1; i <= 3; i++){
                stmt.setInt(i, balid);
            }
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    list.add(rs.getInt("id"));
                }
            }
        }catch (SQLException e){
            logger.error("Error al obtener productos deshabilitados para la balanza {} : {}",balid,e.getMessage());
        }
        return list;

         */
        return List.of();
    }

    @Override
    public List<Integer> getEnabledArticles(Scale scale){
        /*
        int balid = scale.getBalId();
        System.out.println(balid);
        List<Integer> list = new ArrayList<>();
        String query = "SELECT id FROM (" +
                "   SELECT zm.id FROM zmateriales zm " +
                "   JOIN zbalanzalastupdated zb ON zm.updated > zb.lastupdated " +
                "   WHERE zm.estado = 0 AND zb.balid = ? " +
                "   UNION " +
                "   SELECT zmb.id FROM zmatebalanza zmb " +
                "   JOIN zbalanzalastupdated zb ON zmb.updated > zb.lastupdated " +
                "   WHERE zmb.estado = 0 AND zb.balid = ? " +
                "   UNION " +
                "   SELECT a.id FROM articles a " +
                "   JOIN zbalanzalastupdated zb ON a.updated > zb.lastupdated " +
                "   WHERE a.eliminated = 0 AND zb.balid = ?" +
                ") AS ids";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            for(int i = 1; i <= 3; i++){
                stmt.setInt(i, balid);
            }
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    list.add(rs.getInt("id"));
                }
                System.out.println(list);
            }
        }catch (SQLException e){
            logger.error("Error al obtener productos habilitados para la balanza {} : {}",balid,e.getMessage());
        }
        return list;
        */
        return List.of();
    }

    public List<ArticleDTO> getListArticles(List<String> ids){

        List<ArticleDTO> list = new ArrayList<>();
        if(ids == null || ids.isEmpty()){
            return list;
        }
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids.subList(0,100));
        String query = "SELECT " +
                "a.id AS plu_id, a.price AS plu_price, a.description AS plu_description, " +
                "a.measure AS plu_measure, " +
                "zm.ressanitaria AS plu_ressanitaria, zm.conservacion AS plu_conservacion, " +
                "zm.duracion AS plu_duracion, zm.tara AS plu_tara, zm.imprimir AS plu_imprimir, " +
                "zm.idfrigo AS plu_idfrigo, zm.fecben AS plu_fecben, zm.procedencia AS plu_procedencia, " +
                "zm.faenado AS plu_faenado, zm.embalaje AS plu_embalaje, zm.afe_ley AS plu_afe_ley, " +
                "zm.soliliq AS plu_soliliq FROM articles a " +
                "JOIN zmateriales zm ON a.id = zm.id " +
                "WHERE a.id IN (:ids)";
        try{
            list = namedParameterJdbcTemplate.query(query,
                    parameters,
                    (rs,rowNum) -> new ArticleDTO.Builder()
                    .setDescription(rs.getString("plu"))
                    .setPrice(rs.getInt("plu_price"))
                    .setId(rs.getString("plu_id"))
                    .setConservacion(rs.getString("plu_conservacion"))
                    .setAfeley(rs.getInt("plu_afe_ley"))
                    .setMeasure(rs.getInt("plu_measure"))
                    .setDuracion(rs.getInt("plu_duracion"))
                    .setTara(rs.getDouble("plu_tara"))
                    .setImprimir(rs.getInt("plu_imprimir"))
                    .build()
            );
        }catch(Exception e){
            logger.error("Error al extraer lista de articulos : {}",e.getMessage());
            return Collections.emptyList();
        }
        return list;
    }

}
