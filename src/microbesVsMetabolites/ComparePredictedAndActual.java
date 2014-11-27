package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class ComparePredictedAndActual
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, Double> pcoaValues = WriteUrineForSVMLight.getPCOA();
		
		BufferedReader samplesToClassify = new BufferedReader(new FileReader(new File(
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"setToClassify.txt"	)));
		
		BufferedReader svmResults = new BufferedReader(new FileReader(new File(
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"svmOut.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(  
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"predictedVsActual.txt")));
		writer.write("sample\tactual\tpredicted\n");
		
		for(String s= samplesToClassify.readLine(); s != null; s = samplesToClassify.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			int sampleID = Integer.parseInt(sToken.nextToken().replace("sample", ""));
			writer.write(sampleID + "\t" + pcoaValues.get(sampleID) + "\t");
			
			double svmVal = Double.parseDouble(svmResults.readLine());
			writer.write(svmVal + "\n");
			
		}
		
		if( svmResults.readLine() != null)
			throw new Exception("No");
		
		writer.flush();  writer.close();
	}
}
