package machineLearningExamples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.ProcessWrapper;

public class SVM_LightWithExternalCaseControl
{
	private static HashMap<String, String> getFileLines(String filepath) throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String key = s.split("\t")[0];
			
			if( map.containsKey(key) )
				throw new Exception("No");
			
			map.put(key, s);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> fileLines = 
				getFileLines("D:\\raad_SupervisedClassification\\nina\\inputData\\nina_otus\\nina_DN_rare_2765.txt" );
		
		HashMap<String, String> map = getCaseControlMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"D:\\raad_SupervisedClassification\\nina\\rocForDN_rare_2765.txt")));
		writer.write("case\tscore\n");
		
		for( String s : map.keySet())
		{

			String caseControlVal = map.get(s);
			
			File model = writeAnInteration(fileLines,s, map);
			double score = getClassificationScore(fileLines.get(s), model, s);
			
			writer.write(caseControlVal + "\t");
			
			writer.write(score + "\n");
			writer.flush();
		}
		
		writer.flush();  writer.close();
	
	}
	
	static double getClassificationScore(String fileLine, File modelFile, String key) throws Exception
	{
		File classificationData = new File("D:\\MachineLearningJournalClub\\trainingDataSVM_classification" + key+ ".txt");
		OtuWrapperToSVMLight.deleteOrThrow(classificationData);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(classificationData
				));
		
		File svmResults= new File("D:\\MachineLearningJournalClub\\modelSVM_results" + key+ ".txt");
		
		OtuWrapperToSVMLight.deleteOrThrow(svmResults);
		
		writer.write("0 ");
				
		String[] splits = fileLine.split("\t");
				
		for( int x=1; x < splits.length; x++)
			writer.write( x + ":" + splits[x] + " " );
				
		writer.write("\n");
					
		writer.flush();  writer.close();
		
		String[] args = new String[4];
		args[0] = ConfigReader.getSvmDir() + File.separator + "svm_classify";
		args[1] = classificationData.getAbsolutePath();
		args[2] = modelFile.getAbsolutePath();
		args[3] = svmResults.getAbsolutePath();
		
		for( int x=0; x < args.length; x++)
			System.out.println(args[x]);
		
		new ProcessWrapper(args);
		
		if(! svmResults.exists())
			throw new Exception("Could not find " + svmResults.getAbsolutePath());
		
		BufferedReader reader = new BufferedReader(new FileReader(svmResults));
		double returnVal = Double.parseDouble(reader.readLine());
		reader.close();
		
		return returnVal;
	}
	
	private static File writeAnInteration(HashMap<String, String> fileLines, String key,HashMap<String, String> caseContolMap) 
				throws Exception
	{
		File trainingData = new File("D:\\MachineLearningJournalClub\\trainingDataSVM_" + key+ ".txt");
		OtuWrapperToSVMLight.deleteOrThrow(trainingData);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(trainingData
				));
		
		File svmModel = new File("D:\\MachineLearningJournalClub\\modelSVM_" + key+ ".txt");
		
		OtuWrapperToSVMLight.deleteOrThrow(svmModel);
		
		for( String aKey: fileLines.keySet())
			if( ! aKey.equals(key))
			{
				if( caseContolMap.get(aKey).equals("case")) 
					writer.write("1 ");
				else if( caseContolMap.get(aKey).equals("control")) 
					writer.write("-1 ");
				else throw new Exception("No " + aKey);
				
				String[] splits = fileLines.get(aKey).split("\t");
				
				for( int x=1; x < splits.length; x++)
					writer.write( x + ":" + splits[x] + " " );
				
				writer.write("\n");
					
			}
		writer.flush();  writer.close();
		
		String[] args = new String[3];
		args[0] = ConfigReader.getSvmDir() + File.separator + "svm_learn";
		args[1] = trainingData.getAbsolutePath();
		args[2] = svmModel.getAbsolutePath();
		
		System.out.println(args);
		new ProcessWrapper(args);
		
		if( ! svmModel.exists())
			throw new Exception("Could not find " + svmModel.getAbsolutePath());
		
		return svmModel;
	}
	
	private static HashMap<String, String> getCaseControlMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"D:\\raad_SupervisedClassification\\nina\\inputData\\nina_map_file.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			if(map.containsKey(splits[0]))
				throw new Exception("Duplicate");
			
			map.put(splits[0], splits[2]);
		}
		
		return map;
	}
}	
