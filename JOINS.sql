USE CoWorkingSpace; -- joins 

-- 1. Inner Join: Admin and Room
SELECT admin.fname AS AdminName, room.name AS RoomName, room.rental_rate_per_hour
FROM admin
INNER JOIN room ON admin.admin_id = room.admin_id;
-- Links admins with the rooms they manage, providing essential context for room management.

-- 2. Inner Join: Rental and Room
SELECT rental.start_datetime, rental.end_datetime, room.name AS RoomName, rental.total_price
FROM rental
INNER JOIN room ON rental.room_id = room.room_id;
-- Shows rental details linked to specific rooms, essential for billing and room usage tracking.

-- 3. Left Join: Visitor and Rental
SELECT visitor.fname AS VisitorName, rental.total_price, rental.approval_status
FROM visitor
LEFT JOIN rental ON visitor.visitor_id = rental.visitor_id;
-- Retrieves all visitors with their rentals, highlighting cases where no rentals exist for a visitor.

-- 4. Inner Join: Maintenance Report and Room
SELECT maintenance_report.report_date, maintenance_report.issue_description, room.name AS RoomName
FROM maintenance_report
INNER JOIN room ON maintenance_report.room_id = room.room_id;
-- Fetches maintenance details for rooms, important for operational monitoring.

-- 5. Right Join: Feature and Room
SELECT feature.name AS FeatureName, room.name AS RoomName, room.capacity
FROM feature
RIGHT JOIN room ON FIND_IN_SET(feature.name, room.features);
-- Lists room features, ensuring all rooms are represented even if no features are linked.

-- 6. Left Join: Staff Member and Staff Service Request
SELECT staff_member.fname AS StaffName, staff_service_request.item_name, staff_service_request.quantity
FROM staff_member
LEFT JOIN staff_service_request ON staff_member.staff_id = staff_service_request.staff_id;
-- Shows staff service requests, including staff members without any requests.

-- 7. Left Join: Admin and Supply Request
SELECT admin.fname AS AdminName, supply_request.item_name, supply_request.status
FROM admin
LEFT JOIN supply_request ON admin.admin_id = supply_request.admin_id;
-- Links admins with their supply requests, ensuring all admins are displayed.

-- 8. Right Join: Supply Request and Admin
SELECT supply_request.item_name, supply_request.quantity, admin.fname AS AdminName
FROM supply_request
RIGHT JOIN admin ON supply_request.admin_id = admin.admin_id;
-- Highlights all supply requests while ensuring all admins are displayed.

-- 9. Full Join: Visitor and Feedback
SELECT visitor.fname AS VisitorName, feedback.comment
FROM visitor
LEFT JOIN feedback ON visitor.visitor_id = feedback.visitor_id
UNION
SELECT visitor.fname AS VisitorName, feedback.comment
FROM visitor
RIGHT JOIN feedback ON visitor.visitor_id = feedback.visitor_id;
-- Combines visitor names and feedback, ensuring full visibility of both entities.

-- 10. Full Join: Feedback and Room
SELECT feedback.rating, room.name AS RoomName
FROM feedback
LEFT JOIN room ON feedback.room_id = room.room_id
UNION
SELECT feedback.rating, room.name AS RoomName
FROM feedback
RIGHT JOIN room ON feedback.room_id = room.room_id;
-- Combines feedback ratings and rooms, ensuring visibility of feedback and rooms without feedback.
