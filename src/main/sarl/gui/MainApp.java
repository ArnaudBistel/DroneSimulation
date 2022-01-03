package gui;
import java.io.IOException;
import java.util.ArrayList;

import controller.MainWindowController;
import controller.RootLayoutController;
import controller.ResultsController;
import io.sarl.javafx.FxApplication;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.application.Platform;

/** 
 * @author arnaud
 * 
 */
public class MainApp extends FxApplication {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainWindowController mainWindowController;
    @Override
    protected FXMLLoader doApplicationStart(Stage stage) {

    	this.primaryStage = stage;
        this.primaryStage.setTitle("Drone Simulation");

        initRootLayout();

        FXMLLoader loader = loadAndDisplayUI();
        /*stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	@Override
        	public void handle(WindowEvent event) {
        		
        	}
        	
        });*/
        
        return loader;
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(MainApp.class.getResource("/resources/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(this.getClass().getResource("style/style.css").toExternalForm());
            primaryStage.setScene(scene);
            /*
            primaryStage.setHeight(900);
            primaryStage.setWidth(1540);
            */
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            
            
            primaryStage.show();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Shows the main window inside the root layout.
     */
    public FXMLLoader loadAndDisplayUI() {
        try {
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(MainApp.class.getResource("/resources/fxml/MainWindow.fxml"));
        	AnchorPane mainApp = (AnchorPane) loader.load();
        	mainApp.getStylesheets().clear();
        	mainApp.getStylesheets().add(this.getClass().getResource("style/style.css").toExternalForm());
        	rootLayout.setCenter(mainApp);
            
        	mainWindowController  = loader.getController();
        	mainWindowController.setMainApp(this);
            
            return loader;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Opens a dialog to show statistics.
     */
    public void showStatistics() {
    	
    	this.openResultsWindow();
    	/*if (!mainWindowController.getStarted()) {
    		if (mainWindowController.getSimulation()) {
        		this.openResultsWindow();
    		}
    	} else {
    		this.openResultsWindow();       		
    	}*/
    }
    
    
    public void openResultsWindow() {
        try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("/resources/fxml/Results.fxml"));
    		AnchorPane page = (AnchorPane) loader.load();
    		Stage dialogStage = new Stage();
    		dialogStage.setTitle("Résultats de tests");
    		dialogStage.initModality(Modality.WINDOW_MODAL);
    		dialogStage.initOwner(primaryStage);
    		Scene scene = new Scene(page);
    		scene.getStylesheets().clear();
            scene.getStylesheets().add(this.getClass().getResource("style/style.css").toExternalForm());
    		dialogStage.setScene(scene);
    		ResultsController controller = loader.getController();
    		controller.init(this, this.mainWindowController);
    		this.mainWindowController.setResultsController(controller);
    		dialogStage.show();
    		
    		dialogStage.setOnHiding(new EventHandler<WindowEvent>() {
    			
    			@Override
    			public void handle(WindowEvent event) {
    				Platform.runLater(new Runnable() {
    					
    					@Override
    					public void run() {
    						//mainWindowController.startAgent();
    					}
    				});
    			}
    		});
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }
    
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public MainWindowController getMainWindowController() {
		return this.mainWindowController;
	}
	
	public void stop() {
		super.stop();
		System.out.println("App is stopping");
	}

}