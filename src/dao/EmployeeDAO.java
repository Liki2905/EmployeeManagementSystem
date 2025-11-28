package dao;

import db.DBConnection;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee e) throws SQLException {
        String sql = "INSERT INTO employees(name, age, department, salary) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setInt(2, e.getAge());
            ps.setString(3, e.getDepartment());
            ps.setDouble(4, e.getSalary());
            ps.executeUpdate();
        }
    }

    public void updateEmployee(Employee e) throws SQLException {
        String sql = "UPDATE employees SET name=?, age=?, department=?, salary=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setInt(2, e.getAge());
            ps.setString(3, e.getDepartment());
            ps.setDouble(4, e.getSalary());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("department"),
                        rs.getDouble("salary")
                ));
            }
        }
        return list;
    }

    public List<Employee> searchByName(String name) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE name LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getDouble("salary")
                    ));
                }
            }
        }
        return list;
    }
}
