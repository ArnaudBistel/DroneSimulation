package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import javafx.application.HostServices;
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
    	alert.setHeaderText("Lien vers notre dépôt GitHub :");
    	Hyperlink link = new Hyperlink();
    	link.setText("https://github.com/ArnaudBistel/DroneSimulation");
    	link.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent e) {
    	    	mainApp.getHostServices().showDocument("https://github.com/ArnaudBistel/DroneSimulation");
    	    }
    	});
    	
    	alert.getDialogPane().setContent(link);
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