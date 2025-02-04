

# Co-Working Space Management System

## Overview

The **Co-Working Space Management System** is a **desktop application** developed in **Java** with a **Graphical User Interface (GUI)**. It enables users to manage co-working space reservations, track available desks, handle user registrations, and manage payment processing efficiently. The system is **integrated with an SQL database** to store user data, reservations, payments, and workspace availability.

## Features

- **Graphical User Interface (GUI)**: User-friendly interface for managing bookings.
- **SQL Database Integration**: Stores user accounts, bookings, payments, and workspace details.
- **User Management**: Register, update, and delete user profiles.
- **Space Reservation**: Book and manage workspaces in a co-working environment.
- **Real-Time Availability**: Display available desks and rooms dynamically.
- **Billing & Payment Processing**: Manage payment transactions for reservations.
- **Admin Dashboard**: Provides an overview of bookings, revenue, and user activity.

## Code Structure

### 1. **Core Classes**
- `User`: Represents co-working space members with attributes like name, email, and membership type.
- `Workspace`: Defines workspaces available for booking, including desk type and location.
- `Reservation`: Manages bookings, including user ID, workspace ID, and booking duration.
- `Payment`: Handles transactions and stores payment records.
- `Admin`: Provides administrative functions like user management and workspace allocation.
- `DatabaseManager`: Handles SQL queries and database interactions.

### 2. **SQL Database Tables**
- `users`: Stores user details (ID, name, email, membership type).
- `workspaces`: Contains workspace details (ID, type, availability status).
- `reservations`: Tracks bookings with user IDs, workspace IDs, and time slots.
- `payments`: Stores payment details including user ID, booking ID, amount, and payment status.

### 3. **Graphical User Interface (GUI)**
- `LoginScreen`: Authentication for users and admins.
- `MainDashboard`: The main interface showing workspace availability.
- `BookingPanel`: Interface for users to select and book workspaces.
- `AdminPanel`: Administrative interface for managing users and workspaces.
- `PaymentPanel`: Handles payment processing for reservations.

## How to Use

### 1. Clone the Repository:
```bash
git clone https://github.com/w-abdou/Co-WorkingSpace_Management_System.git
cd Co-WorkingSpace_Management_System
```

### 2. Set Up the SQL Database:
1. **Create the database** in MySQL (or another SQL-based system):
   ```sql
   CREATE DATABASE coworking_space;
   ```
2. **Import the provided SQL schema**:
   ```sql
   USE coworking_space;
   SOURCE database_schema.sql;
   ```
3. **Update database connection details** in `DatabaseManager.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/coworking_space";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
   ```

### 3. Compile and Run the Application:
```bash
javac *.java
java MainDashboard
```

### 4. Application Workflow:

#### **For Users:**
1. **Login/Register**: Users create an account or log in.
2. **Browse Workspaces**: View available workspaces with real-time updates from the SQL database.
3. **Book a Space**: Select a workspace and duration.
4. **Make Payment**: Complete payment via integrated methods.
5. **Check Booking Status**: View and manage reservations.

#### **For Admins:**
1. **Manage Users**: Approve, edit, or remove user accounts.
2. **Allocate Workspaces**: Add, modify, or remove desks and rooms.
3. **View Reports**: Monitor bookings and revenue.


## Notes

- Ensure **MySQL or any other SQL database** is installed and running.
- If using **JavaFX**, make sure JavaFX is installed.
- Update the **database credentials** in `DatabaseManager.java` before running.

