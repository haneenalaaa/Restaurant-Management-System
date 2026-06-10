# 🍽️ Restaurant Management System

A desktop application built with **JavaFX** and **Oracle Database** for managing restaurant operations including orders, reservations, and real-time customer-admin communication.

> Developed as a team project (6 members) — Advanced Programming course

---

## ✨ Features

### 👤 User Authentication
- Login and Sign Up system with two roles: **Admin** and **Customer**
- Abstract `User` class with method overloading for welcome messages

### 🛒 Order Management
- Customers can place orders by selecting items from the menu
- Each order tracks: customer name, ordered items, total price (in EGP), and status
- Admin can view and manage all orders

### 📅 Table Reservation
- Customers can reserve tables by providing: name, phone number, date, time, number of guests, and table ID
- Reservation data is saved to the database

### 🍕 Menu Management
- Menu items have: name, description, price, and category
- Admin can add/view menu items
- Items support multiple constructors (overloading)

### 💬 Real-Time Chat (Customer ↔ Admin)
- Live chat feature using **Java Sockets**
- Customer connects to Admin as a client
- Admin runs as a server on a local port
- Supports reconnection attempts automatically

### 📊 Report Generation
- Both `Order` and `Reservation` implement the `Reportable` interface
- Reports include formatted date/time, customer info, and summary details

### 🗄️ Database Integration
- Connected to **Oracle Database XE** via JDBC
- All data (users, orders, reservations, menu) is persisted in the database

---

## 🏗️ Project Structure

```
src/advancedproj/
├── User.java          # Abstract base class for Admin and Customer
├── Admin.java         # Admin role (extends User)
├── Customer.java      # Customer role (extends User)
├── MenuItem.java      # Menu item model (name, price, description, category)
├── Order.java         # Order model — implements Reportable
├── Reservation.java   # Reservation model — implements Reportable
├── Reportable.java    # Interface for generating reports
├── ChatService.java   # Real-time chat using Java Sockets
├── Table.java         # Table model (number, capacity, availability)
├── DB.java            # Oracle Database connection handler
├── Main.java          # JavaFX application entry point
├── Final.java         # Main UI controller
└── StyleSheet.css     # JavaFX styling
```

---

## 🧠 OOP Concepts Used

| Concept | Where Used |
|--------|-----------|
| **Inheritance** | `Admin` and `Customer` extend `User` |
| **Abstraction** | `User` is abstract, `getRole()` is abstract method |
| **Interface** | `Reportable` implemented by `Order` and `Reservation` |
| **Overloading** | `getWelcomeMessage()` and `MenuItem` constructors |
| **Association** | `Order` has a `Customer` |
| **Aggregation** | `Order` contains a list of `MenuItem` objects |
| **Multithreading** | `ChatService` runs socket connections on separate threads |

---

## ⚙️ How to Run

### Requirements
- Java JDK 8+
- JavaFX SDK
- Oracle Database XE (local)
- NetBeans IDE (recommended)

### Setup Steps
1. Install **Oracle Database 21c XE** from [oracle.com](https://www.oracle.com/database/technologies/xe-downloads.html)
2. Create a user with username `DB207` and password `123`
3. Run the SQL setup script (if provided) to create the tables
4. Open the project in **NetBeans**
5. Make sure `ojdbc6.jar` is in the project libraries
6. Run `Main.java`

> ⚠️ The database runs locally, so you need Oracle XE installed on your machine to use all features.

---

## 👥 Team

Developed by a team of 6 students — Alexandria University, Faculty of Science  
Course: Advanced Programming
