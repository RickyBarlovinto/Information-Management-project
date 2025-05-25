package all;

import java.sql.*;

public class OrderAddOnDAO {
    public static void addOrderAddOn(int orderDetailId, int addOnId) {
        String sql = "INSERT INTO OrderAddOns (OrderDetailID, AddOnID) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetailId);
            stmt.setInt(2, addOnId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
