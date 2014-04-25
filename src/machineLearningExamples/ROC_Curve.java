package machineLearningExamples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import sun.util.locale.StringTokenIterator;
import utils.ConfigReader;

public class ROC_Curve
{
	private static class Holder implements Comparable<Holder>
	{
		double score;
		boolean isCase;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return Double.compare(arg0.score, this.score);
		}
	}
	
	private static void writeResults(List<Holder> list) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D:\\MachineLearningJournalClub\\rocOut.txt")));
		writer.write("numCase\tnumControl\n");
		
		int numCase =0;
		int numControl =0;
		
		for( Holder h : list )
		{
			if(h.isCase )
				numCase++;
			else
				numControl++;
			
			writer.write(numCase + "\t");
			writer.write(numControl + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static List<Holder> getList() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\MachineLearningJournalClub\\roc.txt")));
		
		List<Holder> list = new ArrayList<ROC_Curve.Holder>();
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			Holder h= new Holder();
			h.isCase = sToken.nextToken().equals("case");
			h.score = Double.parseDouble(sToken.nextToken());
			list.add(h);
		}
		
		Collections.sort(list);
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list =  getList();
		writeResults(list);
	}
}
