package advancedproj;

public class MenuItem {

    private String name;
    private String description;
    private double price;
    private String category;


    public MenuItem(String name, String description, double price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

  
    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = "General"; // Default category
    }

   
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

 
    @Override
    public String toString() {
        return name + " (" + price + " EGP)";
    }
}