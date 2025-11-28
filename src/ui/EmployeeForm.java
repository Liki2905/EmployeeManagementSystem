package ui;

import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class EmployeeForm extends JDialog {
    private JTextField nameField, ageField, deptField, salaryField;
    private JButton saveBtn, cancelBtn;
    private EmployeeDAO dao = new EmployeeDAO();
    private Employee employee;
    private MainFrame mainFrame;

    // Constructor that fixes the issue
    public EmployeeForm(MainFrame parent, Employee e) {
        super(parent, "Employee Form", true); // parent = MainFrame, modal = true
        mainFrame = parent;
        employee = e;

        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 250);
        setLocationRelativeTo(parent);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Department:"));
        deptField = new JTextField();
        add(deptField);

        add(new JLabel("Salary:"));
        salaryField = new JTextField();
        add(salaryField);

        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        add(saveBtn);
        add(cancelBtn);

        // If editing, pre-fill the fields
        if (employee != null) {
            nameField.setText(employee.getName());
            ageField.setText(String.valueOf(employee.getAge()));
            deptField.setText(employee.getDepartment());
            salaryField.setText(String.valueOf(employee.getSalary()));
        }

        saveBtn.addActionListener(ev -> saveEmployee());
        cancelBtn.addActionListener(ev -> dispose());

        setVisible(true);
    }

    private void saveEmployee() {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String dept = deptField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());

            if (employee == null) { // Adding new employee
                dao.addEmployee(new Employee(0, name, age, dept, salary));
            } else { // Editing existing employee
                employee.setName(name);
                employee.setAge(age);
                employee.setDepartment(dept);
                employee.setSalary(salary);
                dao.updateEmployee(employee);
            }

            mainFrame.loadEmployees(); // Refresh table in MainFrame
            dispose();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid age or salary!");
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, "Database error: " + se.getMessage());
        }
    }
}

