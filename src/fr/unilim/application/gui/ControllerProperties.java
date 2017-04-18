package fr.unilim.application.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.unilim.application.gui.util.ExceptionDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerProperties implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(ControllerProperties.class);
	
	public static final String NAME_PROJECT_SRC = "SRC_DIR";
	public static final String NAME_PROJECT_PKG = "PACKAGE";
	public static final String NAME_PROJECT_APPLET = "APPLET";
	
	@FXML
	private TextField tfSrc;
	
	@FXML
	private TextField tfPkg;
	
	@FXML
	private TextField tfName;
	
	@FXML
	private Button btnDone;
	
	private Stage dialogStage;
	
	private boolean okClicked;
	
	private File propFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		okClicked = false;
		
		if(tfSrc.getText().isEmpty()){
			tfSrc.setText("src/");
		}
	}
	
	public void loadProperties(File propertiesFile){
		this.propFile = propertiesFile;
		if(!this.propFile.exists()){
			return;
		}
		Properties prop = new Properties();
		try(FileInputStream input = new FileInputStream(this.propFile)) {
			prop.load(input);
			tfSrc.setText(prop.getProperty(NAME_PROJECT_SRC, tfSrc.getText()));
			tfPkg.setText(prop.getProperty(NAME_PROJECT_PKG, tfPkg.getText()));
			tfName.setText(prop.getProperty(NAME_PROJECT_APPLET, tfName.getText()));
		} catch (FileNotFoundException e) {
			log.warn("Can't read properties file {}.", this.propFile.toString(), e);
			ExceptionDialog.showException(e);
			return;
		} catch (IOException e) {
			log.warn("Can't load properties file {].", this.propFile.toString(), e);
			ExceptionDialog.showException(e);
			return;
		}
	}
	
	@FXML
	public void done(){
		Properties prop = new Properties();
		try(FileOutputStream output = new FileOutputStream(propFile)) {
			prop.setProperty(NAME_PROJECT_SRC, tfSrc.getText());
			prop.setProperty(NAME_PROJECT_PKG, tfPkg.getText());
			prop.setProperty(NAME_PROJECT_APPLET, tfName.getText());
			prop.store(output, "");
		} catch (IOException e) {
			log.error("Can't save properties file ({}).", propFile.toString(), e);
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
