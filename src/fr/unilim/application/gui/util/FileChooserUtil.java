package fr.unilim.application.gui.util;

import java.io.File;
import java.nio.file.Paths;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FileChooserUtil {
	
	private FileChooserUtil(){}

	/**
	 * Create FileChooser with title.
	 * @param parent
	 * 		Parent window
	 * @param title
	 * 		title of FileChooser
	 * @param tf
	 * 		if tf is not null, path is write in field
	 * @param initial
	 * 		if not null file chooser is open in initial,
	 * 		but if tf contains an exists path, this path is choose.
	 * @return File selected 
	 */
	public static File createFileChooser(Window parent, String title, TextField tf, File initial){
		FileChooser chooser = new FileChooser();
		chooser.setTitle(title);
		boolean initialSet = false;
		
		if(tf != null && tf.getText() != null && !tf.getText().isEmpty()){
			File f = new File(tf.getText());
			if(f.exists()){
				if(f.isFile()){
					f = f.getParentFile();
				}
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
		
		File file = chooser.showOpenDialog(parent);
		if(file != null && tf != null)
			tf.setText(file.toString());
		return file;
	}
	
	public static File createFileChooser(Window parent, String title, TextField tf, String initial){
		File f = null;
		if(initial != null && Paths.get(initial).toFile().exists()){
			f = Paths.get(initial).toFile();
			if(f.isFile()){
				f = f.getParentFile();
			}
		}
		return createFileChooser(parent, title, tf, f);
	}
}
