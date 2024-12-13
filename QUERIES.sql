USE CoWorkingSpace; -- queries 


-- admin table 
SELECT * 
FROM admin
ORDER BY lname ASC;

SELECT * 
FROM admin
WHERE fname LIKE 'M%';

-- feature table 
SELECT * 
FROM feature
ORDER BY maintenance_status;

SELECT * 
FROM feature
WHERE maintenance_status LIKE '%Working%';

-- feedback table 
SELECT * 
FROM feedback
WHERE rating = 5
ORDER BY room_id;

SELECT * 
FROM feedback
WHERE comment LIKE '%good%';

-- maintenance report 
SELECT *
FROM maintenance_report 
WHERE resolved_status 
ORDER BY report_date; 

SELECT * 
FROM maintenance_report 
WHERE issue_description LIKE '%screen%'; 

-- --rental 
SELECT * 
FROM rental 
ORDER BY total_price DESC;  

SELECT *
FROM rental 
WHERE approval_status LIKE 'pending'; 

-- room 
SELECT * 
FROM room 
ORDER BY rental_rate_per_hour ASC; 

SELECT * 
FROM room 
WHERE capacity LIKE '10'; 

-- staff member 
SELECT * 
FROM staff_member 
WHERE job_title ORDER BY staff_ID; 

SELECT * 
FROM staff_member 
WHERE fname LIKE 'A%'; 

-- staff service request 
SELECT *
FROM staff_service_request  
ORDER BY request_date; 

SELECT * 
FROM staff_service_request 
WHERE item_name LIKE 'Markers'; 

-- supply request 
SELECT *
FROM supply_request 
WHERE status ORDER BY item_name; 

SELECT * 
FROM supply_request 
WHERE admin_id LIKE '1'; 

-- visitor 
SELECT * 
FROM visitor 
WHERE membership_status ORDER BY 'Pending'; 

SELECT * 
FROM visitor 
WHERE fname LIKE 'A%'; 

