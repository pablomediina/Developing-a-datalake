package org.example;


import static spark.Spark.get;

public class SparkWebService {
    public void start() {
        SQLiteReader SQLiteReader = new SQLiteReader();

        get("/v1/places/with-max-temperature", (req, res) ->
                SQLiteReader.getMaxsFromTo(req.queryParams("from"), req.queryParams("to")));

        get("/v1/places/with-min-temperature", (req, res) ->
                SQLiteReader.getMinsFromTo(req.queryParams("from"), req.queryParams("to")));
    }
}