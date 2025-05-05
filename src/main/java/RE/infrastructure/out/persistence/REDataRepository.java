package RE.infrastructure.out.persistence;

import RE.application.port.out.REDataRepositoryPort;
import RE.domain.model.REData;
import Weather.infrastructure.out.persistence.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class REDataRepository implements REDataRepositoryPort {
    @Override
    public void saveAll(List<REData> batch) {
        String sql = """
                INSERT INTO re_data
                (indicator, value, percentage, unit, timestamp, geo_name, geo_id)
                VALUES (?,?,?,?,?,?,?)
                """;
        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (REData d : batch) {
                ps.setString(1, d.getIndicator());
                ps.setDouble(2, d.getValue());
                ps.setDouble(3, d.getPercentage());
                ps.setString(4, d.getUnit());
                ps.setString(5, d.getTimestamp());
                ps.setString(6, d.getGeoName());
                ps.setInt(7, d.getGeoId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    @Override
    public Optional<REData> findLatestByIndicator(String indicator) {
        String sql = """
                SELECT indicator,value,percentage,unit,timestamp,geo_name,geo_id
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
                    REData d = new REData();
                    d.setIndicator(rs.getString("indicator"));
                    d.setValue(rs.getDouble("value"));
                    d.setPercentage(rs.getDouble("percentage"));
                    d.setUnit(rs.getString("unit"));
                    d.setTimestamp(rs.getString("timestamp"));
                    d.setGeoName(rs.getString("geo_name"));
                    d.setGeoId(rs.getInt("geo_id"));
                    return Optional.of(d);
                }
            }

        } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
        }
            return Optional.empty();
    }

}

