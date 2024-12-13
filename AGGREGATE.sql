USE CoWorkingSpace; -- AGGREGATE FNS 

-- Counting the number of staff members in the staff_member table
SELECT COUNT(*) AS total_staff FROM staff_member;


-- Finding the average rental price in the room table
SELECT AVG(rental_rate_per_hour) AS avg_rental_rate FROM room;


--  Finding the maximum number of rooms rented in a single rental from the
SELECT MAX(hours_rented) AS max_rental_duration FROM rental;


--  Finding the minimum rating in the feedback table
SELECT MIN(rating) AS lowest_rating FROM feedback;


--  Finding the sum of all rental prices in the rental table
SELECT SUM(total_price) AS total_revenue FROM rental;