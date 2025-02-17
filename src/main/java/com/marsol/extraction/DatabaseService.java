package com.marsol.extraction;

import com.marsol.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseService {
    private final DataSource dataSource;

    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    //Consultas de Balanzas

    public List<Map<String,Object>> getEnabledScales(){
        List<Map<String,Object>> scales = new ArrayList<>();
        String query = "SELECT * FROM zbalanza WHERE " +
                "zbalanza.estado = 0 " +
                "AND zbalanza.balid = 20";


        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("balid",resultSet.getInt("balid"));
                map.put("updated", resultSet.getTimestamp("updated"));
                map.put("nombre", resultSet.getString("nombre"));
                map.put("etiqueta_u", resultSet.getInt("etiqueta_u"));
                map.put("etiqueta_p", resultSet.getInt("etiqueta_p"));
                map.put("letraplu", resultSet.getInt("letraplu"));
                map.put("letraing", resultSet.getInt("letraing"));
                map.put("ip", resultSet.getString("ip"));
                map.put("puerto", resultSet.getInt("puerto"));
                map.put("masivo", resultSet.getInt("masivo"));
                map.put("estado", resultSet.getInt("estado"));
                scales.add(map);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return scales;
    }

    /*

    ------------Consultas de PLU----------------

    Consultas recibiran parametro balID porque dependen de la balanza.
    Primero eliminar PLU de acuerdo a (Seleccion de PLU a eliminar segun su id):
    (Se debe hacer un cruce entre los id obtenidos)
        1) select zmateriales.id from zmateriales, zbalanzalastupdated
            where zmateriales.estado = 1 and zmateriales.updated > zbalanzalastupdated.lastupdated

        2) select zmatebalanza.id from zmatebalanza, zbalanzalastupdated
            where zmatebalanza.estado = 1 and zmatebalanza.updated > zbalanzalastupdated.lastupdated

        3) select articles.id from articles, zbalanzalastupdated
            where articles.eliminated = 1 and articles.updated > zbalanzalastupdated.lastupdated

     Segundo, envía carga de los PLU de acuerdo a (Seleccion de ID a cargar):
     (Se debe hacer un cruce entre los id obtenidos)
        1) select materiales.id from zmateriales, zbalanzalastupdated
            where zmateriales.estado = 0 and zmateriales.updated > zbalanzalastupdated.lastupdated

        2) select zmatebalanza.id from zmatebalanza, zbalanzalastupdated
            where zmatebalanza.estado = 0 and zmatebalanza.updated > zbalanzalastupdated.lastupdated

        3) select articles.id from articles, zbalanzalastupdated
            where articles.eliminated = 0 and articles.updated > zbalanzalastupdated.lastupdated
     */

    private List<Map<String,Object>> exectuteGetIdQuerys(String query, int... params){
        List<Map<String,Object>> result = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){

            for(int i = 0; i < params.length; i++){
                ps.setInt(i + 1, params[i]);
            }

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("id", rs.getString("id"));
                    result.add(map);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String,Object>> getDisabledZmateriales(int idBal){
        String query = "SELECT zm.id " +
                "FROM zmateriales zm " +
                "JOIN zbalanzalastupdated zb ON zm.updated > zb.lastupdated AND zb.balid = ? " +
                "WHERE zm.estado = 1";
        return exectuteGetIdQuerys(query, idBal);
    }

    public List<Map<String,Object>> getDisabledZmatebalanza(int idBal){
        String query = "SELECT zmb.id " +
                "FROM zmatebalanza zmb " +
                "JOIN zbalanzalastupdated zb ON zmb.updated > zb.lastupdated AND zmb.balid = ? AND zb.balid = ? " +
                "WHERE zmb.estado = 1";

        return exectuteGetIdQuerys(query, idBal, idBal);
    }

    public List<Map<String,Object>> getDisabledArticles(int idBal){
        String query = "SELECT a.id " +
                "FROM articles a " +
                "JOIN zbalanzalastupdated zb ON a.updated > zb.lastupdated AND zb.balid = ? " +
                "WHERE a.estado = 1";
        return exectuteGetIdQuerys(query, idBal);
    }

    public List<Map<String,Object>> getEnabledZmateriales(int idBal){
        String query = "Select zm.id " +
                "FROM zmateriales zm " +
                "JOIN zbalanzalastupdated zb ON zm.updated > zb.lastupdated AND zb.balid = ? " +
                "WHERE zm.estado = 0";
        return exectuteGetIdQuerys(query, idBal);
    }

    public List<Map<String,Object>> getEnabledZmatebalanza(int idBal){
        String query = "SELECT zmb.id " +
                "FROM zmatebalanza zmb " +
                "JOIN zbalanzalastupdated zb ON zmb.updated > zb.lastupdated AND zmb.balid = ? AND zb.balid = ? " +
                "WHERE zmb.estado = 0";
        return exectuteGetIdQuerys(query, idBal, idBal);
    }

    public List<Map<String,Object>> getEnabledArticles(int idBal){
        String query = "SELECT a.id " +
                "FROM articles a " +
                "JOIN zbalanzalastupdated zb ON a.updated > zb.lastupdated AND zb.balid = ? " +
                "WHERE a.eliminated = 0";
        return exectuteGetIdQuerys(query, idBal);
    }

    public List<Map<String,Object>> getMasiveArticles(int idBal){
        String query = "SELECT a.id " +
                "FROM articles a " +
                "JOIN zbalanzalastupdated zb ON zb.balid = ? " +
                "WHERE a.eliminated = 0";
        return exectuteGetIdQuerys(query, idBal);
    }

    public List<Article> getPluItems(List<String> ids){
        List<Article> articles = new ArrayList<>();
        String placeholder = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String query = "SELECT " +
                "a.id AS plu_id, a.description AS plu_description, a.price AS plu_price, a.measure AS plu_measure, " +
                "zm.ressanitaria AS plu_ressanitaria, zm.conservacion AS plu_conservacion, zm.duracion AS plu_duracion, " +
                "zm.tara AS plu_tara, zm.imprimir AS plu_imprimir, zm.procedencia AS plu_pocedencia, zm.idfrigo AS plu_idFrigo, " +
                "zm.fecben AS plu_fecben, " +
                "zm.faenado AS plu_faenado, zm.embalaje AS plu_embalaje, zm.afe_ley AS plu_afeley, zm.soliliq AS plu_soliliq " +
                "FROM articles a " +
                "JOIN zmateriales zm ON a.id = zm.id " +
                "WHERE a.id IN (" + placeholder + ")";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){

            for(int i = 0; i < ids.size(); i++){
                ps.setString(i + 1, ids.get(i));
            }
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Article article = new Article();
                    article.setId(rs.getString("plu_id"));
                    article.setDescription(rs.getString("plu_description"));
                    article.setPrice(rs.getInt("plu_price"));
                    article.setMeasure(rs.getInt("plu_measure"));
                    article.setRessanitaria(rs.getString("plu_ressanitaria"));
                    article.setConservacion(rs.getString("plu_conservacion"));
                    article.setDuracion(rs.getInt("plu_duracion"));
                    article.setTara(rs.getDouble("plu_tara"));
                    article.setImprimir(rs.getInt("plu_imprimir"));
                    article.setProcedencia(rs.getString("plu_pocedencia"));
                    article.setIdFrigo(rs.getInt("plu_idFrigo"));
                    article.setFecben(rs.getTimestamp("plu_fecben"));
                    article.setFaenado(rs.getString("plu_faenado"));
                    article.setEmbalaje(rs.getString("plu_embalaje"));
                    article.setAfeley(rs.getInt("plu_afeley"));
                    articles.add(article);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return articles;
    }

    public List<Map<String,Object>> getAllArticles(int idBal){
        List<Map<String,Object>> articles = new ArrayList<>();
        String query = "SELECT " +
                "a.id AS plu_id, a.description AS plu_description, a.price AS plu_price, a.measure AS plu_measure, " +
                "zm.ressanitaria AS plu_ressanitaria, zm.conservacion AS plu_conservacion, zm.duracion AS plu_duracion, " +
                "zm.tara AS plu_tara, zm.imprimir AS plu_imprimir, zm.procedencia AS plu_pocedencia, zm.idfrigo AS plu_idFrigo, " +
                "zm.fecben AS plu_fecben, " +
                "zm.faenado AS plu_faenado, zm.embalaje AS plu_embalaje, zm.afe_ley AS plu_afeley, zm.soliliq AS plu_soliliq " +
                "FROM articles a " +
                "JOIN zmateriales zm ON a.id = zm.id " +
                "JOIN zmatebalanza zmb ON a.id = zmb.id " +
                "WHERE a.eliminated = 0 AND zmb.balid = " + idBal;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("plu_id", rs.getString("plu_id"));
                map.put("plu_description", rs.getString("plu_description"));
                map.put("plu_price", rs.getInt("plu_price"));
                map.put("plu_measure", rs.getInt("plu_measure"));
                map.put("plu_ressanitaria", rs.getString("plu_ressanitaria"));
                map.put("plu_conservacion", rs.getString("plu_conservacion"));
                map.put("plu_duracion", rs.getInt("plu_duracion"));
                map.put("plu_tara", rs.getDouble("plu_tara"));
                map.put("plu_imprimir", rs.getInt("plu_imprimir"));
                map.put("plu_pocedencia",rs.getString("plu_pocedencia"));
                map.put("plu_idFrigo",rs.getInt("plu_idFrigo"));
                map.put("plu_fecben", rs.getTimestamp("plu_fecben"));
                map.put("plu_faenado", rs.getString("plu_faenado"));
                map.put("plu_embalaje", rs.getString("plu_embalaje"));
                map.put("plu_afeley", rs.getInt("plu_afeley"));
                articles.add(map);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return articles;
    }


    public List<Map<String,Object>> getAllStores(){
        String query = "SELECT * FROM zmaetienda";
        List<Map<String,Object>> stores = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery()){

            while(resultSet.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("localid", resultSet.getInt("localid"));
                map.put("nombre",resultSet.getString("nombre"));
                map.put("direccion",resultSet.getString("direccion"));
                map.put("updated",resultSet.getTimestamp("updated"));
                stores.add(map);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return stores;
    }
}
