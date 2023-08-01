package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.BookDAO;
import dao.UserDAO;
import model.Book;
import service.BookList;

public class MainController {
public static void main(String[] args) throws SQLException, ClassNotFoundException {
    BookDAO bookd = new BookDAO();
    UserDAO userd = new UserDAO();
    bookd.createTableIfNotExists();
    userd.createTableIfNotExists();

    Scanner scanner = new Scanner(System.in);

    System.out.println("Welcome to Library Management System!");
    System.out.println("Please log in to continue.");
    boolean isUserLoggedIn = false;
    boolean isAdminLoggedIn = false;
    String username, password;
    do {
        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("Password: ");
        password = scanner.nextLine();

        isUserLoggedIn = UserDAO.isValidUserCredentials(username, password);
        isAdminLoggedIn = UserDAO.isValidAdminCredentials(username, password);

        if (!isUserLoggedIn && !isAdminLoggedIn) {
            System.out.println("Invalid credentials. Please try again.");
        }
    } while (!isUserLoggedIn && !isAdminLoggedIn);

    if (isUserLoggedIn) {
        System.out.println("User Login successful!\n");
        while (true) {
            System.out.println("==== Library Management System ====");
            System.out.println("1. Add Book");
            System.out.println("2. Display All Books");
            System.out.println("3. Rent a Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    Book newBook = new Book(-1, title, author, false);
                    BookDAO.addBookToDatabase(newBook);
                    break;

                case 2:
                    List<Book> books = BookDAO.getAllBooksFromDatabase();
                    BookList.displayAllBooks(books);
                    System.out.println();
                    break;


                case 3:
                        if (UserDAO.checkUserBorrow(username)) {
                                    System.out.print("Enter book title to search: ");
                                String searchTitle = scanner.nextLine();
                                    List<Book> foundBooks = BookDAO.findBookByName(searchTitle);
                    if (!foundBooks.isEmpty()) {
                                    System.out.println("Books found:");
                                    BookList.displayAllBooks(foundBooks);
                                                                                    System.out.println("Enter Book Number to Rent: ");
                                                                                    Book bookToRent = foundBooks.get(scanner.nextInt()-1);
                                                                                    scanner.nextLine();
                                                                                    if (bookToRent.isBorrowed()) {
                                                                                        System.out.println("Book is already Borrowed.");
                                                                                                                                    } else {
BookDAO. updateBookBorrowedStatus(bookToRent.getId(), true,username);

                                                                                                                                                                }
                                                                                                                                                            } else {
                                        System.out.println("No books found with the given title.\n");
                                                                                                }
                                                                                                                            }
                                                                                                                            else{
                                                                                                        System.out.println("User: " + username+" has aldready borrowed a book. Return it to admin to borrow another book.");
                                                                                                                            }
                    break;

                case 4:
                    System.out.println("Exiting Library Management...");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a valid option (1-4).\n");
            }
    }
    } else if (isAdminLoggedIn) {
        System.out.println("Admin Login successful!\n");
        while (true) {
            System.out.println("==== Library Management System ====");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. Update Password of User");
            System.out.println("4. Update Book Record");
            System.out.println("5. Display All Books");
            System.out.println("6. Display User Records");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
            System.out.print("Enter username: ");
            String name = scanner.next();
            System.out.print("Enter password: ");
            String newpassword = scanner.next();
            UserDAO.addUser(name,newpassword);

            break;
        case 2:
            System.out.print("Enter username to delete: ");
            String oldname = scanner.next();
            UserDAO.deleteUser(oldname);

            break;
        case 3:
            System.out.print("Enter username to update password: ");
            String userUpdate = scanner.next();
            System.out.print("Enter new password: ");
            String newPassword = scanner.next();
            UserDAO.updateUserPassword(userUpdate, newPassword);

            break;
                case 4:
                System.out.println();
                        UserDAO.getAllUsersFromDatabase();
                System.out.println();
                System.out.print("Enter username: ");
                String userReturn = scanner.nextLine();
                    List<Book> books = BookDAO.getAllBooksFromDatabase();
                    BookList.displayAllBooks(books);
                    System.out.println();
                System.out.print("Enter BookId: ");
                int bookId = scanner.nextInt();
                if (userReturn != null)
                BookDAO.bookReturned(bookId, userReturn);
                else BookDAO.bookReturned(bookId);
                case 5:
                    List<Book> booksa = BookDAO.getAllBooksFromDatabase();
                    BookList.displayAllBooks(booksa);
                    System.out.println();
                    break;
                case 6:
                System.out.println();
                        UserDAO.getAllUsersFromDatabase();
                System.out.println();
                        break;
                case 7:
                    System.out.println("Exiting Library Management..."); 
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a valid option (1-7).\n");
            }
    }
}
        }
    }
