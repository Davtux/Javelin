package fr.unilim.application.gui.util;

import java.io.File;
import java.nio.file.Paths;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class DirectoryChooserUtil {

	/**
	 * Create DirectoryChooser with title.
	 * @param parent
	 * 		Parent window
	 * @param title
	 * 		title of DirectoryChooser
	 * @param tf
	 * 		if tf is not null, path is write in field
	 * @param initial
	 * 		if not null directory chooser is open in initial,
	 * 		but if tf contains an exists path, this path is choose.
	 * @return File selected  
	 */
	public static File createDirectoryChooser(Window parent, String title, TextField tf, File initial){
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle(title);
		boolean initialSet = false;
		
		if(tf != null && tf.getText() != null && !tf.getText().isEmpty()){
			File f = new File(tf.getText());
			if(f.exists()){
				chooser.setInitialDirectory(f);
				initialSet = true;
			}
		}
		if(!initialSet){
			if(initial != null){
				chooser.setInitialDirectory(initial);
			}else{
				File f = new File(System.getProperty("user.dir"));
				chooser.setInitialDirectory(f);
			}
		}
		
		File dir = chooser.showDialog(parent);
		if(dir != null && tf != null)
			tf.setText(dir.toString());
		return dir;
	}
	
	/**
	 * Create DirectoryChooser with title.
	 * @param parent
	 * 		Parent window
	 * @param title
	 * 		title of DirectoryChooser
	 * @param tf
	 * 		if tf is not null, path is write in field
	 * @param initial
	 * 		if not null directory chooser is open in initial,
	 * 		but if tf contains an exists path, this path is choose.
	 * @return File selected 
	 */
	public static File createDirectoryChooser(Window parent, String title, TextField tf, String initial){
		File f = null;
		if(initial != null && Paths.get(initial).toFile().exists()){
			f = Paths.get(initial).toFile();
		}
		return createDirectoryChooser(parent, title, tf, f);
	}
}
