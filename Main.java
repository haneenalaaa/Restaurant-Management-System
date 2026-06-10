///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package RestaurantSystem1;
//
//
///**
// *
// * @author Mega
// * @author Part2-Dev
// *
// */
//
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
//
//
//public class Main {
//
//    // مصفوفات لتمثيل قاعدة بيانات النظام 
//    private static ArrayList<User> users = new ArrayList<>();
//    private static ArrayList<Table> tables = new ArrayList<>();
//    private static ArrayList<MenuItem> menu = new ArrayList<>();
//    private static ArrayList<Reservation> reservations = new ArrayList<>();
//    private static ArrayList<Order> orders = new ArrayList<>();
//    
//    private static User currentUser = null;
//    private static Scanner scanner = new Scanner(System.in);
//
//    
//    public static void main(String[] args) {
//        initializeData(); // تجهيز بيانات تجريبية
//        
//        System.out.println("--- Welcome to Restaurant Management System ---");
//        
//        while (true) {
//            if (currentUser == null) {
//                showStartMenu();
//            } else if (currentUser instanceof Admin) {
//                showAdminMenu();
//            } else if (currentUser instanceof Customer) {
//                showCustomerMenu();
//            }
//        }
//    }
//
//    // 1. القائمة الابتدائية (تسجيل الدخول وإنشاء حساب)
//    private static void showStartMenu() {
//        System.out.println("\n1. Login\n2. Register (Customer)\n3. Exit");
//        System.out.print("Select choice: ");
//        int choice = scanner.nextInt();
//        scanner.nextLine(); // clear buffer
//
//        switch (choice) {
//            case 1: login(); break;
//            case 2: register(); break;
//            case 3: System.exit(0);
//        }
//    }
//
//    private static void login() {
//        System.out.print("Username: ");
//        String user = scanner.nextLine();
//        System.out.print("Password: ");
//        String pass = scanner.nextLine();
//
//        for (User u : users) {
//            if (u.login(user, pass)) { // استخدام Polymorphism للـ Login
//                currentUser = u;
//                System.out.println("Login successful! Welcome " + u.getUsername());
//                return;
//            }
//        }
//        System.out.println("Invalid credentials.");
//    }
//
//    private static void register() {
//        System.out.print("Enter new username: ");
//        String user = scanner.nextLine();
//        System.out.print("Enter new password: ");
//        String pass = scanner.nextLine();
//        users.add(new Customer(user, pass));
//        System.out.println("Registration successful. You can login now.");
//    }
//
//    // 2. واجهة الـ Admin (إدارة القائمة والترابيزات)
//    private static void showAdminMenu() {
//        System.out.println("\n--- Admin Dashboard ---");
//        System.out.println("1. View All Reservations\n2. View All Orders\n3. Add Menu Item\n4. Logout");
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//
//        switch (choice) {
//            case 1:
//                for (Reservation r : reservations) r.display();
//                break;
//            case 2:
//                for (Order o : orders) o.display();
//                break;
//            case 3:
//                System.out.print("Name: "); String n = scanner.nextLine();
//                System.out.print("Price: "); double p = scanner.nextDouble();
//                scanner.nextLine();
//                menu.add(new MenuItem(n, "Admin added", p, "Main"));
//                System.out.println("Item added to menu.");
//                break;
//            case 4: currentUser = null; break;
//        }
//    }
//
//    // 3. واجهة الـ Customer (حجز، طلب طعام، إلغاء)
//    private static void showCustomerMenu() {
//        System.out.println("\n--- Customer Menu ---");
//        System.out.println("1. Make Reservation\n2. Cancel Reservation\n3. Place Order\n4. View My Info\n5. Logout");
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//
//        switch (choice) {
//            case 1: makeReservation(); break;
//            case 2: cancelReservation(); break;
//            case 3: placeOrder(); break;
//            case 4: currentUser.display(); break;
//            case 5: currentUser = null; break;
//        }
//    }
//
//    private static void makeReservation() {
//        System.out.println("Available Tables:");
//        for (Table t : tables) {
//            if (t.isAvailable()) t.display();
//        }
//        System.out.print("Enter Table Number: ");
//        int tNum = scanner.nextInt();
//        System.out.print("Guests Count: ");
//        int guests = scanner.nextInt();
//        scanner.nextLine();
//
//        for (Table t : tables) {
//            if (t.getTableNumber() == tNum && t.isAvailable()) {
//                Reservation res = new Reservation(reservations.size()+1, (Customer)currentUser, t, "2026-05-01", "19:00", guests);
//                if (res.isValidGuestCount()) {
//                    reservations.add(res);
//                    System.out.println("Reservation confirmed!");
//                } else {
//                    System.out.println("Table capacity too small!");
//                    t.free();
//                }
//                return;
//            }
//        }
//    }
//
//    private static void cancelReservation() {
//        System.out.print("Enter Reservation ID to cancel: ");
//        int id = scanner.nextInt();
//        for (Reservation r : reservations) {
//            if (r.getReservationId() == id && r.getCustomer().equals(currentUser)) {
//                r.cancel();
//                return;
//            }
//        }
//        System.out.println("Reservation not found.");
//    }
//
//    private static void placeOrder() {
//        Order newOrder = new Order(orders.size() + 1, (Customer)currentUser);
//        System.out.println("Menu:");
//        for (int i = 0; i < menu.size(); i++) {
//            System.out.println((i+1) + ". " + menu.get(i));
//        }
//        System.out.println("Enter item numbers to add (0 to finish):");
//        while (true) {
//            int itemChoice = scanner.nextInt();
//            if (itemChoice == 0) break;
//            if (itemChoice > 0 && itemChoice <= menu.size()) {
//                newOrder.addItem(menu.get(itemChoice-1));
//            }
//        }
//        if (!newOrder.isEmpty()) {
//            orders.add(newOrder);
//            newOrder.display();
//        }
//    }
//
//    private static void initializeData() {
//        // إضافة مستخدمين (Admin و Customer)
//        users.add(new Admin("admin", "123"));
//        users.add(new Customer("omar", "pass"));
//        
//        // إضافة ترابيزات [cite: 40]
//        tables.add(new Table(1, 2));
//        tables.add(new Table(2, 4));
//        tables.add(new Table(3, 8));
//        
//        // إضافة أصناف للمنيو [cite: 48]
//        menu.add(new MenuItem("Pizza", "Italian Cheese Pizza", 150.0, "Main"));
//        menu.add(new MenuItem("Pepsi", "Cold Soft Drink", 30.0, "Drink"));
//    }
//}
//
//    
//
//    
//
