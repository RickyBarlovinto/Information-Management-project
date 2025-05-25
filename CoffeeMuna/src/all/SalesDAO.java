package all;

import java.sql.*;

public class SalesDAO {
    public static double getTotalSales() {
        return getAggregate("SUM(price)");
    }

    public static double getMaxSale() {
        return getAggregate("MAX(price)");
    }

    public static double getMinSale() {
        return getAggregate("MIN(price)");
    }

    public static double getAverageSale() {
        return getAggregate("AVG(price)");
    }

    public static int getTotalItemsSold() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM sales";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static double getAggregate(String function) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT " + function + " FROM sales";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void recordSale(String itemName, double price) {
        if (itemName == null || itemName.isEmpty()) {
            itemName = "Multiple Items";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO sales (item_name, price) VALUES (?, ?)")) {

            stmt.setString(1, itemName);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
