package all;

import java.sql.*;

public class OrderDetailDAO {
    public static int addOrderDetail(int orderId, int menuItemId, int quantity) {
        String sql = "INSERT INTO OrderDetails (OrderID, MenuItemID, Quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, menuItemId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
