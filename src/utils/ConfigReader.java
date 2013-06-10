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
	
	public static boolean isVerboseConsole() throws Exception
	{
		return getConfigReader().isSetToTrue(VERBOSE_CONSOLE);
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
