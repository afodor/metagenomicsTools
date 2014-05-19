package machineLearningExamples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.ConfigReader;
import utils.ProcessWrapper;

public class SVM_LightWithExternalCaseControl
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getCaseControlMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"D:\\raad_SupervisedClassification\\nina\\rocForCR_rare_2701.txt")));
		writer.write("case\tscore\n");
		
		OtuWrapper wrapper = new OtuWrapper( "D:\\raad_SupervisedClassification\\nina\\inputData\\nina_otus\\nina_CR_rare_2701.txt");
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{

			String caseControlVal = map.get(wrapper.getSampleNames().get(x));
		
			
			File model = writeAnInteration(wrapper, x, map);
			double score = OtuWrapperToSVMLight.getClassificationScore(wrapper, model, x);
			
			writer.write(caseControlVal + "\t");
			
			writer.write(score + "\n");
			writer.flush();
		}
		
		writer.flush();  writer.close();
	
	}
	
	private static File writeAnInteration(OtuWrapper wrapper, int iteration,HashMap<String, String> caseContolMap) 
				throws Exception
	{
		File trainingData = new File("D:\\MachineLearningJournalClub\\trainingDataSVM_" + iteration + ".txt");
		OtuWrapperToSVMLight.deleteOrThrow(trainingData);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(trainingData
				));
		
		File svmModel = new File("D:\\MachineLearningJournalClub\\modelSVM_" + iteration + ".txt");
		
		OtuWrapperToSVMLight.deleteOrThrow(svmModel);
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
			if( x != iteration)
			{
				if( caseContolMap.get(wrapper.getSampleNames().get(x)).equals("case")) 
					writer.write("1 ");
				else if( caseContolMap.get(wrapper.getSampleNames().get(x)).equals("control")) 
					writer.write("-1 ");
				else throw new Exception("No " + caseContolMap.get(wrapper.getSampleNames().get(x)));
				
				for( int y=0; y < wrapper.getOtuNames().size(); y++)
				{
					if( wrapper.getFractionZeroForTaxa(y) < 0.75)
						writer.write( y +  ":"+ wrapper.getDataPointsNormalizedThenLogged().get(x).get(y) + " " );
				}
				
				writer.write("\n");
					
			}
		writer.flush();  writer.close();
		
		String[] args = new String[3];
		args[0] = ConfigReader.getSvmDir() + File.separator + "svm_learn";
		args[1] = trainingData.getAbsolutePath();
		args[2] = svmModel.getAbsolutePath();
		new ProcessWrapper(args);
		
		
		
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
