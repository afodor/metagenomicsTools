package machineLearningExamples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.OtuWrapper;
import utils.ConfigReader;
import utils.ProcessWrapper;

public class OtuWrapperToSVMLight
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("d:\\MachineLearningJournalClub\\roc.txt")));
		writer.write("case\tscore\n");
		
		OtuWrapper wrapper = new OtuWrapper( "D:\\MachineLearningJournalClub\\testData.txt");
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			File model = writeAnInteration(wrapper, x);
			double score = getClassificationScore(wrapper, model, x);
			
			if( wrapper.getSampleNames().get(x).indexOf("case") == -1 )
				writer.write("control\t");
			else
				writer.write("case\t");
			
			writer.write(score + "\n");
		}
		
		writer.flush();  writer.close();
			
	}
	
	static void deleteOrThrow(File f) throws Exception
	{
		f.delete();
		
		if( f.exists())
			throw new Exception("No " + f.getAbsolutePath());
	}
	
	static double getClassificationScore(OtuWrapper wrapper, File modelFile, int iteration ) throws Exception
	{
		File classificationData = new File("D:\\MachineLearningJournalClub\\trainingDataSVM_classification" + iteration+ ".txt");
		deleteOrThrow(classificationData);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(classificationData
				));
		
		File svmResults= new File("D:\\MachineLearningJournalClub\\modelSVM_results" + iteration + ".txt");
		
		deleteOrThrow(svmResults);
		
		writer.write("0 ");
				
		for( int y=0; y < wrapper.getOtuNames().size(); y++)
		{
			writer.write( y +  ":"+ wrapper.getDataPointsNormalizedThenLogged().get(iteration).get(y) + " " );
		}
				
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
	
	private static File writeAnInteration(OtuWrapper wrapper, int iteration) throws Exception
	{
		File trainingData = new File("D:\\MachineLearningJournalClub\\trainingDataSVM_" + iteration + ".txt");
		deleteOrThrow(trainingData);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(trainingData
				));
		
		File svmModel = new File("D:\\MachineLearningJournalClub\\modelSVM_" + iteration + ".txt");
		
		deleteOrThrow(svmModel);
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
			if( x != iteration)
			{
				if( wrapper.getSampleNames().get(x).indexOf("case") != -1 ) 
					writer.write("1 ");
				else
					writer.write("-1 ");
				
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
}
