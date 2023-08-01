package dao;

import java.sql.*;

import model.LibraryInterface;
import model.Role;
import utility.ConnectionManager;

public class UserDAO implements LibraryInterface{

    public static void addUser(String name, String password) throws ClassNotFoundException {
         try {
           	Connection connection = ConnectionManager.getConnection();
            String sql = "INSERT INTO users (name, password,role) VALUES (?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, Role.User.toString());
            statement.executeUpdate();
            statement.close();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String name) throws ClassNotFoundException {
        try {
           	Connection connection = ConnectionManager.getConnection();
            String sql = "DELETE FROM users WHERE name = ? and role=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, "User");
            statement.executeUpdate();
            statement.close();
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUserPassword(String name, String newPassword) throws ClassNotFoundException {
          try {
           	Connection connection = ConnectionManager.getConnection();
            String sql = "UPDATE users SET password = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setString(2, name);
            statement.executeUpdate();
            statement.close();
                            System.out.println("Password updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidAdminCredentials(String username, String password) throws ClassNotFoundException {
           try	(Connection connection = ConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM users WHERE name = ? AND password = ? AND role = 'admin'")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isValidUserCredentials(String username, String password) throws ClassNotFoundException {
        try	(Connection conn = ConnectionManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(
"SELECT COUNT(*) AS count FROM users WHERE name = ? AND password = ? AND role = 'user'")) {
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("count") > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
    }

    public  void createTableIfNotExists() throws ClassNotFoundException {
            try	(Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
          String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(200) NOT NULL UNIQUE," +
                     "password TEXT NOT NULL," +
                    "role TEXT NOT NULL," +
                    "borrowedBook INTEGER,"+
                    "FOREIGN KEY (borrowedBook) REFERENCES books(id))";;
            stmt.execute(sql);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        public static boolean checkUserBorrow(String username) throws ClassNotFoundException {
                 try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "select borrowedBook from users where name=?")) {
            pstmt.setString(1, username);
           ResultSet resultSet=pstmt.executeQuery();
            resultSet.next();
           if (resultSet.getInt("borrowedBook") >0)  return false;
           else return true; 
         }catch (SQLException e) {
            e.printStackTrace();
        }
                 return false;
    }
     public static void getAllUsersFromDatabase() throws ClassNotFoundException {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT id,name,borrowedBook FROM users where role='user'");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int borrowedBook = resultSet.getInt("borrowedBook");
                System.out.println(id+" "+name+" "+borrowedBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






    
