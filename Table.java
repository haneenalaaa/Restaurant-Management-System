//package RestaurantSystem1;
//
///**
// * Represents a table in the restaurant.
// * Each table has a number, seating capacity, and availability status.
// *
// * @author Part2-Dev
// */
//public class Table {
//
//    // -------- Fields --------
//    private int tableNumber;    // Unique table number
//    private int capacity;       // Max number of guests this table fits
//    private boolean available;  // true = table is free, false = table is occupied
//
//    // -------- Constructor --------
//
//    /**
//     * Creates a new Table. All tables start as available by default.
//     *
//     * @param tableNumber Unique identifier for the table
//     * @param capacity    Max number of guests
//     */
//    public Table(int tableNumber, int capacity) {
//        this.tableNumber = tableNumber;
//        this.capacity = capacity;
//        this.available = true; // tables are free when first created
//    }
//
//        // -------- Getters & Setters --------
//
//    public void setTableNumber(int tableNumber) {
//        this.tableNumber = tableNumber;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public void setAvailable(boolean available) {
//        this.available = available;
//    }
//
//    /** Returns the table number */
//    public int getTableNumber() {
//        return tableNumber;
//    }
//
//    /** Returns the seating capacity of the table */
//    public int getCapacity() {
//        return capacity;
//    }
//
//    /** Returns true if the table is currently available */
//    public boolean isAvailable() {
//        return available;
//    }
//
//    /**
//     * Marks the table as occupied (available = false).
//     * Called when a reservation is made.
//     */
//    public void occupy() {
//        this.available = false;
//    }
//
//    /**
//     * Marks the table as free again (available = true).
//     * Called when a reservation is cancelled or customer leaves.
//     */
//    public void free() {
//        this.available = true;
//    }
//
//    // -------- Display --------
//
//    /**
//     * Prints table info to the console.
//     */
//    public void display() {
//        System.out.println("Table #" + tableNumber
//                + " | Capacity: " + capacity
//                + " | Status: " + (available ? "Available" : "Occupied"));
//    }
//
//    /**
//     * Returns a string representation of the table.
//     * @return 
//     */
//    @Override
//    public String toString() {
//        return "Table #" + tableNumber + " (Capacity: " + capacity + ")";
//    }
//}
