package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import microbesVsMetabolites.WriteTrialsForSVMLight.MetaboliteClass;
import parsers.OtuWrapper;
import utils.ConfigReader;
import utils.Pearson;
import utils.ProcessWrapper;
import utils.Regression;

public class WriteTrialsForSVMLightPhyla
{
	
	private static void deleteOrThrow(File file) throws Exception
	{
		file.delete();
		
		if( file.exists())
			throw new Exception("Could not delete " + file.getAbsolutePath());
	}
	
	private static class Holder
	{
		List<Double> actual;
		List<Double> predicted;
	}
	
	private static Holder runATrial(MetaboliteClass metabolite, String phyla, OtuWrapper wrapper, List<String> keys, 
			boolean scramble) 
			throws Exception
	{
		int otuIndex = wrapper.getIndexForOtuName(phyla);
		HashMap<Integer, List<Double>> metaboliteMap = WriteTrialsForSVMLight.getMetabolites(metabolite, scramble);
		
		int halfPoint = keys.size() / 2;
		
		File trainingSetFile = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"trainingSet.txt");

		deleteOrThrow(trainingSetFile);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
								trainingSetFile));
		
		for( int x=0; x < halfPoint; x++)
		{
			int sampleIndex = wrapper.getIndexForSampleName(keys.get(x));
			writer.write( wrapper.getDataPointsNormalizedThenLogged().get(sampleIndex).get(otuIndex)+ " "  );
			
			List<Double> list = metaboliteMap.get( Integer.parseInt(keys.get(x).replace("sample", "")));
			
			for(  int y=0; y < list.size(); y++ )
			{
				boolean skip = false;
				
				if(  metabolite.equals(MetaboliteClass.METADATA) && Math.abs( list.get(y) + 1 ) < 0.0001)
					skip =true;
					
				if( ! skip)
					writer.write( (y+1) + ":" + list.get(y) + " " );
				else
					System.out.println("SKIP!!!!!!!!");
				
			}
				
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		File regressModel = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator +  
				"regressionModel");
		
		deleteOrThrow(regressModel);
		
		// train with svm_learn.exe -z r trainingSet.txt regressModel
		String[] args = new String[5];
		args[0] = ConfigReader.getSvmDir() + File.separator + "svm_learn.exe";
		args[1] = "-z";
		args[2] = "r";
		args[3] = trainingSetFile.getAbsolutePath();
		args[4] = regressModel.getAbsolutePath();
		new ProcessWrapper(args);
		
		File setToClassify = new File( 
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"setToClassify.txt"	);
		deleteOrThrow(setToClassify);
		
		writer = new BufferedWriter(new FileWriter(setToClassify));
		
		for( int x=halfPoint; x < keys.size(); x++ ) 
		{
			writer.write(keys.get(x) + " " );
			
			List<Double> list = metaboliteMap.get(Integer.parseInt(keys.get(x).replace("sample", "")));
			
			for(  int y=0; y < list.size(); y++ )
				writer.write( (y+1) + ":" + list.get(y) + " " );
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		
		File svmOut = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"svmOut.txt");
		
		deleteOrThrow(svmOut);
		
		// classify with svm_classify setToClassify.txt regressModel svmOut.txt
		args = new String[4];
		args[0] = ConfigReader.getSvmDir() + File.separator + "svm_classify.exe";
		args[1] = setToClassify.getAbsolutePath();
		args[2] = regressModel.getAbsolutePath();
		args[3] = svmOut.getAbsolutePath();
		new ProcessWrapper(args);
		
		BufferedReader svmOutReader = new BufferedReader(new FileReader(svmOut));
		BufferedReader classifiedReader = new BufferedReader(new FileReader(setToClassify));
		List<Double> predicted = new ArrayList<Double>();
		List<Double> actual = new ArrayList<Double>();
		
		for(String s= classifiedReader.readLine(); s != null; s = classifiedReader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			String  sample = sToken.nextToken();
			int sampleID = wrapper.getIndexForSampleName(sample);
			
			actual.add(wrapper.getDataPointsNormalizedThenLogged().get(sampleID).get(otuIndex));
			
			predicted.add(Double.parseDouble(svmOutReader.readLine()));
		}
		
		if( svmOutReader.readLine() != null)
			throw new Exception("No");

		classifiedReader.close();
		svmOutReader.close();
		
		Holder h= new Holder();
		h.actual= actual;
		h.predicted = predicted;
		return h;
		
	}
	
		
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + "phylaAsColumns.txt");
		
		for( String s : wrapper.getOtuNames())
		{
			writeATrialFile(wrapper, s, false);
			writeATrialFile(wrapper, s, true);
		}
	}
	 
	public static void writeATrialFile(OtuWrapper wrapper, String phyla, boolean scramble) throws Exception
	{
		Random random= new Random(324234);
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
			ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
			"trials_comp"+ phyla + "_scrambled_" +scramble +  ".txt")));
		
		writer.write("pValuePlasma\trValuePlasma\tpValueUrine\trValueUrine\tpValueBoth\trValueBoth\t");
		writer.write("pValueMetadata\trValueMetadata\tpValueAll\trValueAll\n");
		
		MetaboliteClass mClass[] = { MetaboliteClass.PLASMA, MetaboliteClass.URINE, MetaboliteClass.BOTH,
				MetaboliteClass.METADATA, MetaboliteClass.ALL};
		
		for( int x=0; x < 10; x++)
		{
			List<String> keys = new ArrayList<String>(wrapper.getSampleNames());
			Collections.shuffle(keys, random);
			for( int y=0; y < mClass.length; y++)
			{
				Holder h = runATrial(mClass[y], phyla,wrapper, keys, scramble);
				Regression r = new Regression();
				r.fitFromList(h.actual, h.predicted);
				writer.write(r.getPValueForSlope()+ "\t");
				writer.write( Pearson.getPearsonR(h.actual, h.predicted) + 
						(y==4 ? "\n" : "\t") );
				writer.flush();
			}
		}
		
		writer.flush();  writer.close();
	}
}
