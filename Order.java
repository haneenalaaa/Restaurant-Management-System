package advancedproj;

import java.util.ArrayList;

public class Order implements Reportable{
    private int orderId;
    private Customer customer;//association 
    private ArrayList<MenuItem> items; //aggregation 
    private String status;

    // Constructor lel-Database (4 columns)
   // Gowwa class Order.java
public Order(String id, String customerName, String itemsString, String status, ArrayList<MenuItem> masterMenu) {
    this.orderId = Integer.parseInt(id);
    this.customer = new Customer(customerName, "");
    this.status = status;
    this.items = new ArrayList<>();

    if (itemsString != null && !itemsString.isEmpty()) {
        String[] names = itemsString.split(",");
        for (String n : names) {
            String cleanName = n.trim();
            for (MenuItem menuObj : masterMenu) {
                if (menuObj.getName().equalsIgnoreCase(cleanName)) {
                    this.items.add(menuObj); // Re-using the SAME object
                    break; 
                }
            }
        }
    }
}

      
public double calculateTotal() {
    double total = 0;
    for (MenuItem item : items) { // Items de el-ArrayList beta3tek
        total += item.getPrice(); // Bi-ygeeb se3r kol akla we y-gma3ha
    }
    return total;
}

// Ghayary esm el-function aw zawedy de gowwa class Order
public double getTotalPrice() {
    return calculateTotal(); 
}
    public String getId() { return String.valueOf(orderId); }

    public String getCustomerName() { 
        return (customer != null) ? customer.getUsername() : "N/A"; 
    }

    // De mohemma giddan 3ashan el-ArrayList tez-har fel-Table
    public String getItemsDisplay() {
        if (items == null || items.isEmpty()) return "No items";
        StringBuilder sb = new StringBuilder();
        for (MenuItem m : items) {
            sb.append(m.getName()).append(" "); // Ben-geeb el-asm men el-ArrayList
        }
        return sb.toString();
    }
    

    public String getStatus() { return status; }

    // Your original functions (addItem, etc.)
    public void addItem(MenuItem item) { items.add(item); }
    
     @Override
    public String generateReport() {
        return "--------------------------\n" +
               "      RESTO ORDER INFO    \n" +
               "--------------------------\n" +
               "Date: " + getFormattedDate() + "\n" + // ندينا ميثود الـ Interface
               "Order ID: #" + orderId + "\n" +
               "Customer: " + getCustomerName() + "\n" +
               "Total Amount: " + calculateTotal() + " EGP\n" +
               "--------------------------";
}}