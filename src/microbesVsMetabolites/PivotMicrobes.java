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
	}
}
