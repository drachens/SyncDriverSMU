package com.marsol.repository.impl.Scale;

import com.marsol.model.Scale;
import com.marsol.repository.ScaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBScaleRepository implements ScaleRepository {

    private final DataSource dataSource;
    private final static Logger logger = LoggerFactory.getLogger(DBScaleRepository.class);

    public DBScaleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Scale> findEnabledScales() {
        List<Scale> scales = new ArrayList<>();
        String query = "SELECT * FROM zbalanza WHERE estado = 0 AND port = 7000";
        try(
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                ){
            while (rs.next()) {
                Scale scale = new Scale.Builder()
                        .balId(rs.getInt("balid"))
                        .nombre(rs.getString("nombre"))
                        .updated(rs.getTimestamp("updated"))
                        .ip(rs.getString("ip"))
                        .puerto(rs.getInt("puerto"))
                        .masivo(rs.getInt("masivo"))
                        .estado(rs.getInt("estado"))
                        .build();
                scales.add(scale);
            }
        }catch (SQLException e) {
            logger.error("Error obteniendo balanzas disponibles: {}",e.getMessage());
        }
        return scales;
    }

    @Override
    public void setUpdateLastupdateScale(Scale scale, boolean isSuccess, String message) {
        try{
            if(isSuccess){
                String query = "UPDATE zbalanzalastupdated SET lastupdated = ?, loadtype = ? WHERE balid = ?";
                PreparedStatement stmt = dataSource.getConnection().prepareStatement(query);

                stmt.setTimestamp(1, scale.getLastUpdate());
                stmt.setString(2, scale.getTypeLastUpdate());
                stmt.setInt(3,scale.getBalId());

                stmt.executeUpdate();
            }else{
                String query = "UPDATE zbalanzalastupdated SET error = ? WHERE balid = ?";
                PreparedStatement stmt = dataSource.getConnection().prepareStatement(query);
                stmt.setString(1, message);
                stmt.setInt(2, scale.getBalId());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setUpdateCargaMaestra(Scale scale) {
        try{
            if(scale.getMasivo() == 1){
                String query = "UPDATE zbalanza SET masivo = ? WHERE balid = ?";

                PreparedStatement stmt = dataSource.getConnection().prepareStatement(query);
                stmt.setInt(1, 0);
                stmt.setInt(2, scale.getBalId());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
