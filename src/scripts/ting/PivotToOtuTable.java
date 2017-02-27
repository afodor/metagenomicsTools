package scripts.ting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class PivotToOtuTable
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getTingDir() 
				+ File.separator + "20170224_Casp11_DSS_16S_DeNovo.txt")));
		
		
		
		reader.close();
	}
}
