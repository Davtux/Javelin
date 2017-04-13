package fr.unilim.application.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import fr.unilim.application.gui.util.DirectoryChooserUtil;
import fr.unilim.application.gui.util.ExceptionDialog;
import fr.unilim.automaton.algorithms.AutomatonCreator;
import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.graphstream.apdapter.AutomatonGraphml;
import fr.unilim.concolic.Master;
import fr.unilim.tree.IBinaryTree;
import fr.unilim.tree.adapter.BinaryTreeJSON;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
	
	public static final String PROJECT_FILE_CONFIG = ".javelin";
	public static final String NAME_PROJECT_SRC = "SRC_DIR";
	public static final String NAME_PROJECT_PKG = "PACKAGE";
	public static final String NAME_PROJECT_APPLET = "APPLET";
	
	private Parent parent;
	private Master master;

	@FXML
	private MenuItem im_open;
	@FXML
	private MenuItem im_configuration;
	@FXML
	private Pane p_graph;
	private JPanel panel_graph;
	private Viewer viewer;
	
	public void close(){
		Platform.exit();
	}
	
	public void stop(){
		log.debug("Stop app.");
		if(viewer != null){
			viewer.close();
		}
	}

	@FXML
	void initialize() {
		log.info("Intialize application.");
		if(!Paths.get(APP_FILE_CONFIG).toFile().exists()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Configuration");
			alert.setContentText("No configuation file found (" + APP_FILE_CONFIG + ")\n"
					+ "You can configure application in File > Configuation");
			alert.showAndWait();
			setDisableApplication(true);
			return;
		}
		
		try {
			Config.loadConfigFile(APP_FILE_CONFIG);
		} catch (IOException e) {
			log.error("Can't load configuration ({})", APP_FILE_CONFIG, e);
			ExceptionDialog.showException(e);
			setDisableApplication(true);
		}
	}
	
	@FXML
	void open(){
		File dirProject = DirectoryChooserUtil.createDirectoryChooser(
				this.parent.getScene().getWindow(), "Select javacard project", null, "");
		if(dirProject == null){
			return;
		}
		File fileConf = new File(dirProject, PROJECT_FILE_CONFIG);
		if(!fileConf.exists()){
			log.info("Project file configuration not found.");
			return;
		}
		try {
			loadProjectConfiguration(dirProject, fileConf);
		} catch (IOException e) {
			log.error("Can't load project configuration.", e);
			ExceptionDialog.showException(e);
			return;
		}
		
		this.master.execute(Paths.get(Config.getZ3BuildPath()));
		createAutomaton();
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
		dialogStage.initOwner(this.parent.getScene().getWindow());
		Scene scene = new Scene(pane);
		dialogStage.setScene(scene);

		ControllerConfiguration controller = loader.getController();
		controller.setDialogStage(dialogStage);

		dialogStage.showAndWait();
		setDisableApplication(!controller.isOkClicked());
	}
	
	private void loadProjectConfiguration(File project, File configPath) throws IOException{
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(configPath);
		prop.load(input);
		
		this.master = new Master(
				Paths.get(project.toString(), prop.getProperty(NAME_PROJECT_SRC)), 
				prop.getProperty(NAME_PROJECT_APPLET),
				prop.getProperty(NAME_PROJECT_PKG)
		);
	}
	
	private void createAutomaton(){
		AutomatonCreator ac = new AutomatonCreator();
		
		FileInputStream f = null;
		try {
			f = new FileInputStream("SUT/config.jpf.json");
		} catch (FileNotFoundException e1) {
			log.error("Can't load generated file.", e1);
			ExceptionDialog.showException(e1);
			return;
		}
		
		try {		
			IBinaryTree tree = new BinaryTreeJSON(f);
			AutomatonGraphml a = (AutomatonGraphml) ac.parse(tree, new AutomatonGraphml("automaton"));
			Graph g = a.getGraph();
			g.setAttribute("layout.quality", 4);
			g.setAttribute("layout.weight", 0);
			
			
			viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
			viewer.enableAutoLayout();
			JPanel view = viewer.addDefaultView(false);
			panel_graph = new JPanel();
			panel_graph.setLayout(new BorderLayout());
			panel_graph.add(view);
			panel_graph.setPreferredSize(new Dimension((int)p_graph.getWidth(), (int)p_graph.getHeight()));
			SwingNode graphViewer = new SwingNode();
			graphViewer.setContent(panel_graph);
			p_graph.getChildren().add(graphViewer);
		} catch (ParseException e) {
			log.error("Error during parsing", e);
			ExceptionDialog.showException(e);
		} catch (AlgorithmStateException e) {
			log.error("Error algorithm state", e);
			ExceptionDialog.showException(e);
		} catch (UnsupportedEncodingException e) {
			log.error("Error unsupported encoding", e);
			ExceptionDialog.showException(e);
		} catch (IOException e) {
			log.error("Error io", e);
			ExceptionDialog.showException(e);
		}finally {
			try {
				f.close();
			} catch (IOException e) {
				log.error("Can't close file", e);
			}
		}
	}
	
	private void setDisableApplication(boolean disable){
		im_open.setDisable(disable);
	}
	
	public void setParent(Parent parent) {
		this.parent = parent;
	}
}
