package ui;

import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private EmployeeDAO dao = new EmployeeDAO();

    public MainFrame() {
        setTitle("Employee Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Department", "Salary"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        loadEmployees();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Top panel - Search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        topPanel.add(new JLabel("Search by name:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);

        // Bottom panel - Buttons
        JPanel bottom = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        bottom.add(addBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);
        add(bottom, BorderLayout.SOUTH);

        // Add button
        addBtn.addActionListener(e -> new EmployeeForm(this, null));

        // Edit button
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to edit."); return; }
            int id = (int) model.getValueAt(row, 0);
            String name = (String) model.getValueAt(row, 1);
            int age = (int) model.getValueAt(row, 2);
            String dept = (String) model.getValueAt(row, 3);
            double sal = (double) model.getValueAt(row, 4);
            Employee eobj = new Employee(id, name, age, dept, sal);
            new EmployeeForm(this, eobj);
        });

        // Delete button
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete selected employee?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    dao.deleteEmployee(id);
                    loadEmployees();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
                }
            }
        });

        // Search button
        searchBtn.addActionListener(e -> {
            String q = searchField.getText().trim();
            if (q.isEmpty()) { loadEmployees(); return; }
            try {
                model.setRowCount(0);
                List<Employee> res = dao.searchByName(q);
                for (Employee emp : res) {
                    model.addRow(new Object[]{emp.getId(), emp.getName(), emp.getAge(), emp.getDepartment(), emp.getSalary()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Search failed: " + ex.getMessage());
            }
        });

        // Refresh button
        refreshBtn.addActionListener(e -> loadEmployees());

        setVisible(true);
    }

    // Method to load employees into table
    public void loadEmployees() {
        try {
            model.setRowCount(0);
            for (Employee e : dao.getAllEmployees()) {
                model.addRow(new Object[]{e.getId(), e.getName(), e.getAge(), e.getDepartment(), e.getSalary()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Load failed: " + ex.getMessage());
        }
    }
}
