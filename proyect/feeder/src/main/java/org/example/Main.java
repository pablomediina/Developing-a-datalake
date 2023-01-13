package org.example;


import java.io.File;

public class Main {
    public static void main(String[] args) {
        // args[0] = apiKey         (ex: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYWJsb21zLmxwQGdtYWlsLmNvbSIsImp0aSI6IjIyMmY0NDljLTJjNzItNDkwYy05ZjNhLWJmMzdkYmMzNWU5ZSIsImlzcyI6IkFFTUVUIiwiaWF0IjoxNjcxMzc0OTI2LCJ1c2VySWQiOiIyMjJmNDQ5Yy0yYzcyLTQ5MGMtOWYzYS1iZjM3ZGJjMzVlOWUiLCJyb2xlIjoiIn0.HcgyKIyLiZRicJZb-RsRJ57XS0eI_77red0SlttB8RY")
        // args[1] = datalake root  (ex: "C:/Users/pablo/OneDrive - Universidad de Las Palmas de Gran Canaria/Universidad/Segundo/Primer Cuatrimestre/DACD/Proyectos/TFC/proyect/datalake/")
        Controller controller = new Controller(args[0], new File(args[1]));
        controller.run();
    }
}