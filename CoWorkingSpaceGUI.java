import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class CoWorkingSpaceGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
    private String currentTable;

    public CoWorkingSpaceGUI() {
        // Initialize the frame
        frame = new JFrame("Co-Working Space Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Establish Database Connection
        connectToDatabase();

        // Set Global Font
        UIManager.put("Menu.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("MenuItem.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("Panel.font", new Font("Segoe UI", Font.PLAIN, 14));

        // Top Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu tablesMenu = new JMenu("Tables");
        tablesMenu.setFont(new Font("Segoe UI", Font.BOLD, 18));

        String[] tables = {
            "Admin", "Visitor", "Staff_Member", "Room", "Rental", "Feature",
            "Feedback", "Maintenance_Report", "Staff_Service_Request", "Supply_Request"
        };
        for (String tableName : tables) {
            JMenuItem tableItem = new JMenuItem(tableName);
            tableItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tableItem.addActionListener(e -> {
                currentTable = tableName;
                loadTable(tableName);
            });
            tablesMenu.add(tableItem);
        }
        menuBar.add(tablesMenu);

        JMenu actionsMenu = new JMenu("Actions");
        actionsMenu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JMenuItem addButton = new JMenuItem("Add");
        JMenuItem editButton = new JMenuItem("Edit");
        JMenuItem deleteButton = new JMenuItem("Delete");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.addActionListener(e -> showAddForm());
        editButton.addActionListener(e -> showEditForm());
        deleteButton.addActionListener(e -> deleteSelectedRow());
        actionsMenu.add(addButton);
        actionsMenu.add(editButton);
        actionsMenu.add(deleteButton);
        menuBar.add(actionsMenu);

        frame.setJMenuBar(menuBar);

        // Side Menu Bar for Queries
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setPreferredSize(new Dimension(250, frame.getHeight()));

        // Title Label for Queries
        sideMenu.add(Box.createVerticalStrut(30)); // Add space
        JLabel queriesLabel = new JLabel("Joins:");
        queriesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        queriesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideMenu.add(queriesLabel);
        sideMenu.add(Box.createVerticalStrut(15));

        // Query Buttons
        String[] queries = {
            "Admins and Rooms", "Rental Details", "Visitor Rentals", "Maintenance Reports",
            "Feature and Rooms"
        };
        for (String query : queries) {
            JButton queryButton = new JButton(query);
            queryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            queryButton.addActionListener(e -> executeQuery(query));
            queryButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            sideMenu.add(Box.createVerticalStrut(10)); // Add space
            sideMenu.add(queryButton);
        }

        // Separator and Title for Aggregate Functions
        sideMenu.add(Box.createVerticalStrut(30));
        JLabel aggregateLabel = new JLabel("Aggregate Functions:");
        aggregateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        aggregateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideMenu.add(aggregateLabel);
        sideMenu.add(Box.createVerticalStrut(15));

        // Aggregate Function Buttons
        String[] aggregateFunctions = {
            "Count Staff Members", "Average Rental Rate", "Max Rental Duration", "Min Feedback Rating"
        };
        for (String query : aggregateFunctions) {
            JButton queryButton = new JButton(query);
            queryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            queryButton.addActionListener(e -> executeQuery(query));
            queryButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            sideMenu.add(Box.createVerticalStrut(10));
            sideMenu.add(queryButton);
        }

        // Main Content Area
        mainPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(sideMenu, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void connectToDatabase() {
        String url = ""; // database url 
        String user = ""; //username 
        String password = ""; // password
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable(String tableName) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            int columnCount = rs.getMetaData().getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(rs.getMetaData().getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);
            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(rs.getObject(i));
                }
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddForm() {
        showForm("Add Record", null);
    }

    private void showEditForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showForm("Edit Record", selectedRow);
    }

    private void showForm(String title, Integer selectedRow) {
        if (currentTable == null) {
            JOptionPane.showMessageDialog(frame, "Please select a table first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + currentTable + " LIMIT 1")) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            JPanel panel = new JPanel(new GridLayout(columnCount - 1, 2));
            JTextField[] fields = new JTextField[columnCount];
            Vector<Integer> editableColumns = new Vector<>();

            for (int i = 1; i <= columnCount; i++) {
                if (metaData.isAutoIncrement(i)) continue;
                editableColumns.add(i);
                String columnName = metaData.getColumnName(i);
                panel.add(new JLabel(columnName));
                fields[i - 1] = new JTextField();
                if (selectedRow != null) fields[i - 1].setText(table.getValueAt(selectedRow, i - 1).toString());
                panel.add(fields[i - 1]);
            }

            int result = JOptionPane.showConfirmDialog(frame, panel, title, JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                StringBuilder query;
                if (selectedRow == null) {
                    query = new StringBuilder("INSERT INTO " + currentTable + " (");
                    StringBuilder values = new StringBuilder("VALUES (");
                    for (int index : editableColumns) {
                        query.append(metaData.getColumnName(index)).append(",");
                        values.append("'").append(fields[index - 1].getText()).append("',");
                    }
                    query.deleteCharAt(query.length() - 1).append(") ");
                    values.deleteCharAt(values.length() - 1).append(")");
                    query.append(values);
                } else {
                    String primaryKeyColumn = table.getColumnName(0);
                    Object primaryKeyValue = table.getValueAt(selectedRow, 0);
                    query = new StringBuilder("UPDATE " + currentTable + " SET ");
                    for (int index : editableColumns) {
                        query.append(metaData.getColumnName(index)).append(" = '")
                            .append(fields[index - 1].getText()).append("',");
                    }
                    query.deleteCharAt(query.length() - 1);
                    query.append(" WHERE ").append(primaryKeyColumn).append(" = '").append(primaryKeyValue).append("'");
                }
                stmt.executeUpdate(query.toString());
                loadTable(currentTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error processing form: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Statement stmt = connection.createStatement()) {
            String primaryKeyColumn = table.getColumnName(0);
            Object primaryKeyValue = table.getValueAt(selectedRow, 0);
            String query = "DELETE FROM " + currentTable + " WHERE " + primaryKeyColumn + " = '" + primaryKeyValue + "'";
            stmt.executeUpdate(query);
            tableModel.removeRow(selectedRow);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting row: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeQuery(String query) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        String sql;
        switch (query) {
            case "Admins and Rooms":
                sql = "SELECT admin.fname AS AdminName, room.name AS RoomName, room.rental_rate_per_hour FROM admin INNER JOIN room ON admin.admin_id = room.admin_id";
                break;
            case "Rental Details":
                sql = "SELECT rental.start_datetime, rental.end_datetime, room.name AS RoomName, rental.total_price FROM rental INNER JOIN room ON rental.room_id = room.room_id";
                break;
            case "Visitor Rentals":
                sql = "SELECT visitor.fname AS VisitorName, rental.total_price, rental.approval_status FROM visitor LEFT JOIN rental ON visitor.visitor_id = rental.visitor_id";
                break;
            case "Maintenance Reports":
                sql = "SELECT maintenance_report.report_date, maintenance_report.issue_description, room.name AS RoomName FROM maintenance_report INNER JOIN room ON maintenance_report.room_id = room.room_id";
                break;
            case "Feature and Rooms":
                sql = "SELECT feature.name AS FeatureName, room.name AS RoomName, room.capacity FROM feature RIGHT JOIN room ON FIND_IN_SET(feature.name, room.features)";
                break;
            case "Count Staff Members":
                sql = "SELECT COUNT(*) AS TotalStaff FROM staff_member";
                break;
            case "Average Rental Rate":
                sql = "SELECT AVG(rental_rate_per_hour) AS AverageRentalRate FROM room";
                break;
            case "Max Rental Duration":
                sql = "SELECT MAX(hours_rented) AS MaxRentalDuration FROM rental";
                break;
            case "Min Feedback Rating":
                sql = "SELECT MIN(rating) AS MinFeedbackRating FROM feedback";
                break;
            default:
                sql = "";
        }
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int columnCount = rs.getMetaData().getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(rs.getMetaData().getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);
            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(rs.getObject(i));
                }
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to execute query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CoWorkingSpaceGUI();
    }
}
