/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


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
	public static final String BIG_BLAST_DIR = "BIG_BLAST_DIR";
	
	public static final String GENBANK_CACHE_DIR = "GENBANK_CACHE_DIR";
	public static final String REDUCE_OTU_DIR = "REDUCE_OTU_DIR";
	
	public static final String SACCHARINE_RAT_DIR = "SACCHARINE_RAT_DIR";
	
	public static final String IAN_ANOREXIA_DIR = "IAN_ANOREXIA_DIR";
	
	public static final String MOTHUR_DIR = "MOTHUR_DIR";
	public static final String CROSSOVER_EXERCISE_DIR = "CROSSOVER_EXERCISE_DIR";
	
	public static final String ERIN_DATA_DIR = "ERIN_DATA_DIR";
	
	public static final String BLAST_DIR = "BLAST_DIR";
	public static final String E_TREE_TEST_DIR = "E_TREE_TEST_DIR";
	
	public static final String RDP_JAR_PATH= "RDP_JAR_PATH";
	public static final String MOCK_SEQ_DIR="MOCK_SEQ_DIR";
	
	public static boolean isVerboseConsole() throws Exception
	{
		return getConfigReader().isSetToTrue(VERBOSE_CONSOLE);
	}

	public static String getMachineLearningDir() throws Exception
	{
		return getConfigReader().getAProperty(MACHINE_LEARNING_DIR);
	}
	
	public static String getSaccharineRatDir() throws Exception
	{
		return getConfigReader().getAProperty(SACCHARINE_RAT_DIR);
	}
	
	public static String getETreeTestDir() throws Exception
	{
		return getConfigReader().getAProperty(E_TREE_TEST_DIR);
	}
	
	public static String getMockSeqDir() throws Exception
	{
		return getConfigReader().getAProperty(MOCK_SEQ_DIR);
	}
	
	public static String getRDPJarPath() throws Exception
	{
		return getConfigReader().getAProperty(RDP_JAR_PATH);
	}
	
	public static String getIanAnorexiaDir() throws Exception
	{
		return getConfigReader().getAProperty(IAN_ANOREXIA_DIR);
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
	
	public static String getReducedOTUDir() throws Exception
	{
		return getConfigReader().getAProperty(REDUCE_OTU_DIR);
	}

	private static String getAProperty(String namedProperty) throws Exception
	{
		Object obj = props.get(namedProperty);

		if (obj == null)
			throw new Exception("Error!  Could not find " + namedProperty
					+ " in " + PROPERTIES_FILE);

		return obj.toString();
	}
	
	public static String getBlastDirectory() throws Exception
	{
		return getConfigReader().getAProperty(BLAST_DIR);
	}
	
	public static String getBurkholderiaDir() throws Exception
	{
		return getConfigReader().getAProperty(BURKHOLDERIA_DIR);
	}
	
	public static String getPancreatitisDir() throws Exception
	{
		return getConfigReader().getAProperty(PANCREATITIS_DIR);
	}
	
	public static String getBigBlastDir() throws Exception
	{
		return getConfigReader().getAProperty(BIG_BLAST_DIR);
	}
	
	public static String getMothurDir() throws Exception
	{
		return getConfigReader().getAProperty(MOTHUR_DIR);
	}
	
	public static String getErinDataDir() throws Exception
	{
		return getConfigReader().getAProperty(ERIN_DATA_DIR);
	}
	
	public static String getGenbankCacheDir() throws Exception
	{
		return getConfigReader().getAProperty(GENBANK_CACHE_DIR);
	}
	
	public static String getCrossoverExerciseDir() throws Exception
	{
		return getConfigReader().getAProperty(CROSSOVER_EXERCISE_DIR);
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
