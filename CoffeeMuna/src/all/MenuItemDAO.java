package all;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    public static List<MenuItem> getAllItems() {
        List<MenuItem> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM MenuItems";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("MenuItemID");
                String name = rs.getString("Name");
                String category = rs.getString("Category");
                double price = rs.getDouble("BasePrice");
                items.add(new MenuItem(id, name, category, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void addItem(MenuItem item) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO MenuItems (Name, Category, BasePrice) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateItem(MenuItem item) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE MenuItems SET Name = ?, Category = ?, BasePrice = ? WHERE MenuItemID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM MenuItems WHERE MenuItemID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
