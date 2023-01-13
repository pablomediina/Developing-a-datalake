package org.example.interfaces;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface DatabaseReader {
    List<File> getFilesNotFull();

    List<String> alreadyIn() throws SQLException, ParseException;
}
