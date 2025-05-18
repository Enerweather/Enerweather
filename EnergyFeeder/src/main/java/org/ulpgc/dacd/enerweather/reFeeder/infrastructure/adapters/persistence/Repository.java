package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.persistence;

import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port.RepositoryPort;
import org.ulpgc.dacd.enerweather.reFeeder.application.domain.model.Energy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class Repository implements RepositoryPort {
    @Override
    public void saveAll(List<Energy> batch) {
        String sql = """
                INSERT INTO re_data
                (indicator, value, timestamp)
                VALUES (?,?,?)
                """;
        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Energy d : batch) {
                ps.setString(1, d.getIndicator());
                ps.setDouble(2, d.getValue());
                ps.setString(3, d.getTimestamp());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    @Override
    public Optional<Energy> findLatest(String indicator) {
        String sql = """
                SELECT indicator,value,timestamp
                FROM re_data
                WHERE indicator = ?
                ORDER BY timestamp DESC
                LIMIT 1
                """;
        try (Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, indicator);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Energy d = new Energy(
                        rs.getString("indicator"),
                        rs.getDouble("value"),
                        rs.getString("timestamp")
                    );
                    return Optional.of(d);
                }
            }

        } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
        }
            return Optional.empty();
    }

}

