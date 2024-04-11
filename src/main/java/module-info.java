module com.example.fx_currencyexchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;

    opens com.example.fx_currencyexchange to javafx.fxml;
    exports com.example.fx_currencyexchange;
}