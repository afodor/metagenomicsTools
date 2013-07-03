package utils;

import java.io.InputStream;

import java.util.Properties;

public class ConfigReader
{
	public static final String PROPERTIES_FILE = "MetagenomicsTools.properties";

	private static ConfigReader configReader = null;
	private static Properties props = new Properties();

	public static final String TRUE = "TRUE";
	public static final String YES = "YES";

	public static final String BLAST_DIRECTORY = "BLAST_DIRECTORY";
	public static final String VERBOSE_CONSOLE = "VERBOSE_CONSOLE";
	
	public static final String PANCREATITIS_DIR = "PANCREATITIS_DIR";
	
	public static final String MACHINE_LEARNING_DIR = "MACHINE_LEARNING_DIR";
	
	public static final String BURKHOLDERIA_DIR = "BURKHOLDERIA_DIR";
	
	public static boolean isVerboseConsole() throws Exception
	{
		return getConfigReader().isSetToTrue(VERBOSE_CONSOLE);
	}

	public static String getMachineLearningDir() throws Exception
	{
		return getConfigReader().getAProperty(MACHINE_LEARNING_DIR);
	}
	
	private boolean isSetToTrue(String namedProperty)
	{
		Object obj = props.get(namedProperty);

		if (obj == null)
			return false;

		if (obj.toString().equalsIgnoreCase(TRUE)
				|| obj.toString().equalsIgnoreCase(YES))
			return true;

		return false;
	}
	

	private static String getAProperty(String namedProperty) throws Exception
	{
		Object obj = props.get(namedProperty);

		if (obj == null)
			throw new Exception("Error!  Could not find " + namedProperty
					+ " in " + PROPERTIES_FILE);

		return obj.toString();
	}
	
	public static String getBurkholderiaDir() throws Exception
	{
		return getConfigReader().getAProperty(BURKHOLDERIA_DIR);
	}
	
	public static String getPancreatitisDir() throws Exception
	{
		return getConfigReader().getAProperty(PANCREATITIS_DIR);
	}
	
	private ConfigReader() throws Exception
	{
		Object o = new Object();

		InputStream in = o.getClass().getClassLoader()
				.getSystemResourceAsStream(PROPERTIES_FILE);

		if (in == null)
			throw new Exception("Error!  Could not find " + PROPERTIES_FILE
					+ " anywhere on the current classpath");

		props = new Properties();
		props.load(in);

	}

	private static synchronized ConfigReader getConfigReader() throws Exception
	{
		if (configReader == null)
		{
			configReader = new ConfigReader();
		}

		return configReader;
	}
}
