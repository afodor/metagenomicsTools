package microbesVsMetabolites;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotMicrobes
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper.transpose(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"Microbiome_Metabolomics_data.txt", 
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"Microbiome_Metabolomics_taxaAsColumns.txt"
				 , false);
		

		OtuWrapper.transpose(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"patientMetadata.txt", 
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"patientMetadataSubjectsAsColumns.txt"
				 , false);
		
		for( String s : WritaAsPhyla.LEVELS)
		{
			System.out.println(s);
			OtuWrapper.transpose(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
					 s+  "AsRows.txt", 
					ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
					s + "AsColumns.txt"
					 , false);
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
					s + "AsColumns.txt");
			
			wrapper.writeNormalizedLoggedDataToFile(new File(
					ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
					s + "AsColumnsLogNormalized.txt"));
			
		}
	}
}
