# ✈️ Airlines Management System

A Java Swing + MySQL based Airlines Management System to manage
Passengers, Airhosters and Officers efficiently.

## ✨ Features
- 🧳 Passenger Management (Add, Update, Delete, View)
- 👩‍✈️ Airhoster Management (Add, Update, Delete, View)
- 🪖 Officer Management (Add, Update, Delete, View)
- 📋 Dynamic Table Loading based on selection
- ✅ Phone Number Validation (10-digit Indian numbers)
- 🖱️ Click-to-fill form from table

## 🛠️ Technologies Used
- Java (Swing + AWT)
- MySQL Database
- JDBC Connectivity

## 🗄️ Database Tables

| Table | Description |
|-------|-------------|
| `passenger_list` | Stores passenger details |
| `airhosters` | Stores airhoster details |
| `officers` | Stores officer details |

## 🗃️ Database Setup

```sql
CREATE DATABASE malathi_airlines;
USE malathi_airlines;

CREATE TABLE passenger_list (
    passengername VARCHAR(100),
    seats INT,
    phone_no VARCHAR(15),
    classes VARCHAR(50),
    gender VARCHAR(10)
);

CREATE TABLE airhosters (
    airhostersname VARCHAR(100),
    phone_no VARCHAR(15),
    reg_no VARCHAR(50)
);

CREATE TABLE officers (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(15)
);
```

## ▶️ How to Run
1. Install Java JDK 17+
2. Install MySQL
3. Run the SQL setup above
4. Add MySQL connector JAR to classpath
5. Run `AirlinesManagementSystem.java`

## 👩‍💻 Developer
Malathi
