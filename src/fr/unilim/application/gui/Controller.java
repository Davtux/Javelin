package fr.unilim.application.gui;

import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import fr.unilim.application.gui.util.ExceptionDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Controller.class);
	
	public static final String APP_FILE_CONFIG = "Javelin.conf";
	
	@FXML
	private MenuItem it_configuration;
	
	public void close(){
		Platform.exit();
	}

	@FXML
	public void initialize() {
		log.info("Intialize application.");
		if(!Paths.get(APP_FILE_CONFIG).toFile().exists()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Configuration");
			alert.setContentText("No configuation file found (" + APP_FILE_CONFIG + ")\n"
					+ "You can configure application in File > Configuation");
			alert.showAndWait();
			// TODO disable app
			return;
		}
		
		try {
			Config.loadConfigFile(APP_FILE_CONFIG);
		} catch (IOException e) {
			log.error("Can't load configuration ({})", APP_FILE_CONFIG, e);
			ExceptionDialog.showException(e);
		}
	}
	
	@FXML
	void configuration(){
		FXMLLoader loader = new FXMLLoader(Controller.class.getResource("configuration.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e1) {
			log.error("Error when loading configuration.fxml", e1);
			return;
		}
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Configuration");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		//dialogStage.initOwner(it_configuration.getParentMenu().getParentPopup().getOwnerWindow());
		Scene scene = new Scene(pane);
		dialogStage.setScene(scene);

		ControllerConfiguration controller = loader.getController();
		controller.setDialogStage(dialogStage);

		dialogStage.showAndWait();
		if (!controller.isOkClicked()) {
			
			return;
		}
	}
}
