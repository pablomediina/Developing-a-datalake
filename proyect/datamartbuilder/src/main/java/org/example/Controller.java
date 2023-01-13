package org.example;


import org.example.model.Weather;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class Controller {
    private final SQLiteWriter writer;
    private final SQLiteReader reader;
    private final Datalake datalake;

    public Controller(File datalakeRoot) {
        writer = new SQLiteWriter();
        reader = new SQLiteReader(datalakeRoot);
        datalake = new Datalake();
    }

    public void run() throws SQLException {
        writer.createTableMax();
        writer.createTableMin();
        List<List<Weather>> maxAndMins = datalake.getMaxAndMins(reader.getFilesNotFull());
        writer.updateDatamart(maxAndMins, reader.alreadyIn());
    }
}
