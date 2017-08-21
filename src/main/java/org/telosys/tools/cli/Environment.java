package org.telosys.tools.cli;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.PropertiesManager;


public class Environment {
	
	private static final String TELOSYS_CLI_CFG = "telosys-cli.cfg" ;
	private static final String EDITOR_COMMAND  = "EditorCommand" ;
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private final CommandProvider commandProvider ;
	private final CommandsGroups  commandsGroups ;
	
	private final String jarLocation ;
	private final String osName ;
	private final String editorCommand ;
	private final String originalDirectory ;

	private       String homeDirectory ;
	private       String currentDirectory ;
	private       String currentGitHubStore = "telosys-templates-v3";
	private       String currentModel ;
	private       String currentBundle ;

	
	/**
	 * Constructor
	 * @param commandProvider
	 */
	public Environment(CommandProvider commandProvider) {
		super();
		this.commandProvider = commandProvider ;
		this.commandsGroups  = new CommandsGroups(commandProvider);
		this.jarLocation = findJarFullPath();
		this.originalDirectory = System.getProperty("user.dir"); 
		this.currentDirectory = originalDirectory ;
		this.homeDirectory    = null ;
		this.currentModel     = null ;
		this.currentBundle    = null ;
		this.osName = System.getProperty("os.name");
//		if ( osName.contains("windows") || osName.contains("Windows") ) {
//			editorCommand = "notepad.exe" ;
//		}
//		else {
//			editorCommand = "vi" ;
//		}
		this.editorCommand = findEditorCommand();
		
	}
	
	/**
	 * Returns the editor command to be used <br>
	 * The specific command if defined in the '.cfg' file or the standard default command <br>
	 * @return
	 */
	private String findEditorCommand() {
		String specificEditorCommand = getSpecificEditorCommand();
		if ( specificEditorCommand != null ) {
			return specificEditorCommand ;
		}
		else {
			return getDefaultEditorCommand();
		}
	}
	
	/**
	 * Returns the '.jar' file location (full path) 
	 * @return
	 */
	private String findJarFullPath() {
		File file = findJarFile();
		return file.toString();
	}

	/**
	 * Returns the '.jar' file 
	 * @return
	 */
	private File findJarFile() {
		URL url = ApplicationCLI.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			URI uri = url.toURI();
			return new File( uri.getPath() ) ;
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot get jar location (URISyntaxException)", e);
		}		
	}

	private File findConfigFile() {
		File file = findJarFile();
		if ( file.exists() ) {
			File parent = file.getParentFile();
			String folderPath = parent.toString();
			File cfgFile = new File( FileUtil.buildFilePath(folderPath, TELOSYS_CLI_CFG) ) ;
			if ( cfgFile.exists() && cfgFile.isFile() ) {
				return cfgFile ;
			}
		}
		return null ;
	}

	private String getSpecificEditorCommand() {
		String cmd = null ;
		File configFile = findConfigFile();
		if ( configFile.exists() ) {
			PropertiesManager pm = new PropertiesManager(configFile);
			Properties p = pm.load();
			cmd = p.getProperty(EDITOR_COMMAND);
		}
		return cmd ;
	}

	private String getDefaultEditorCommand() {
		String osName = System.getProperty("os.name");
		if ( osName.contains("windows") || osName.contains("Windows") ) {
			// Windows
			return "notepad.exe $FILE" ;
		}
		else if ( osName.contains("mac") || osName.contains("Mac") ) {
			// Mac OS
			return "open -t $FILE" ;
		}
		else {
			// Other => Linux
			return "vi $FILE" ;
		}
	}

	//---------------------------------------------------------------------------------
	public String getJarLocation() {
		return jarLocation;
	}

	//---------------------------------------------------------------------------------
	public String getOperatingSystem() {
		return osName;
	}

	//---------------------------------------------------------------------------------
	public String getEditorCommand() {
		return editorCommand;
	}

	//---------------------------------------------------------------------------------
	public String getOriginalDirectory() {
		return originalDirectory;
	}

	//---------------------------------------------------------------------------------
	public CommandProvider getCommandProvider() {
		return commandProvider;
	}
	//---------------------------------------------------------------------------------
	public CommandsGroups getCommandsGroups() {
		return commandsGroups;
	}


	//---------------------------------------------------------------------------------
	// HOME directory
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current "HOME" directory or null if not defined
	 * @return
	 */
	public String getHomeDirectory() {
		return homeDirectory;
	}

	/**
	 * Set the "HOME" directory with the given directory
	 * @param homeDirectory
	 */
	public void setHomeDirectory(String directory) {
		this.homeDirectory = directory;
	}

	/**
	 * Set the "HOME" directory with the current directory
	 */
	public void setHomeDirectory() {
		this.homeDirectory = this.currentDirectory ;
	}

	//---------------------------------------------------------------------------------
	// Current directory
	//---------------------------------------------------------------------------------
	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(String directory) {
		this.currentDirectory = directory;
	}
	
	public void resetCurrentDirectoryToHomeIfDefined() {
		if ( this.homeDirectory != null ) {
			this.currentDirectory = this.homeDirectory;
		}
		// else unchanged
	}

	//---------------------------------------------------------------------------------
	// Current GITHUB STORE
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current model name
	 * @return
	 */
	public String getCurrentGitHubStore() {
		return currentGitHubStore;
	}

	/**
	 * Set the current model name
	 * @param github
	 */
	public void setCurrentGitHubStore(String github) {
		this.currentGitHubStore = github;
	}

	//---------------------------------------------------------------------------------
	// Current MODEL
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current model name
	 * @return
	 */
	public String getCurrentModel() {
		return currentModel;
	}

	/**
	 * Set the current model name
	 * @param modelName
	 */
	public void setCurrentModel(String modelName) {
		this.currentModel = modelName;
	}

	//---------------------------------------------------------------------------------
	// Current BUNDLE
	//---------------------------------------------------------------------------------
	/**
	 * Returns the current bundle name
	 * @return
	 */
	public String getCurrentBundle() {
		return currentBundle ;
	}

	/**
	 * Set the current bundle name
	 * @param bundleName
	 */
	public void setCurrentBundle(String bundleName) {
		this.currentBundle = bundleName;
	}

	
}
