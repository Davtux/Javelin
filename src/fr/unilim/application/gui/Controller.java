package fr.unilim.application.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkGML;
import org.graphstream.ui.view.Viewer;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import fr.unilim.Config;
import fr.unilim.application.gui.tasks.MasterTask;
import fr.unilim.application.gui.util.DirectoryChooserUtil;
import fr.unilim.application.gui.util.ExceptionDialog;
import fr.unilim.automaton.algorithms.AutomatonCreator;
import fr.unilim.automaton.algorithms.exception.AlgorithmStateException;
import fr.unilim.automaton.graphstream.apdapter.AutomatonGraphml;
import fr.unilim.automaton.graphstream.io.OutputGraphStream;
import fr.unilim.concolic.Master;
import fr.unilim.tree.IBinaryTree;
import fr.unilim.tree.adapter.BinaryTreeJSON;
import fr.unilim.utils.FileUtil;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Controller.class);
	
	public static final String APP_FILE_CONFIG = "Javelin.conf";
	
	public static final String PROJECT_FILE_CONFIG = ".javelin";
	
	private Parent parent;

	@FXML
	private MenuItem im_open;
	@FXML
	private MenuItem im_configuration;
	@FXML
	private Menu m_project;
	@FXML
	private MenuItem im_start_generation;
	@FXML
	private MenuItem im_properties;
	@FXML
	private Pane p_graph;
	private JPanel panel_graph;
	private Viewer viewer;
	private Graph graph;
	
	@FXML
	private Label statusBarEtat;
	@FXML
	private ProgressBar statusBarProgressBar;
	
	private Master master;
	private File currentProjectDir;
	
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
					+ "You can configure application in File > Configuration");
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
		
		setDisableApplicationProject(true);
	}
	
	@FXML
	void open(){
		this.currentProjectDir = DirectoryChooserUtil.createDirectoryChooser(
				this.parent.getScene().getWindow(), "Select javacard project", null, "");
		if(this.currentProjectDir == null){
			return;
		}
		
		setDisableApplicationProject(false);
		
		File fileConf = new File(this.currentProjectDir, PROJECT_FILE_CONFIG);
		if(!fileConf.exists()){
			log.info("Project file configuration not found.");
			properties();
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
		dialogStage.initOwner(this.parent.getScene().getWindow());
		Scene scene = new Scene(pane);
		dialogStage.setScene(scene);

		ControllerConfiguration controller = loader.getController();
		controller.setDialogStage(dialogStage);

		dialogStage.showAndWait();
		
		im_properties.setDisable(
				im_properties.isDisable() 
				? !controller.isOkClicked()
				: false
		);
	}
	
	@FXML
	private void startGeneration(){
		try {
			loadProjectConfiguration(currentProjectDir, new File( currentProjectDir, PROJECT_FILE_CONFIG));
		} catch (IOException e) {
			log.error("Can't read properties file", e);
			ExceptionDialog.showException(e);
			return;
		}
		MasterTask masterTask = new MasterTask();
		masterTask.setMaster(master);
		masterTask.setOnSucceeded(
			(WorkerStateEvent event) -> {
				createAutomaton();
				statusBarProgressBar.setVisible(false);
				createGraphView(graph);
			}
		);
		masterTask.setOnRunning(
			(WorkerStateEvent event) -> {
				statusBarProgressBar.setVisible(true);
			}
		);
		masterTask.setOnFailed(
			(WorkerStateEvent event) -> {
				statusBarProgressBar.setVisible(false);
				ExceptionDialog.showException((Exception) masterTask.getException());
			}
		);
		statusBarEtat.textProperty().bind(masterTask.messageProperty());
		new Thread(masterTask).start();
	}
	
	@FXML
	private void exportGML(){
		if(graph == null){
			startGeneration();
		}
		File f = selectSaveFile("Export GML", currentProjectDir, new FileChooser.ExtensionFilter[]{
				new FileChooser.ExtensionFilter("GML", "*.gml"),
			new FileChooser.ExtensionFilter("XML", "*.xml")
		});
		
		if(f == null){
			return;
		}
		
		try {
			OutputGraphStream.exportGraph(f.toString(), graph, new FileSinkGML());
		} catch (IOException e) {
			log.error("Error, can't export graph.", e);
			ExceptionDialog.showException(e);
		}
	}
	
	@FXML
	private void exportGraphML(){
		if(graph == null){
			startGeneration();
		}
		File f = selectSaveFile("Export GraphML", currentProjectDir, new FileChooser.ExtensionFilter[]{
			new FileChooser.ExtensionFilter("XML", "*.xml"),
			new FileChooser.ExtensionFilter("GML", "*.gml")
		});
		
		if(f == null){
			return;
		}
		
		try {
			OutputGraphStream.exportGraphToGraphML(f.toString(), graph);
		} catch (IOException e) {
			log.error("Error, can't export graph.", e);
			ExceptionDialog.showException(e);
		}
	}
	
	@FXML
	private void properties(){
		FXMLLoader loader = new FXMLLoader(Controller.class.getResource("properties.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e1) {
			log.error("Error when loading properties.fxml", e1);
			return;
		}
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Properties");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(this.parent.getScene().getWindow());
		Scene scene = new Scene(pane);
		dialogStage.setScene(scene);

		ControllerProperties controller = loader.getController();
		controller.loadProperties(new File(this.currentProjectDir, PROJECT_FILE_CONFIG));
		controller.setDialogStage(dialogStage);

		dialogStage.showAndWait();
	}
	
	private void loadProjectConfiguration(File project, File configPath) throws IOException{
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(configPath);
		prop.load(input);
		
		this.master = new Master(
				Paths.get(project.toString(), prop.getProperty(ControllerProperties.NAME_PROJECT_SRC)), 
				prop.getProperty(ControllerProperties.NAME_PROJECT_APPLET),
				prop.getProperty(ControllerProperties.NAME_PROJECT_PKG)
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
			graph = a.getGraph();
			graph.setAttribute("layout.quality", 4);
			graph.setAttribute("layout.weight", 0);
			
			
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
			FileUtil.closeFile(f);
		}
	}
	
	private void createGraphView(Graph graph){
		if(viewer != null){
			viewer = null;
		}
		if(panel_graph != null){
			p_graph.getChildren().remove(0);
		}
		
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.enableAutoLayout();
		JPanel view = viewer.addDefaultView(false);
		panel_graph = new JPanel();
		panel_graph.setLayout(new BorderLayout());
		panel_graph.add(view);
		panel_graph.setPreferredSize(new Dimension((int)p_graph.getWidth(), (int)p_graph.getHeight()));
		SwingNode graphViewer = new SwingNode();
		graphViewer.setContent(panel_graph);
		p_graph.getChildren().add(graphViewer);
	}
	
	private void setDisableApplication(boolean disable){
		im_open.setDisable(disable);
	}
	
	private void setDisableApplicationProject(boolean disable){
		m_project.setDisable(disable);
	}
	
	public void setParent(Parent parent) {
		this.parent = parent;
	}
	
	public void finishAction(String message){
		statusBarEtat.setText(message);
		statusBarProgressBar.setVisible(false);
	}
	
	private File selectSaveFile(String title, File initial, FileChooser.ExtensionFilter[] filters){
		FileChooser chooser = new FileChooser();
		chooser.setTitle(title);
		String nameAllFile = "All files";
		
		if(initial == null){
			initial = new File(System.getProperty("user.dir"));
		}
		chooser.setInitialDirectory(initial);
		chooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter(nameAllFile, "*.*"));
		chooser.getExtensionFilters().addAll(filters);
		if(filters.length != 0){
			chooser.setSelectedExtensionFilter(filters[0]);			
			chooser.setInitialFileName("export" + filters[0].getExtensions().get(0).split("\\*")[1]);
		}
		
		File f = chooser.showSaveDialog(parent.getScene().getWindow());
		
		if(f != null && FileUtil.getFileExtension(f).isEmpty() 
				&& chooser.getExtensionFilters().get(0).getDescription().equals(nameAllFile)){
			f = new File(
					f.getAbsolutePath() + 
					chooser.getSelectedExtensionFilter().getExtensions().get(0).split("\\*")[1]
			);
		}
		
		return f;
	}
}
