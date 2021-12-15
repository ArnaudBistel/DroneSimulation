package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import gui.MainApp;

public class RootLayoutController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void handleOpen() {

    }


    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Drone Simulation");
    	alert.setHeaderText("About");
    	alert.setContentText("Drone sim");
    	alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    /**
     * Opens the statistics window.
     */
    @FXML
    private void handleShowStatistics() {
    	mainApp.showStatistics();
    }
    
}