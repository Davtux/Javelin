package fr.unilim.application.gui;

import java.io.IOException;

import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainUI extends Application{
	
	public static final org.slf4j.Logger log = LoggerFactory.getLogger(MainUI.class);

	@Override
    public void start(Stage primaryStage) throws Exception {
		Parent root = null;
		try{
			FXMLLoader loader = new FXMLLoader(Controller.class.getResource("main_interface.fxml"));
			root = loader.load();
			final Controller controller = (Controller) loader.getController();
			controller.setParent(root);
			
			primaryStage.setOnCloseRequest(
				(WindowEvent event) -> controller.stop()
			);
		}catch(IOException ioe){
			log.error("Can't load main_interface.fxml", ioe);
			return;
		}
		
		Scene scene = new Scene(root, 650, 650);
		
        primaryStage.setTitle("Javelin");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	
	public static void main(String[] args) {
        launch(args);
    }

}
