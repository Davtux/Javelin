package fr.unilim.application.gui.util;

import javafx.scene.control.Alert;

public class ExceptionDialog {
	
	private ExceptionDialog() {}

	/**
	 * Print exception's message in dialog.
	 * @param e
	 * 		exception you want to show.
	 */
	public static void showException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
