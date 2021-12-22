package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

import java.io.File;

import gui.MainApp;
import javafx.stage.FileChooser;


public class RootLayoutController {

    private MainApp mainApp;
    
    @FXML
    private BorderPane rootPane;
    

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        //rootPane.scaleXProperty().set(0.7);
        //rootPane.scaleXProperty().set(0.7);
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
    
    @FXML
    private void handleImport() {
    	if (mainApp.getMainWindowController().getAppManager() != null) {
    		System.out.println("Try to import");
	    	FileChooser fileChooser = new FileChooser();
	    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
	        fileChooser.setTitle("Import csv file");
	        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
	        if (file != null ) {
	        	mainApp.getMainWindowController().importData(file.getPath());
	        }
    	}
    }
    
    @FXML
    private void handleExport() {
    	if (mainApp.getMainWindowController().getAppManager() != null) {
    		System.out.println("Try to export");
	    	FileChooser fileChooser = new FileChooser();
	    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
	        fileChooser.setTitle("Export csv file");
	        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
	        if (file != null ) {
	        	mainApp.getMainWindowController().exportData(file.getPath());
	        }
    	}
    }
    
}