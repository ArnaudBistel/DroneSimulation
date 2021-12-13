package gui;
import java.io.IOException;

import controller.MainWindowController;
import controller.RootLayoutController;
import controller.StatisticsController;
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


/** 
 * @author arnaud
 * 
 */
public class MainApp extends FxApplication {

    private Stage primaryStage;
    private BorderPane rootLayout;

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
            primaryStage.setScene(scene);
            primaryStage.setHeight(600);
            primaryStage.setWidth(800);
            
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
        	
        	rootLayout.setCenter(mainApp);
            
            MainWindowController controller = loader.getController();
            controller.setMainApp(this);
            
            return loader;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Opens a dialog to show statistics.
     */
    public void showStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/resources/fxml/Statistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StatisticsController controller = loader.getController();

            dialogStage.show();

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
	
	public void stop() {
		super.stop();
		System.out.println("App is stopping");
	}

}