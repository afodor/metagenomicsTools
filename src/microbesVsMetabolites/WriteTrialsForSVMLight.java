package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.ProcessWrapper;
import utils.Regression;
import utils.TabReader;

public class WriteTrialsForSVMLight
{
	static HashMap<Integer,Double> getPCOA(int component) throws Exception
	{
		HashMap<Integer, Double> map = new HashMap<Integer,Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"pcoa_Microbiome_Metabolomics_taxaAsColumns.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int key = Integer.parseInt(splits[0].replaceAll("\"", ""));
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Double.parseDouble(splits[component]));
		}
		
		return map;
	}
	
	public enum MetaboliteClass { URINE, PLASMA , BOTH } ;
	
	private static void deleteOrThrow(File file) throws Exception
	{
		file.delete();
		
		if( file.exists())
			throw new Exception("Could not delete " + file.getAbsolutePath());
	}
	
	public static Regression runATrial(MetaboliteClass metabolite, int component) throws Exception
	{
		HashMap<Integer,Double> pcoaMap = getPCOA(component);
		HashMap<Integer, List<Double>> metaboliteMap = getMetabolites(metabolite);
		
		List<Integer> keys = new ArrayList<Integer>(pcoaMap.keySet());
		int halfPoint = keys.size() / 2;
		Collections.shuffle(keys);
		
		File trainingSetFile = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"trainingSet.txt");

		deleteOrThrow(trainingSetFile);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
								trainingSetFile));
		
		for( int x=0; x < halfPoint; x++)
		{
			writer.write( pcoaMap.get(keys.get(x)) + " "  );
			
			List<Double> list = metaboliteMap.get(keys.get(x));
			
			for(  int y=0; y < list.size(); y++ )
				writer.write( (y+1) + ":" + list.get(y) + " " );
			
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
			writer.write("sample" + keys.get(x) + " " );
			
			List<Double> list = metaboliteMap.get(keys.get(x));
			
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
			int sampleID = Integer.parseInt(sToken.nextToken().replace("sample", ""));
			Double pcoaValue = pcoaMap.get(sampleID);
			
			if( pcoaValue == null)
				throw new Exception("No");
			
			actual.add(pcoaValue);
			
			predicted.add(Double.parseDouble(svmOutReader.readLine()));
		}
		
		if( svmOutReader.readLine() != null)
			throw new Exception("No");

		classifiedReader.close();
		svmOutReader.close();
		
		Regression r = new Regression();
		r.fitFromList(predicted, actual);
		return r;
		
	}
	
	private static HashMap<Integer, List<Double>> getMetabolites(MetaboliteClass metaboliteClass) throws Exception
	{

		if( metaboliteClass.equals(MetaboliteClass.BOTH))
		{
			HashMap<Integer, List<Double>> map1 = 
					getMetabolites(MetaboliteClass.PLASMA);
			
			HashMap<Integer, List<Double>> map2 = 
					getMetabolites(MetaboliteClass.URINE);
			
			if( ! map1.keySet().equals(map2.keySet()) )
				throw new Exception("Unexpected parse");
			
			for(Integer i : map1.keySet())
			{
				map1.get(i).addAll(map2.get(i));
			}
			
			return map1;
		}
		
		HashMap<Integer, List<Double>> map = new LinkedHashMap<Integer, List<Double>>();
		
		BufferedReader reader = null;
		
		if( metaboliteClass.equals(MetaboliteClass.URINE))
		{
			reader = 
					new BufferedReader(new FileReader(new File( 
							ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
							"urine_metabolites_data.txt")));
					
		}
		else if ( metaboliteClass.equals(MetaboliteClass.PLASMA))
		{
			reader = 
					new BufferedReader(new FileReader(new File( 
							ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
							"plasmaMetabolites.txt")));
		}
		else throw new Exception("No");
		
		String firstLine = reader.readLine();
		
		TabReader tr = new TabReader(firstLine);
		
		tr.nextToken(); tr.nextToken(); tr.nextToken();
		
		for(int x=1; x <= 124; x++)
		{
			int aVal = Integer.parseInt(tr.nextToken());
			
			map.put(aVal, new ArrayList<Double>());
		}
		
		if(tr.hasMore())
			throw new Exception("No " + tr.nextToken());
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			tr =new TabReader(s);
			tr.nextToken(); tr.nextToken(); tr.nextToken();
			
			for( Integer key : map.keySet())
			{
				List<Double> list =map.get(key);
				list.add(Double.parseDouble(tr.nextToken()));
			}
			
			if( tr.hasMore())
				throw new Exception("No");
				
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		Regression r = runATrial(MetaboliteClass.URINE, 1);
		System.out.println(r.getPValueForSlope() + "\n");
	}
}
