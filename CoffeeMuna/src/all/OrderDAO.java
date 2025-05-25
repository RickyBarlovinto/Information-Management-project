package all;

import java.sql.*;

public class OrderDAO {
    public static int addOrder(String orderDate, double totalPrice) {
        String sql = "INSERT INTO Orders (OrderDate, TotalPrice) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(orderDate));
            stmt.setDouble(2, totalPrice);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
