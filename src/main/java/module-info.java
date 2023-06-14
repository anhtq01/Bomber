module com.example.bombermangame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.bombermangame to javafx.fxml;
    exports com.example.bombermangame;
}