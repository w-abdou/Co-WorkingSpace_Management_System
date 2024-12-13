CREATE DATABASE CoWorkingSpace;
USE CoWorkingSpace;

CREATE TABLE admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(12) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE visitor (
    visitor_id INT PRIMARY KEY AUTO_INCREMENT,
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) NOT NULL CHECK (phone_number REGEXP '^\\+20[0-9]{10}$'),
    membership_status ENUM('Active', 'Inactive', 'Pending') NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(12) NOT NULL
) AUTO_INCREMENT = 200;


CREATE TABLE staff_member (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) CHECK (phone_number REGEXP '^\\+20[0-9]{10}$'),
    job_title ENUM('member', 'leader', 'maintenance support', 'housekeeping', 'security', 'receptionist', 'manager') NOT NULL DEFAULT 'member',
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(12) NOT NULL
);

CREATE TABLE feature (
    feature_code INT PRIMARY KEY AUTO_INCREMENT,
    name ENUM('WiFi', 'Projector', 'Whiteboard', 'Air Conditioning', 'Coffee Machine', 'Printer', 'Sound System','Lighting', 'Computers','TV','Desk','Luxury Seating' ) NOT NULL,
    description TEXT NOT NULL,
    maintenance_status ENUM('Working', 'Maintenance') DEFAULT 'Working'
);

CREATE TABLE room (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    capacity ENUM('2', '5', '10', '20') NOT NULL,
    features SET('WiFi', 'Projector', 'Whiteboard', 'Air Conditioning', 'Coffee Machine', 'Printer', 'Sound System','Lighting', 'Computers','TV','Desk','Luxury Seating' ) NOT NULL,
    status ENUM('Available', 'Occupied', 'Maintenance') DEFAULT 'Available',
    rental_rate_per_hour DECIMAL(10,2) NOT NULL,
    admin_id INT,
    FOREIGN KEY (admin_id) REFERENCES admin(admin_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE rental (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    start_datetime DATETIME NOT NULL,
    end_datetime DATETIME NOT NULL,
    hours_rented DECIMAL(10,2) GENERATED ALWAYS AS (TIMESTAMPDIFF(MINUTE, start_datetime, end_datetime) / 60) STORED,
    total_price DECIMAL(10,2),
    approval_status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    room_id INT NOT NULL,
    visitor_id INT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (visitor_id) REFERENCES visitor(visitor_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DELIMITER $$

CREATE TRIGGER calculate_total_price
BEFORE INSERT ON rental
FOR EACH ROW
BEGIN
    DECLARE rental_rate DECIMAL(10,2);
    
    -- Get the rental rate per hour from the room table
    SELECT rental_rate_per_hour INTO rental_rate
    FROM room
    WHERE room_id = NEW.room_id;
    
    -- Calculate the total price
    SET NEW.total_price = rental_rate * TIMESTAMPDIFF(MINUTE, NEW.start_datetime, NEW.end_datetime) / 60;
END$$

DELIMITER ;

CREATE TABLE staff_service_request (
    ssr_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    request_date DATE NOT NULL,
    room_id INT,
    staff_id INT,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff_member(staff_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE maintenance_report (
    mr_id INT PRIMARY KEY AUTO_INCREMENT,
    report_date DATE NOT NULL,
    issue_description TEXT NOT NULL,
    resolved_status ENUM('Pending', 'Resolved') DEFAULT 'Pending',
    room_id INT,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE feedback (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT,
    rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    feedback_date DATE NOT NULL,
    room_id INT,
    visitor_id INT,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (visitor_id) REFERENCES visitor(visitor_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE supply_request (
    sr_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    request_date DATE NOT NULL,
    admin_id INT,
    FOREIGN KEY (admin_id) REFERENCES admin(admin_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE visitor_rents_room (
    visitor_id INT,
    rental_id INT,
    room_id INT,
    PRIMARY KEY (visitor_id, rental_id, room_id),
    FOREIGN KEY (visitor_id) REFERENCES visitor(visitor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (rental_id) REFERENCES rental(rental_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE room_assigned_to_staff_member (
    room_id INT,
    staff_id INT,
    PRIMARY KEY (room_id, staff_id),
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff_member(staff_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE feature_provided_in_room (
    feature_code INT,
    room_id INT,
    PRIMARY KEY (feature_code, room_id),
    FOREIGN KEY (feature_code) REFERENCES feature(feature_code) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room(room_id) ON DELETE CASCADE ON UPDATE CASCADE
);


