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
	
	public static final String LACTO_CHECK_DIR = "LACTO_CHECK_DIR";
	
	public static final String MOTHUR_DIR = "MOTHUR_DIR";
	public static final String CROSSOVER_EXERCISE_DIR = "CROSSOVER_EXERCISE_DIR";
	
	public static final String ERIN_DATA_DIR = "ERIN_DATA_DIR";
	
	public static final String BLAST_DIR = "BLAST_DIR";
	public static final String E_TREE_TEST_DIR = "E_TREE_TEST_DIR";
	
	public static final String RDP_JAR_PATH= "RDP_JAR_PATH";
	public static final String MOCK_SEQ_DIR="MOCK_SEQ_DIR";
	
	public static final String UEGP_DIR = "UEGP_DIR";
	
	public static final String D3_DIR = "D3_DIR";
	
	public static final String R_DIRECTORY = "R_DIRECTORY";
	public static final String NINA_WITH_DUPLICATES_DIR = "NINA_WITH_DUPLICATES_DIR";
	
	public static final String SANDRA_RIVER_JUNE_2012_Dir = "SANDRA_RIVER_JUNE_2012_Dir";
	
	public static final String CHINA_DIR_DEC_2017 = "CHINA_DIR_DEC_2017"; 
	
	public static final String METABOLITES_CASE_CONTROL = "METABOLITES_CASE_CONTROL";
	public static final String SVM_DIR = "SVM_DIR";
	public static final String JANELLE_RNA_SEQ_DIR = "JANELLE_RNA_SEQ_DIR";
	public static final String KLEB_DIR="KLEB_DIR";
	public static final String BIG_DATA_SCALING_FACTORS = "BIG_DATA_SCALING_FACTORS";
	
	public static final String ADENONMAS_RELEASE_DIR = "ADENONMAS_RELEASE_DIR";
	
	public static final String TOPE_CHECK_DIR = "TOPE_CHECK_DIR";
	public static final String SCOTT_PILOT_DIR = "SCOTT_PILOT_DIR";
	public static final String CHINA_DIR = "CHINA_DIR";
	public static final String MBQC_DIR = "MBQC_DIR";
	public static final String RAT_SACH_REANALYSIS_DIR = "RAT_SACH_REANALYSIS_DIR";
	public static final String MICROBES_VS_METABOLITES_DIR = "MICROBES_VS_METABOLITES_DIR";
	public static final String VANDERBILT_DIR = "VANDERBILT_DIR";
	public static final String KYLIE_AGE_DIR = "KYLIE_AGE";
	public static final String KYLIE_16S_DIR= "KYLIE_16S_DIR";
	public static final String KYLIE_DROPBOX_DIR = "KYLIE_DROPBOX_DIR";
	public static final String KATIE_DIR = "KATIE_DIR";
	public static final String JOBIN_CARDIO_DIR = "JOBIN_CARDIO_DIR";
	public static final String IAN_LONGITUDINAL_DEC_2015_DIR= "IAN_LONGITUDINAL_DEC_2015_DIR";
	public static final String TOPE_CONTROL_DIR = "TOPE_CONTROL_DIR";
	public static final String HUMAN_IOWA = "HUMAN_IOWA";
	
	public static final String LAURA_DIR = "LAURA_DIR";
	
	public static final String ROSHONDA_CASE_CONTROL_DIR = "ROSHONDA_CASE_CONTROL_DIR";
	
	public static final String CHAPEL_HILL_WORKSHOP_DIR = "CHAPEL_HILL_WORKSHOP_DIR";
	
	public static final String SONJA_2016_DIR = "SONJA_2016_DIR";
	
	public static final String TOPE_DIVERTICULOSIS_DEC_2015_DIR = 
					"TOPE_DIVERTICULOSIS_DEC_2015_DIR";
	
	public static final String TOPE_DIVERTICULOSIS_JAN_2016_DIR = 
			"TOPE_DIVERTICULOSIS_JAN_2016_DIR";

	public static final String TOPE_DIVERTICULOSIS_FEB_2016_DIR = 
			"TOPE_DIVERTICULOSIS_FEB_2016_DIR";
		
	public static final String IAN_NOVEMBER_2015 = "IAN_NOVEMBER_2015";
	
	public static final String TANYA_DIR="TANYA_DIR";
	
	public static final String GORAN_TRIAL = "GORAN_TRIAL";
	
	public static final String JENNIFER_TEST_DIR = "JENNIFER_TEST_DIR";
	
	public static final String JOBIN_APRIL_2015_DIR = "JOBIN_APRIL_2015_DIR";
	
	public static final String MARK_AUG_2015_BATCH1_DIR = "MARK_AUG_2015_BATCH1_DIR";
	
	public static final String TOPE_SEP_2015_DIR = "TOPE_SEP_2015_DIR";
	
	public static final String GORAN_OCT_2015_DIR = "GORAN_OCT_2015_DIR";
	
	public static final String IAN_OCT_2015_DIR = "IAN_OCT_2015_DIR";
	
	public static final String FRAGMENT_RECRUITER_SUPPORT_DIR = "FRAGMENT_RECRUITER_SUPPORT_DIR";
	
	public static final String CRE_ORTHOLOGS_DIR = "CRE_ORTHOLOGS_DIR";
	public static final String CHS_DIR = "CHS_DIR";
	
	public static final String GORAN_LAB_RAT_DATA = "GORAN_LAB_RAT_DATA";
	
	public static final String TOPE_ONE_AT_A_TIME_DIR = "TOPE_ONE_AT_A_TIME_DIR";
	
	public static final String SANG_LAB_MAY_2016_DIR = "SANG_LAB_MAY_2016_DIR";
	
	public static final String BIOLOCK_J_DIR = "BIOLOCK_J_DIR";
	
	public static final String LYTE_NOV_2016_DIR = "LYTE_NOV_2016_DIR";
	
	public static final String MERGED_ARFF_DIR = "MERGED_ARFF_DIR";
	
	public static final String TING_DIR = "TING_DIR";
	
	public static final String LYTE_BEHAVIOR_MARCH_2017_DIR = "LYTE_BEHAVIOR_MARCH_2017_DIR";
	
	public static final String IAN_MOUSE_AUG_2017_DIR = "IAN_MOUSE_AUG_2017_DIR";
	
	public static final String EMILY_TRANSFER_PROJECT = "EMILY_TRANSFER_PROJECT";
	
	public static final String TANYA_BLOOD_DIR= "TANYA_BLOOD_DIR";
	public static final String TANYA_BLOOD_DIR2= "TANYA_BLOOD_DIR2";
	
	public static final String EMILY_JAN_2018_DIR = "EMILY_JAN_2018_DIR";
	
	public static final String EMILY_MAY_2018_DIR = "EMILY_MAY_2018_DIR";
	
	public static final String IAN_ORGANOID_DIRECTORY = "IAN_ORGANOID_DIRECTORY";
	
	public static final String KATIE_BLAST_DIR = "KATIE_BLAST_DIR";
	public static final String TANYA_FEB_2018_DIR = "TANYA_FEB_2018_DIR"; 
	public static final String FARNAZ_FEB_2018_DIR = "FARNAZ_FEB_2018_DIR";
	
	public static final String EVAN_FEB_2018_DIR = "EVAN_FEB_2018_DIR";
	
	public static final String PETER_ANTIBODY_DIR = "PETER_ANTIBODY_DIR";
	
	public static final String JAMES_EOE_DIR= "JAMES_EOE_DIR";
	
	public static String getPeterAntibodyDirectory() throws Exception
	{
		return getConfigReader().getAProperty(PETER_ANTIBODY_DIR);
	}
	
	public static String getFarnazFeb2018Directory() throws Exception
	{
		return getConfigReader().getAProperty(FARNAZ_FEB_2018_DIR);
	}
	

	public static String getJamesEoeDirectory() throws Exception
	{
		return getConfigReader().getAProperty(JAMES_EOE_DIR);
	}
	
	
	public static String getEvanFeb2018Dir() throws Exception
	{
		return getConfigReader().getAProperty(EVAN_FEB_2018_DIR);
	}
	
	public static String getLauraDir() throws Exception
	{
		return getConfigReader().getAProperty(LAURA_DIR);
	}
	
	public static String getHumanIowa() throws Exception
	{
		return getConfigReader().getAProperty(HUMAN_IOWA);
	}
	
	public static String getIanOrganoidDirectory() throws Exception
	{
		return getConfigReader().getAProperty(IAN_ORGANOID_DIRECTORY);
	}
	
	public static String getTanyaFeb2018Directory() throws Exception
	{
		return getConfigReader().getAProperty(TANYA_FEB_2018_DIR);
	}

	public static String getKatieBlastDir() throws Exception
	{
		return getConfigReader().getAProperty(KATIE_BLAST_DIR);
	}
	
	public static String getFragmentRecruiterSupportDir() throws Exception
	{
		return getConfigReader().getAProperty(FRAGMENT_RECRUITER_SUPPORT_DIR );
	}
	
	public static String getEmilyTransferProject() throws Exception
	{
		return getConfigReader().getAProperty(EMILY_TRANSFER_PROJECT);
	}
	
	public static String getEmilyJan2018Dir() throws Exception
	{
		return getConfigReader().getAProperty(EMILY_JAN_2018_DIR);
	}
	
	public static String getEmilyMay2018Dir() throws Exception
	{
		return getConfigReader().getAProperty(EMILY_MAY_2018_DIR);
	}
	
	public static String getBioLockJDir() throws Exception
	{
		return getConfigReader().getAProperty(BIOLOCK_J_DIR);
	}
	
	public static String getTanyaBloodDir() throws Exception
	{
		return getConfigReader().getAProperty(TANYA_BLOOD_DIR);
	}
	
	public static String getTanyaBloodDir2() throws Exception
	{
		return getConfigReader().getAProperty(TANYA_BLOOD_DIR2);
	}
	
	public static String getIanMouseAug2017Dir() throws Exception
	{
		return getConfigReader().getAProperty(IAN_MOUSE_AUG_2017_DIR);
	}
	
	public static String getSangLabMay2016Dir() throws Exception
	{
		return getConfigReader().getAProperty(SANG_LAB_MAY_2016_DIR);
	}
	
	public static String getUEGPDir() throws Exception
	{
		return getConfigReader().getAProperty(UEGP_DIR);
	}
	
	public static String getLyteNov2016Dir() throws Exception
	{
		return getConfigReader().getAProperty(LYTE_NOV_2016_DIR);
	}


	public static String getChinaDecember2017Dir() throws Exception
	{
		return getConfigReader().getAProperty(CHINA_DIR_DEC_2017);	
	}
	

	
	public static String getLactoCheckDir() throws Exception
	{
		return getConfigReader().getAProperty(LACTO_CHECK_DIR);	
	}
	
	public static String getTopeOneAtATimeDir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_ONE_AT_A_TIME_DIR);
	}
	
	
	public static String getMergedArffDir() throws Exception
	{
		return getConfigReader().getAProperty(MERGED_ARFF_DIR);
	}
	
	public static String getLyteBehaviorMarch2017Dir() throws Exception
	{
		return getConfigReader().getAProperty(LYTE_BEHAVIOR_MARCH_2017_DIR);
	}
	
	public static boolean isVerboseConsole() throws Exception
	{
		return getConfigReader().isSetToTrue(VERBOSE_CONSOLE);
	}
	
	public static String getTingDir() throws Exception
	{
		return getConfigReader().getAProperty(TING_DIR);
	}
	
	public static String getGoranLabRatData() throws Exception
	{
		return getConfigReader().getAProperty(GORAN_LAB_RAT_DATA );
	}
	
	public static String getTanyaDir() throws Exception
	{
		return getConfigReader().getAProperty(TANYA_DIR);
	}
	
	public static String getIanNov2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(IAN_NOVEMBER_2015);
	}
	
	public static String getJobinLabRNASeqDir() throws Exception
	{
		throw new Exception("From legacy code");
	}
	
	public static String getTopeJan2016Dir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_DIVERTICULOSIS_JAN_2016_DIR);
	}
	
	public static String getTopeFeb2016Dir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_DIVERTICULOSIS_FEB_2016_DIR);
	}
	
	public static String getTopeControlDir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_CONTROL_DIR);
	}
	
	public static String getIanLongitudnalDec2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(IAN_LONGITUDINAL_DEC_2015_DIR);
	}
	
	public static String getSonja2016Dir() throws Exception
	{
		return getConfigReader().getAProperty(SONJA_2016_DIR);
	}
	
	public static String getTopeDiverticulosisDec2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_DIVERTICULOSIS_DEC_2015_DIR);
	}
	
	public static String getMarkAug2015Batch1Dir() throws Exception
	{
		return getConfigReader().getAProperty(MARK_AUG_2015_BATCH1_DIR);
	}
	
	public static String getCREOrthologsDir() throws Exception
	{
		return getConfigReader().getAProperty(CRE_ORTHOLOGS_DIR);
	}
	
	public static String getCHSDir() throws Exception
	{
		return getConfigReader().getAProperty(CHS_DIR);
	}
	
	public static String getTopeSep2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_SEP_2015_DIR);
	}
	
	public static String getGoranOct2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(GORAN_OCT_2015_DIR);
	}
	
	public static String getJobinApril2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(JOBIN_APRIL_2015_DIR);
	}
	
	public static String getRoshondaCaseControlDir() throws Exception
	{
		return getConfigReader().getAProperty(ROSHONDA_CASE_CONTROL_DIR);
	}

	public static String getKatieDir() throws Exception
	{
		return getConfigReader().getAProperty(KATIE_DIR);
	}
	
	public static String getChapelHillWorkshopDir() throws Exception
	{
		return getConfigReader().getAProperty(CHAPEL_HILL_WORKSHOP_DIR);
	}
	
	public static String getNinaWithDuplicatesDir() throws Exception
	{
		return getConfigReader().getAProperty(NINA_WITH_DUPLICATES_DIR);
	}
	
	public static String getJobinCardioDir() throws Exception
	{
		return getConfigReader().getAProperty(JOBIN_CARDIO_DIR);
	}
	
	
	public static String getRachSachReanalysisDir() throws Exception
	{
		return getConfigReader().getAProperty(RAT_SACH_REANALYSIS_DIR);
	}
	
	public static String getIanOct2015Dir() throws Exception
	{
		return getConfigReader().getAProperty(IAN_OCT_2015_DIR);
	}
	
	
	public static String getJenniferTestDir() throws Exception
	{
		return getConfigReader().getAProperty(JENNIFER_TEST_DIR);
	}
	
	public static String getVanderbiltDir() throws Exception
	{
		return getConfigReader().getAProperty(VANDERBILT_DIR);
	}
	
	public static String getGoranTrialDir() throws Exception
	{
		return getConfigReader().getAProperty(GORAN_TRIAL);
	}
	
	public static String getKylieDropoxDir() throws Exception
	{
		return getConfigReader().getAProperty(KYLIE_DROPBOX_DIR);
	}
	
	public static String getSvmDir() throws Exception
	{
		return getConfigReader().getAProperty(SVM_DIR);
	}
	
	public static String getTopeCheckDir() throws Exception
	{
		return getConfigReader().getAProperty(TOPE_CHECK_DIR);
	}
	
	public static String getMetabolitesCaseControl() throws Exception
	{
		return getConfigReader().getAProperty(METABOLITES_CASE_CONTROL);
	}
	
	public static String getKylie16SDir() throws Exception
	{
		return getConfigReader().getAProperty(KYLIE_16S_DIR);
	}
	
	public static String getKylieAgeDir() throws Exception
	{
		return getConfigReader().getAProperty(KYLIE_AGE_DIR);
	}
	
	public static String getChinaDir() throws Exception
	{
		return getConfigReader().getAProperty(CHINA_DIR);
	}
	
	public static String getAdenomasReleaseDir() throws Exception
	{
		return getConfigReader().getAProperty(ADENONMAS_RELEASE_DIR);
	}
	
	public static String getKlebDir() throws Exception
	{
		return getConfigReader().getAProperty(KLEB_DIR);
	}
	
	public static String getMicrboesVsMetabolitesDir() throws Exception
	{
		return getConfigReader().getAProperty(MICROBES_VS_METABOLITES_DIR);
	}
	
	public static String getScottPilotDataDir() throws Exception
	{
		return getConfigReader().getAProperty(SCOTT_PILOT_DIR);
	}
	
	public static String getMbqcDir() throws Exception
	{
		return getConfigReader().getAProperty(MBQC_DIR);
	}

	public static String getMachineLearningDir() throws Exception
	{
		return getConfigReader().getAProperty(MACHINE_LEARNING_DIR);
	}
	
	public static String getSaccharineRatDir() throws Exception
	{
		return getConfigReader().getAProperty(SACCHARINE_RAT_DIR);
	}
	
	public static String getD3Dir() throws Exception
	{
		return getConfigReader().getAProperty(D3_DIR);
	}
	
	public static String getBigDataScalingFactorsDir() throws Exception
	{
		return getConfigReader().getAProperty(BIG_DATA_SCALING_FACTORS);
	}
	
	public static String getSandraRiverJune2012Dir() throws Exception
	{
		return getConfigReader().getAProperty(SANDRA_RIVER_JUNE_2012_Dir);
	}
	
	public static String getETreeTestDir() throws Exception
	{
		return getConfigReader().getAProperty(E_TREE_TEST_DIR);
	}
	
	public static String getMockSeqDir() throws Exception
	{
		return getConfigReader().getAProperty(MOCK_SEQ_DIR);
	}
	
	public static String getRDirectory() throws Exception
	{
		return getConfigReader().getAProperty(R_DIRECTORY);
	}
	
	public static String getRDPJarPath() throws Exception
	{
		return getConfigReader().getAProperty(RDP_JAR_PATH);
	}
	
	public static String getIanAnorexiaDir() throws Exception
	{
		return getConfigReader().getAProperty(IAN_ANOREXIA_DIR);
	}
	
	public static String getJanelleRNASeqDir() throws Exception
	{
		return getConfigReader().getAProperty(JANELLE_RNA_SEQ_DIR);
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
