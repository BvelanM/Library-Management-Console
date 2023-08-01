package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.LibraryInterface;
import utility.ConnectionManager;

public class BookDAO implements LibraryInterface {
    
    public void createTableIfNotExists() throws SQLException {

														try (Connection conn = ConnectionManager.getConnection()) {
															Statement stmt = conn.createStatement();
String sql = "CREATE TABLE IF NOT EXISTS books (" +
															      "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
															      "title TEXT NOT NULL," +
															      "author TEXT NOT NULL," +
															      "is_Borrowed BOOLEAN NOT NULL DEFAULT 0)";
stmt.execute(sql);
stmt.close();
conn.close();
														} catch (ClassNotFoundException e) {
															e.printStackTrace();
														}

    }

    public static void addBookToDatabase(Book book) throws ClassNotFoundException {
        try (Connection conn = ConnectionManager.getConnection()) 
								{
																						PreparedStatement pstmt = conn.prepareStatement(
																														"INSERT INTO books (title, author, is_Borrowed) VALUES (?, ?, ?)");
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setBoolean(3, book.isBorrowed());
            pstmt.executeUpdate();
												                        System.out.println("Book added successfully!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> getAllBooksFromDatabase() throws ClassNotFoundException {
              List<Book> books = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean isBorrowed = resultSet.getBoolean("is_Borrowed");
                Book book = new Book(id, title, author, isBorrowed);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static List<Book> findBookByName(String title) throws ClassNotFoundException {
        List<Book> foundBooks = new ArrayList<>();
         try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM books WHERE title LIKE ?")) {
            pstmt.setString(1, "%" + title + "%");
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean isBorrowed = resultSet.getBoolean("is_Borrowed");
                Book book = new Book(id, bookTitle, author, isBorrowed);
                foundBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundBooks;
    }

    public static void updateBookBorrowedStatus(int bookId, boolean isBorrowed, String username) throws ClassNotFoundException {
                 try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE books SET is_Borrowed = ? WHERE id = ?")) {
            pstmt.setBoolean(1, isBorrowed);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            PreparedStatement pstmt1 = conn.prepareStatement("UPDATE users SET borrowedBook = ? WHERE name = ?");
            pstmt1.setInt(1, bookId);
            pstmt1.setString(2, username);
            pstmt1.executeUpdate();
												                                            System.out.println("Book Borrowed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void bookReturned(int bookId, String username) throws ClassNotFoundException {
       try (Connection conn = ConnectionManager.getConnection()){
        String updateBookSql = "UPDATE books SET is_Borrowed = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateBookSql)) {
            pstmt.setBoolean(1, false);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }

        int userId = -1;
        String getUserIdSql = "SELECT id FROM users WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(getUserIdSql)) {
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        }
        String updateUserSql = "UPDATE users SET borrowedBook = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateUserSql)) {
            pstmt.setNull(1, Types.INTEGER); 
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            System.out.println("Book Returned");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    public static void bookReturned(int bookId) throws ClassNotFoundException, SQLException {
       try (Connection conn = ConnectionManager.getConnection()){
        String updateBookSql = "UPDATE books SET is_Borrowed = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateBookSql)) {
            pstmt.setBoolean(1, false);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
                        System.out.println("Book Returned"); 
        }
        catch (SQLException e) {
        e.printStackTrace();
    }
}
    }
}
