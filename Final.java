package advancedproj;

import java.sql.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class: Final
 * Purpose: Restaurant Management System - Main Application
 */
public class Final extends Application {

    // --- Global Variables ---
    private boolean[] tablesStatus = new boolean[11];
    private ArrayList<MenuItem> allMenuItems = new ArrayList<>();
    private User currentUser = null; 
    private PreparedStatement ps = null;
    private Connection con = null;

    @Override
    public void start(Stage primaryStage) {

        // ==========================================
        // 1. INITIAL DATA LOADING (FROM DATABASE)
        // ==========================================
        
        // Load Reserved Tables
        try {
            con = DB.dbconn();
            if (con != null) {
                String sqlCheck = "SELECT TABLE_ID FROM RESTAURANTSYSTEM.RESERVATION";
                ps = con.prepareStatement(sqlCheck);
                ResultSet rsCheck = ps.executeQuery();
                while (rsCheck.next()) {
                    int id = rsCheck.getInt(1);
                    if (id >= 1 && id <= 10) {
                        tablesStatus[id] = true;
                    }
                }
                rsCheck.close(); ps.close(); con.close();
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load table status: " + e.getMessage());
        }

        // Load Menu Items
        try {
            con = DB.dbconn();
            if (con != null) {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM RESTAURANTSYSTEM.MENUITEMS");
                while (rs.next()) {
                    allMenuItems.add(new MenuItem(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)));
                }
                rs.close(); con.close();
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load menu items: " + e.getMessage());
        }

        // ==========================================
        // 2. LOGIN SCENE (sin)
        // ==========================================
        Label L = new Label("Welcome to Restaurant Management System");
        Label ln = new Label("UserName:");
        Label lp = new Label("Password:");
        TextField tn = new TextField();
        PasswordField tp = new PasswordField();
        Button in = new Button("LOGIN");
        Button up1 = new Button("SIGNUP");

        GridPane gin = new GridPane();
        gin.add(L, 0, 0, 2, 1);
        gin.add(ln, 0, 2); gin.add(tn, 1, 2);
        gin.add(lp, 0, 3); gin.add(tp, 1, 3);
        gin.add(in, 0, 4); gin.add(up1, 1, 4, 2, 1);

        gin.setVgap(10); gin.setHgap(10);
        gin.setAlignment(Pos.CENTER);
        Scene sin = new Scene(gin, 800, 600);

        // ==========================================
        // 3. ADMIN SCENE (sadm)
        // ==========================================
        Button dis = new Button("display Reservations");
        Button or = new Button("display Order");
        Button menu = new Button("display Menu");
        Button add = new Button("Add item");
        Button delete = new Button("Delete item");
        Button out = new Button("Logout");
        Button chatAdmin = new Button("Customer Support (Chat)");


chatAdmin.setOnAction(e -> {
    openChatWindow("Admin", true);
});

        GridPane gadm = new GridPane();
        gadm.add(dis, 0, 0); gadm.add(menu, 1, 0);
        gadm.add(or, 0, 1);  gadm.add(add, 1, 1);
        gadm.add(delete, 0, 2); gadm.add(out, 1, 2);
        gadm.add(chatAdmin, 0, 3); 
        
        gadm.setAlignment(Pos.CENTER);
        gadm.setHgap(20); gadm.setVgap(20);
        Scene sadm = new Scene(gadm, 800, 600);

        
        chatAdmin.setOnAction(e -> {
    openChatWindow("Admin", true);
});
        // --- Admin Action: Display Reservations ---
        dis.setOnAction((eres) -> {
            for (int i = 0; i < tablesStatus.length; i++) tablesStatus[i] = false;
            TableView<Reservation> table = new TableView<>();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            TableColumn<Reservation, String> c1 = new TableColumn<>("Phone");
            c1.setCellValueFactory(new PropertyValueFactory<>("phone"));
            TableColumn<Reservation, String> c2 = new TableColumn<>("Name");
            c2.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<Reservation, String> c3 = new TableColumn<>("Table ID");
            c3.setCellValueFactory(new PropertyValueFactory<>("tableId"));
            TableColumn<Reservation, String> c4 = new TableColumn<>("Time");
            c4.setCellValueFactory(new PropertyValueFactory<>("time"));
            TableColumn<Reservation, String> c5 = new TableColumn<>("Guests");
            c5.setCellValueFactory(new PropertyValueFactory<>("guestNum"));
            TableColumn<Reservation, String> c6 = new TableColumn<>("Date");
            c6.setCellValueFactory(new PropertyValueFactory<>("date"));

            table.getColumns().addAll(c1, c2, c3, c4, c5, c6);
            ObservableList<Reservation> data = FXCollections.observableArrayList();
            
            try {
                con = DB.dbconn();
                ps = con.prepareStatement("SELECT * FROM RESTAURANTSYSTEM.RESERVATION");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String tid = rs.getString(3);
                    try {
                        int tableNum = Integer.parseInt(tid);
                        if (tableNum >= 1 && tableNum <= 10) tablesStatus[tableNum] = true;
                    } catch (Exception e) {}
                    data.add(new Reservation(rs.getString(1), rs.getString(2), tid, rs.getString(4), rs.getString(5), rs.getString(6)));
                }
                table.setItems(data);
                rs.close(); ps.close(); con.close();
            } catch (SQLException ex) {
                showErrorAlert("Database Error", ex.getMessage());
            }

            int reservedCount = 0;
            for (boolean b : tablesStatus) if (b) reservedCount++;
            Label info = new Label("Occupied Tables: " + reservedCount + " / 10");
            info.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Button back = new Button("Back");
            back.setOnAction(e -> primaryStage.setScene(sadm));
            VBox v = new VBox(15, info, table, back);
            v.setPadding(new Insets(20)); v.setAlignment(Pos.CENTER);
            primaryStage.setScene(new Scene(v, 800, 600));
        });

        // --- Admin Action: Display Orders ---
        or.setOnAction((eor) -> {
            TableView<Order> tableOr = new TableView<>();
            tableOr.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            TableColumn<Order, String> c1 = new TableColumn<>("Order ID");
            c1.setCellValueFactory(new PropertyValueFactory<>("id"));
            TableColumn<Order, String> c2 = new TableColumn<>("Customer");
            c2.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            TableColumn<Order, String> c3 = new TableColumn<>("Items");
            c3.setCellValueFactory(new PropertyValueFactory<>("itemsDisplay"));
            TableColumn<Order, String> c4 = new TableColumn<>("Status");
            c4.setCellValueFactory(new PropertyValueFactory<>("status"));
            TableColumn<Order, Double> colTotal = new TableColumn<>("Total Price");
            colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

            tableOr.getColumns().addAll(c1, c2, c3, c4, colTotal);
            ObservableList<Order> data = FXCollections.observableArrayList();
            
            try {
                con = DB.dbconn();
                ps = con.prepareStatement("SELECT * FROM RESTAURANTSYSTEM.ORDER_INFO");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), allMenuItems));
                }
                tableOr.setItems(data);
                rs.close(); ps.close(); con.close();
            } catch (SQLException ex) {
                showErrorAlert("Database Error", ex.getMessage());
            }

            Button back = new Button("Back");
            back.setOnAction(e -> primaryStage.setScene(sadm));
            VBox v = new VBox(10, new Label("Orders List"), tableOr, back);
            v.setAlignment(Pos.CENTER); v.setPadding(new Insets(20));
            Scene s = new Scene(v, 800, 600);
            s.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
            primaryStage.setScene(s);
        });

        // --- Admin Action: Display Menu ---
        menu.setOnAction((em) -> {
            TableView<MenuItem> tableMenu = new TableView<>();
            TableColumn<MenuItem, String> colName = new TableColumn<>("Item Name");
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<MenuItem, String> colDesc = new TableColumn<>("Description");
            colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            TableColumn<MenuItem, Double> colPrice = new TableColumn<>("Price (EGP)");
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn<MenuItem, String> colCat = new TableColumn<>("Category");
            colCat.setCellValueFactory(new PropertyValueFactory<>("category"));

            tableMenu.getColumns().addAll(colName, colDesc, colPrice, colCat);
            ObservableList<MenuItem> menuItemsList = FXCollections.observableArrayList();
            
            try {
                con = DB.dbconn();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM RESTAURANTSYSTEM.MENUITEMS");
                while (rs.next()) {
                    menuItemsList.add(new MenuItem(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)));
                }
                tableMenu.setItems(menuItemsList);
                rs.close(); con.close();
            } catch (SQLException ex) {
                showErrorAlert("Database Error", ex.getMessage());
            }

            Button btnBack = new Button("Back to Admin");
            btnBack.setOnAction(e -> primaryStage.setScene(sadm));
            VBox layout = new VBox(15, new Label("Restaurant Menu Management"), tableMenu, btnBack);
            layout.setPadding(new Insets(20)); layout.setAlignment(Pos.CENTER);
            Scene menuScene = new Scene(layout, 800, 600);
            menuScene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
            primaryStage.setScene(menuScene);
        });

        // --- Admin Action: Add Item ---
        add.setOnAction((eadd) -> {
            Label lblMName = new Label("Item Name:"); Label lblMDesc = new Label("Description:");
            Label lblMPrice = new Label("Price:"); Label lblMCat = new Label("Category:");
            TextField txtMName = new TextField(); TextField txtMDesc = new TextField();
            TextField txtMPrice = new TextField(); TextField txtMCat = new TextField();
            Button btnAddMenu = new Button("Add Item");

            GridPane gMenu = new GridPane();
            gMenu.setAlignment(Pos.CENTER); gMenu.setHgap(10); gMenu.setVgap(10);
            gMenu.add(lblMName, 0, 0); gMenu.add(txtMName, 1, 0);
            gMenu.add(lblMDesc, 0, 1); gMenu.add(txtMDesc, 1, 1);
            gMenu.add(lblMPrice, 0, 2); gMenu.add(txtMPrice, 1, 2);
            gMenu.add(lblMCat, 0, 3); gMenu.add(txtMCat, 1, 3);
            gMenu.add(btnAddMenu, 1, 4);

            Scene sceneMenu = new Scene(gMenu, 800, 600);
            sceneMenu.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
            primaryStage.setScene(sceneMenu);

            btnAddMenu.setOnAction((eadd1) -> {
                try {
                    con = DB.dbconn();
                    ps = con.prepareStatement("Insert into RESTAURANTSYSTEM.MENUITEMS values(?,?,?,?)");
                    ps.setString(1, txtMName.getText());
                    ps.setString(2, txtMDesc.getText());
                    ps.setString(3, txtMPrice.getText());
                    ps.setString(4, txtMCat.getText());
                    if (ps.executeUpdate() == 1) showInfoAlert("Success", "Data inserted successfully");
                    ps.close(); con.close();
                } catch (SQLException ex2) {
                    showErrorAlert("Insert Error", ex2.getMessage());
                }
                primaryStage.setScene(sadm);
            });
        });

        // --- Admin Action: Delete Item ---
        delete.setOnAction((edel) -> {
            Label name = new Label("Name of item :");
            TextField tname = new TextField();
            Button del = new Button("Delete");
            GridPane gdel = new GridPane();
            gdel.add(name, 0, 0); gdel.add(tname, 1, 0); gdel.add(del, 0, 1);
            gdel.setAlignment(Pos.CENTER); gdel.setHgap(10); gdel.setVgap(10);
            
            Scene scdel = new Scene(gdel, 800, 600);
            scdel.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
            primaryStage.setScene(scdel);

            del.setOnAction((edel2) -> {
    try {
        con = DB.dbconn();
        ps = con.prepareStatement("Delete from RESTAURANTSYSTEM.MENUITEMS Where NAME = ?");
        ps.setString(1, tname.getText());

   
        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            showInfoAlert("Success", "Item deleted successfully!");
        } else {
           
            showErrorAlert("Delete Error", "Item name not found in the menu.");
        }

        ps.close(); con.close();
    } catch (SQLException exd) {
        showErrorAlert("Delete Error", exd.getMessage());
    }
    primaryStage.setScene(sadm);
});
        });

        out.setOnAction((eout) -> primaryStage.setScene(sin));

        // ==========================================
        // 4. CUSTOMER SCENE (sc)
        // ==========================================
        GridPane gc = new GridPane();
        Scene sc = new Scene(gc, 800, 600);

        Button res1 = new Button("Make Reservation");
        Button or1 = new Button("Place Order");
        Button cancel1 = new Button("cancel Reservation");
        Button menuc = new Button("display Menu");
        Button out1 = new Button("Logout");
        // Inside CUSTOMER SCENE (sc) section
Button chatCustomer = new Button("Chat with Admin");



        gc.add(res1, 0, 0); gc.add(or1, 1, 0);
        gc.add(cancel1, 1, 1); gc.add(menuc, 0, 2);
        gc.add(out1, 1, 2);
        gc.add(chatCustomer, 0, 3); // Add to the grid
        gc.setAlignment(Pos.CENTER); gc.setHgap(20); gc.setVgap(20);
chatCustomer.setOnAction(e -> {
    // Replace "Customer" with the actual logged-in name if available
    openChatWindow("Customer", false);
});

        // --- Customer Action: Make Reservation ---
        res1.setOnAction((eres) -> {
            Label l1 = new Label("Phone number"); Label l2 = new Label("Name");
            Label l3 = new Label("Table_Id"); Label l4 = new Label("Date");
            Label l5 = new Label("Time"); Label l6 = new Label("Number of gest");
            TextField t1 = new TextField(); TextField t2 = new TextField();
            TextField t3 = new TextField(); TextField d = new TextField();
            TextField t4 = new TextField(); TextField t5 = new TextField();
            d.setPromptText("YYYY-MM-DD"); t4.setPromptText("HH:MM");
            Button b1 = new Button("confirm");

            GridPane gr = new GridPane();
            gr.add(l1, 0, 0); gr.add(t1, 1, 0); gr.add(l2, 0, 1); gr.add(t2, 1, 1);
            gr.add(l3, 0, 2); gr.add(t3, 1, 2); gr.add(l4, 0, 3); gr.add(d, 1, 3);
            gr.add(l5, 0, 4); gr.add(t4, 1, 4); gr.add(l6, 0, 5); gr.add(t5, 1, 5);
            gr.add(b1, 0, 6, 2, 1);
            gr.setAlignment(Pos.CENTER); gr.setVgap(10); gr.setHgap(10);

            b1.setOnAction((ecom) -> {
                try {
                    int tableNum = Integer.parseInt(t3.getText());
                    if (tableNum >= 1 && tableNum <= 10 && tablesStatus[tableNum]) {
                        showErrorAlert("Table Occupied", "Table " + tableNum + " is already reserved.");
                        return;
                    }
                    con = DB.dbconn();
                    ps = con.prepareStatement("Insert into RESTAURANTSYSTEM.Reservation values(?,?,?,?,?,?)");
                    ps.setString(1, t1.getText()); ps.setString(2, t2.getText());
                    ps.setString(3, t3.getText()); ps.setString(4, t4.getText());
                    ps.setString(5, t5.getText()); ps.setString(6, d.getText());
                    Reservation res = new Reservation(t1.getText(), t2.getText(), t3.getText(), t4.getText(), t5.getText(), d.getText());

                    if (ps.executeUpdate() == 1) {
                        tablesStatus[tableNum] = true;
                         showInfoAlert("Success", res.generateReport());
                        primaryStage.setScene(sc);
                    }
                    ps.close(); con.close();
                } catch (NumberFormatException nfe) {
                    showErrorAlert("Input Error", "Please enter a valid table number.");
                } catch (SQLException ex2) {
                    showErrorAlert("Database Error", ex2.getMessage());
                }
            });
            Scene s = new Scene(gr, 600, 400, Color.AQUAMARINE);
            s.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
            primaryStage.setScene(s);
        });

        // --- Customer Action: Place Order ---
        or1.setOnAction((eor) -> {
    // العناصر المرئية
    TextField txtId = new TextField(); 
    TextField txtItems = new TextField(); // اليوزر بيكتب أسماء الأصناف مفصولة بفاصلة (مثلاً: Pizza, Pasta)
    txtItems.setPromptText("Enter items: Pizza, Pasta...");
    Button btnConfirm = new Button("Confirm Order");

    GridPane grid = new GridPane();
    grid.add(new Label("Order ID:"), 0, 0); grid.add(txtId, 1, 0);
    grid.add(new Label("Items (comma separated):"), 0, 1); grid.add(txtItems, 1, 1);
    grid.add(btnConfirm, 0, 2, 2, 1);
    grid.setAlignment(Pos.CENTER); grid.setHgap(10); grid.setVgap(15);

    btnConfirm.setOnAction((ecom) -> {
        try {
            String orderId = txtId.getText();
            String itemsStr = txtItems.getText();
            
            String customerName = (currentUser!= null) ? currentUser.getUsername() : "Guest";


            Order newOrder = new Order(orderId, customerName, itemsStr, "Pending", allMenuItems);
            
            con = DB.dbconn();
            ps = con.prepareStatement("Insert into RESTAURANTSYSTEM.ORDER_INFO values(?,?,?,?)");
            ps.setString(1, orderId); 
            ps.setString(2, customerName);
            ps.setString(3, itemsStr); 
            ps.setString(4, "Pending");
            ps.executeUpdate();
            ps.close(); con.close();

            showInfoAlert("Order Placed Successfully", newOrder.generateReport());

            primaryStage.setScene(sc); 
        } catch (SQLException ex) { 
            showErrorAlert("Database Error", ex.getMessage()); 
        } catch (Exception ex) {
            showErrorAlert("Input Error", "Please check your order data.");
        }
    });

    Scene sor = new Scene(grid, 600, 400, Color.AQUAMARINE);
    sor.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
    primaryStage.setScene(sor);
});

        // --- Customer Action: Cancel Reservation ---
        cancel1.setOnAction((ecancel) -> {
            TextField tite = new TextField(); Button bite = new Button("Cancel");
            GridPane gcan = new GridPane();
            gcan.add(new Label("phone number"), 0, 0); gcan.add(tite, 1, 0); gcan.add(bite, 0, 1);
            gcan.setAlignment(Pos.CENTER); gcan.setVgap(20); gcan.setHgap(20);
            
            bite.setOnAction((ecom) -> {
                try {
                    con = DB.dbconn();
                    ps = con.prepareStatement("Delete from RESTAURANTSYSTEM.Reservation Where PHONE_NUMBER = ?");
                    ps.setString(1, tite.getText());
                    ps.executeUpdate();
                    ps.close(); con.close();
                    primaryStage.setScene(sc);
                } catch (SQLException exd) { showErrorAlert("Delete Error", exd.getMessage()); }
            });
            primaryStage.setScene(new Scene(gcan, 600, 400, Color.AQUAMARINE));
        });

        menuc.setOnAction((em) -> {
   
        TableView<MenuItem> tableMenu = new TableView<>();
        TableColumn<MenuItem, String> colName = new TableColumn<>("Item Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<MenuItem, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<MenuItem, Double> colPrice = new TableColumn<>("Price (EGP)");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<MenuItem, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(new PropertyValueFactory<>("category"));

        tableMenu.getColumns().addAll(colName, colDesc, colPrice, colCat);
        ObservableList<MenuItem> menuItemsList = FXCollections.observableArrayList();
    
    
    try {
        con = DB.dbconn();
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM RESTAURANTSYSTEM.MENUITEMS");
        while (rs.next()) {
            
            menuItemsList.add(new MenuItem(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)));
        }
        tableMenu.setItems(menuItemsList);
        rs.close(); con.close();
    } catch (SQLException ex) {
        showErrorAlert("Database Error", ex.getMessage());
    }


    Button btnBack = new Button("Back to Dashboard");
    btnBack.setOnAction(e -> primaryStage.setScene(sc)); 

   
    VBox layout = new VBox(15, new Label("Restaurant Menu"), tableMenu, btnBack);
    layout.setPadding(new Insets(20)); layout.setAlignment(Pos.CENTER);
    Scene menuScene = new Scene(layout, 800, 600);
    
   
    try {
        menuScene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
    } catch (Exception e) {
        System.out.println("CSS file not found, skipping styles.");
    }
    
    primaryStage.setScene(menuScene);
});
        
        out1.setOnAction((eout) -> primaryStage.setScene(sin));

        // ==========================================
        // 5. SIGNUP SCENE (sup)
        // ==========================================
        TextField tname_up = new TextField(); PasswordField tpass_up = new PasswordField();
        CheckBox cb1 = new CheckBox("I agree to the terms and conditions");
        Button up = new Button("Sign Up");

        GridPane gup = new GridPane();
        gup.add(new Label("Name:"), 0, 0); gup.add(tname_up, 1, 0);
        gup.add(new Label("Password:"), 0, 1); gup.add(tpass_up, 1, 1);
        gup.add(cb1, 0, 5, 2, 1); gup.add(up, 1, 6);
        gup.setAlignment(Pos.CENTER); gup.setHgap(20); gup.setVgap(20);
        Scene sup = new Scene(gup, 800, 600);

       up.setOnAction((ActionEvent eup) -> {
    String nameInput = tname_up.getText().trim();
    String passInput = tpass_up.getText().trim();

    if (nameInput.isEmpty() || passInput.isEmpty()) {
        showErrorAlert("Input Error", "Please fill all fields.");
        return;
    }
    
    if (!cb1.isSelected()) {
        showErrorAlert("Terms Error", "You must agree to the terms.");
        return;
    }

    try {
        con = DB.dbconn();
        String sql;
       
        if (passInput.equals("20")) {
            sql = "Insert into RESTAURANTSYSTEM.ADMIN values(?,?)";
        } else {
            sql = "Insert into RESTAURANTSYSTEM.CUSTOMER values(?,?)";
        }

        ps = con.prepareStatement(sql);
        ps.setString(1, nameInput);
        ps.setString(2, passInput);

        int result = ps.executeUpdate();
        if (result == 1) {
    User newUser;
    if (passInput.equals("20")) newUser = new Admin(nameInput, passInput);
    else newUser = new Customer(nameInput, passInput);
    showInfoAlert("Registration Success", newUser.getWelcomeMessage("Welcome to our family,"));
    
    if (passInput.equals("20")) primaryStage.setScene(sadm);
    else primaryStage.setScene(sc);
}
        ps.close(); con.close();
    } catch (SQLException ex) {
        showErrorAlert("Database Error", "Registration failed: " + ex.getMessage());
    }
});

        // ==========================================
        // 6. LOGIN & NAVIGATION LOGIC
        // ==========================================
        in.setOnAction((ein) -> {
    String user = tn.getText().trim();
    String pass = tp.getText().trim();

    if (user.isEmpty() || pass.isEmpty()) {
        showErrorAlert("Login Error", "Please enter username and password.");
        return;
    }

    try {
        con = DB.dbconn();
        boolean found = false;
       //check if he is an admin
        ps = con.prepareStatement("SELECT * FROM RESTAURANTSYSTEM.ADMIN WHERE NAME = ? AND PASSWORD = ?");
        ps.setString(1, user);
        ps.setString(2, pass);
        ResultSet rsAdmin = ps.executeQuery();

        if (rsAdmin.next()) {
            found = true;
            User currentAdmin = new Admin(user, pass);
            showInfoAlert("Login Success", currentAdmin.getWelcomeMessage()); 
            primaryStage.setTitle("Restaurant System - " + currentAdmin.getRole());
            primaryStage.setScene(sadm);
}
        rsAdmin.close();
        ps.close();

        //check if he is a customer
        if (!found) {
            ps = con.prepareStatement("SELECT * FROM RESTAURANTSYSTEM.CUSTOMER WHERE NAME = ? AND PASSWORD = ?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rsCust = ps.executeQuery();

            if (rsCust.next()) {
                found = true;
                User currentCustomer = new Customer(user, pass);
                showInfoAlert("Login Success", currentCustomer.getWelcomeMessage()); 
                primaryStage.setTitle("Restaurant System - " + currentCustomer.getRole());
                primaryStage.setScene(sc);
            }
            rsCust.close();
            ps.close();
        }

        // not admin and not customer
        if (!found) {
            showErrorAlert("Login Failed", "Invalid Username or Password.");
        } else {
            tn.clear();
            tp.clear();
        }

        con.close();

    } catch (SQLException ex) {
        showErrorAlert("Database Error", "Error: " + ex.getMessage());
    }
});

        up1.setOnAction((e) -> primaryStage.setScene(sup));

        // --- Set Styles ---
        sadm.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
        sup.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
        sin.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
        sc.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());

        primaryStage.setTitle("Restaurant Management System");
        primaryStage.setScene(sin);
        primaryStage.show();
    }

    // --- Helper Methods for Alerts ---
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void openChatWindow(String username, boolean isServer) {
    Stage chatStage = new Stage();
    TextArea chatArea = new TextArea();
    chatArea.setEditable(false);
    TextField inputField = new TextField();
    Button sendBtn = new Button("Send");
    sendBtn.setDefaultButton(true);

    ChatService chatService = new ChatService(chatArea);

   
    int port = 8888; 

    if (isServer) {
        
        chatService.startServer(port);
    } else {
        
        chatService.connectToServer("127.0.0.1", port); 
    }

    sendBtn.setOnAction(e -> {
        chatService.sendMessage(username, inputField.getText());
        inputField.clear();
    });

    chatStage.setOnCloseRequest(event -> chatService.stop());

    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5, inputField, sendBtn);
    VBox layout = new VBox(10, chatArea, hbox);
    layout.setPadding(new Insets(10));
    chatStage.setScene(new Scene(layout, 350, 450));
    chatStage.setTitle("Chat - " + username);
    chatStage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}