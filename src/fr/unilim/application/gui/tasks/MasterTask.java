package fr.unilim.application.gui.tasks;

import java.nio.file.Paths;

import fr.unilim.Config;
import fr.unilim.concolic.Master;
import javafx.concurrent.Task;

public class MasterTask extends Task<Void> {
	
	private Master master;

	@Override
	protected Void call() throws Exception {
		updateMessage("Running...");
		this.master.execute(Paths.get(Config.getZ3BuildPath()));
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
