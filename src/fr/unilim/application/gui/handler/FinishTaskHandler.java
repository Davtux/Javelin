package fr.unilim.application.gui.handler;

import java.util.List;

import fr.unilim.application.gui.Controller;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TitledPane;

public class FinishTaskHandler implements EventHandler<WorkerStateEvent> {

	private Controller controller;
	
	public FinishTaskHandler(Controller controller, List<TitledPane> activeTitledPane) {
		this.controller = controller;
	}
	
	@Override
	public void handle(WorkerStateEvent event) {
		controller.finishAction("Done.");
	}
}
