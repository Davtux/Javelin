package fr.unilim.application.gui.tasks;

import java.nio.file.Paths;

import fr.unilim.Config;
import fr.unilim.concolic.Master;
import fr.unilim.jacacoco.application.gui.concurrent.TaskException;
import javafx.concurrent.Task;

public class MasterTask extends Task<Void> {
	
	private Master master;

	@Override
	protected Void call() throws Exception {
		updateMessage("Running...");
		if(! this.master.execute(Paths.get(Config.getZ3BuildPath()))){
			throw new TaskException("Error during generation.");
		}
		updateMessage("Done");
		return null;
	}
	
	@Override
	protected void failed(){
		super.failed();
		updateMessage("Error");
	}

	public void setMaster(Master master){
		this.master = master;
	}
}
