package all;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public Dashboard() {
        setTitle("CoffeeMuna Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(77, 51, 25));
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(102, 51, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("CoffeeMuna Dashboard");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JLabel imageLabel = new JLabel(new ImageIcon("src/all/welcome_bg.jpg"));
        imageLabel.setPreferredSize(new Dimension(100, 60));

        headerPanel.add(imageLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price"}, 0);
        table = new JTable(model);
        table.setBackground(new Color(255, 248, 220));
        table.setForeground(new Color(60, 30, 10));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(24);

        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton sellBtn = new JButton("Sell");
        JButton viewSalesBtn = new JButton("View Sales");

        JButton[] buttons = {addBtn, editBtn, deleteBtn, sellBtn, viewSalesBtn};
        for (JButton btn : buttons) {
            btn.setBackground(new Color(153, 102, 51));
            btn.setForeground(Color.WHITE);
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(102, 51, 0));
        for (JButton btn : buttons) controlPanel.add(btn);
        add(controlPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showForm(null));

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                MenuItem item = new MenuItem(
                        (int) model.getValueAt(row, 0),
                        (String) model.getValueAt(row, 1),
                        (String) model.getValueAt(row, 2),
                        (Double) model.getValueAt(row, 3)
                );
                showForm(item);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete this item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) model.getValueAt(row, 0);
                    MenuItemDAO.deleteItem(id);
                    refreshTable();
                }
            }
        });

        sellBtn.addActionListener(e -> sellSelectedItem());
        viewSalesBtn.addActionListener(e -> showSalesSummary());

        setVisible(true);
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<MenuItem> items = MenuItemDAO.getAllItems();
        for (MenuItem item : items) {
            model.addRow(new Object[]{item.getId(), item.getName(), item.getCategory(), item.getPrice()});
        }
    }

    private void showForm(MenuItem item) {
        JTextField nameField = new JTextField(item != null ? item.getName() : "");
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Coffee", "Tea", "Non-Coffee"});
        JTextField priceField = new JTextField(item != null ? String.valueOf(item.getPrice()) : "");

        if (item != null) categoryBox.setSelectedItem(item.getCategory());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, panel, item == null ? "Add Item" : "Edit Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String category = (String) categoryBox.getSelectedItem();
            double price = Double.parseDouble(priceField.getText());

            if (item == null) {
                MenuItemDAO.addItem(new MenuItem(name, category, price));
            } else {
                item.setName(name);
                item.setCategory(category);
                item.setPrice(price);
                MenuItemDAO.updateItem(item);
            }
            refreshTable();
        }
    }

    private void sellSelectedItem() {
        List<MenuItem> items = MenuItemDAO.getAllItems();
        List<String> orderSummary = new ArrayList<>();
        double totalSale = 0;

        boolean continueOrder = true;

        while (continueOrder) {
            String[] itemNames = items.stream()
                    .map(MenuItem::getName)
                    .map(name -> name.split(" - ")[0])
                    .distinct()
                    .toArray(String[]::new);

            String selectedItem = (String) JOptionPane.showInputDialog(
                    this,
                    "Select an item to sell:",
                    "Sell Item",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    itemNames,
                    itemNames.length > 0 ? itemNames[0] : null
            );

            if (selectedItem == null) return;

            String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity for " + selectedItem + ":");
            if (qtyStr == null) return;

            int quantity;
            try {
                quantity = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            MenuItem item = items.stream()
                    .filter(i -> i.getName().split(" - ")[0].equals(selectedItem))
                    .findFirst()
                    .orElse(null);

            if (item == null) continue;

            JCheckBox sugar = new JCheckBox("Add Sugar (₱10)");
            JCheckBox syrup = new JCheckBox("Add Syrup (₱10)");

            JPanel options = new JPanel(new GridLayout(0, 1));
            options.add(new JLabel("Add-ons:"));
            options.add(sugar);
            options.add(syrup);

            int result = JOptionPane.showConfirmDialog(this, options, "Add-ons",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) return;

            double itemPrice = item.getPrice();
            if (sugar.isSelected()) itemPrice += 10;
            if (syrup.isSelected()) itemPrice += 10;

            double subtotal = itemPrice * quantity;
            totalSale += subtotal;

            String addOns = "";
            if (sugar.isSelected()) addOns += "+Sugar ";
            if (syrup.isSelected()) addOns += "+Syrup ";

            orderSummary.add(quantity + "x " + selectedItem + " " + addOns + "= ₱" + String.format("%.2f", subtotal));

            int more = JOptionPane.showConfirmDialog(this, "Do you want to add another order?", "Continue Order", JOptionPane.YES_NO_OPTION);
            continueOrder = (more == JOptionPane.YES_OPTION);
        }

        StringBuilder summary = new StringBuilder("========= CoffeeMuna Receipt =========\n");
        for (String line : orderSummary) summary.append(line).append("\n");
        summary.append("--------------------------------------\n");
        summary.append("TOTAL: ₱").append(String.format("%.2f", totalSale)).append("\n");

        String cashStr = JOptionPane.showInputDialog(this, "Enter cash amount:");
        try {
            double cash = Double.parseDouble(cashStr);
            double change = cash - totalSale;

            if (change < 0) {
                JOptionPane.showMessageDialog(this, "Insufficient amount. Please add ₱" + Math.abs(change));
                return;
            }

            summary.append("CASH: ₱").append(String.format("%.2f", cash)).append("\n");
            summary.append("CHANGE: ₱").append(String.format("%.2f", change)).append("\n");
            summary.append("\nThank you for ordering at CoffeeMuna!");

            JTextArea receiptArea = new JTextArea(summary.toString());
            receiptArea.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(receiptArea), "Customer Receipt", JOptionPane.INFORMATION_MESSAGE);

            SalesDAO.recordSale("Multiple Items", totalSale);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid cash input.");
        }
    }

    private void showSalesSummary() {
        double total = SalesDAO.getTotalSales();
        double min = SalesDAO.getMinSale();
        double max = SalesDAO.getMaxSale();
        double avg = SalesDAO.getAverageSale();
        int count = SalesDAO.getTotalItemsSold();

        String summary = String.format("""
                Total Sales: ₱%.2f
                Min Sale: ₱%.2f
                Max Sale: ₱%.2f
                Average Sale: ₱%.2f
                Total Items Sold: %d
                """, total, min, max, avg, count);

        JOptionPane.showMessageDialog(this, summary, "Sales Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
