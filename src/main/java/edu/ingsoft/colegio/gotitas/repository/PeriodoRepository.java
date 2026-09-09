package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.Periodo;

/** Acceso a datos de la tabla periodos (bloques fijos 7:05am - 12:05pm). */
public class PeriodoRepository {

    public List<Periodo> findAll() throws SQLException {
        String sql = "SELECT id_periodo, orden, hora_inicio, hora_fin FROM periodos ORDER BY orden";
        List<Periodo> resultado = new ArrayList<>();

        try (PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Periodo(
                        rs.getInt("id_periodo"),
                        rs.getInt("orden"),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fin").toLocalTime()
                ));
            }
        }
        return resultado;
    }
}
