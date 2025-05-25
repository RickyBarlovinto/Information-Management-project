package all;

public class MenuItem {
    private int id;
    private String name;
    private String category;
    private double price;

    public MenuItem(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public MenuItem(String name, String category, double price) {
        this(-1, name, category, price);
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
}

