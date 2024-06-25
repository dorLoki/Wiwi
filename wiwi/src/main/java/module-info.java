module net.heydel {
    requires javafx.controls;
    requires javafx.fxml;

    opens net.heydel to javafx.fxml;
    exports net.heydel;
}
