package org.example;

import org.example.interfaces.DatabaseReader;
import org.example.repositories.FileRepository;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLiteReader implements DatabaseReader {
    private final File root;
    SQLiteConnection cc = new SQLiteConnection();
    Connection cn = cc.connect();
    Statement statement;

    {
        try {
            statement = cn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SQLiteReader(File root) {
        this.root = root;
    }

    @Override
    public List<File> getFilesNotFull() {
        File[] listOfFiles = root.listFiles();
        FileRepository fileRepository;
        fileRepository = new FileRepository().addAll(listOfFiles);
        return fileRepository.all()
                .filter(file -> file.length() < 72490) // 72490 is the length a file will have if it is full
                .collect(Collectors.toList());
    }

    @Override
    public List<String> alreadyIn() throws SQLException {
        String query = "SELECT date FROM maximumTemperatures";
        ResultSet resultSet = statement.executeQuery(query);
        List<String> dates = new ArrayList<>();
        while (resultSet.next()) dates.add(resultSet.getString("date"));
        return dates;
    }
}