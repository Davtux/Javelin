package fr.unilim.application.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import fr.unilim.application.gui.util.DirectoryChooserUtil;
import fr.unilim.application.gui.util.ExceptionDialog;
import fr.unilim.application.gui.util.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerConfiguration implements Initializable {
	
	private static final Logger log = LoggerFactory.getLogger(ControllerConfiguration.class);
	
	@FXML
	private TextField tfAPI;
	
	@FXML
	private Button btnAPI;
	
	@FXML
	private TextField tfZ3;
	
	@FXML
	private Button btnZ3;
	
	@FXML
	private Button btnDone;
	
	private Stage dialogStage;
	
	private boolean okClicked;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfAPI.setText("");
		tfZ3.setText("");
		okClicked = false;
		loadSessionData();
		
	}
	
	private void loadSessionData(){
		tfAPI.setText(Config.getJavacardApiJarPath());
		tfZ3.setText(Config.getZ3BuildPath());
	}
	
	@FXML
	public void selectAPI(){
		File dir = FileChooserUtil.createFileChooser(
				btnAPI.getScene().getWindow(),
				"Select path api.jar",
				tfAPI,
				Config.getJavacardApiJarPath()
		);
		if(dir != null)
			Config.setJavacardApiJarPath(dir.toString());
	}
	
	@FXML
	public void selectZ3(){
		File dir = DirectoryChooserUtil.createDirectoryChooser(
				btnZ3.getScene().getWindow(),
				"Select path Z3",
				tfZ3,
				Config.getZ3BuildPath()
		);
		if(dir != null)
			Config.setZ3BuildPath(dir.toString());
	}
	
	@FXML
	public void done(){
		try {
			Config.saveConfiguration(Controller.APP_FILE_CONFIG);
		} catch (IOException e) {
			log.error("Can't save configuration file ({}).", Controller.APP_FILE_CONFIG, e);
			ExceptionDialog.showException(e);
			dialogStage.close();
			return;
		}
		
		okClicked = true;
		dialogStage.close();
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public boolean isOkClicked(){
		return okClicked;
	}
}
