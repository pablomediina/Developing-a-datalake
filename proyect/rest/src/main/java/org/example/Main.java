package org.example;


public class Main {
    public static void main(String[] args) {
        // http://localhost:4567/v1/places/with-max-temperature?from={yyyyMMdd}&to={yyyyMMdd}
        // http://localhost:4567/v1/places/with-min-temperature?from={yyyyMMdd}&to={yyyyMMdd}
        Controller controller = new Controller();
        controller.run();
    }
}