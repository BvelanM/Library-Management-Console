package model;

import java.sql.SQLException;

public interface LibraryInterface {
       void createTableIfNotExists() throws ClassNotFoundException, SQLException;
}
