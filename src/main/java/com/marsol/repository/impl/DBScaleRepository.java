package com.marsol.repository.impl;

import com.marsol.model.Scale;
import com.marsol.repository.ScaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String query = "SELECT * FROM zbalanza WHERE estado = 0";
        try(
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                ){
            while (rs.next()) {
                Scale scale = new Scale.Builder()
                        .balId()
            }
        }catch (SQLException e) {

        }

        return List.of();
    }

    @Override
    public List<Scale> findCargaMaestra() {
        return List.of();
    }

    @Override
    public void updateLastupdateScale(Scale scale) {

    }
}
