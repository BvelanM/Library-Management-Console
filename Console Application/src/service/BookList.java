package service;
import java.util.List;
import model.Book;

public class BookList {
    public static void displayAllBooks(List<Book> books) {
        System.out.println("Library Books:");
        int i=1;
        for (Book book : books) {
            System.out.println(i+" "+book);
            i++;
        }
    }
	}

