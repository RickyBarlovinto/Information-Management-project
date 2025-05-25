package all;



import java.sql.*;

public class AddOnDAO {
    public static void addAddOn(String name, double price) {
        String sql = "INSERT INTO AddOns (Name, Price) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
