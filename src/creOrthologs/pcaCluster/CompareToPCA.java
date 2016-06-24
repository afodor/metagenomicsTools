package creOrthologs.pcaCluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;
import utils.Spearman;

public class CompareToPCA
{
	public static void main(String[] args) throws Exception
	{
		List<List<Float>> pcaAxes = getColumns(
				ConfigReader.getCREOrthologsDir() + File.separator + "pcaCluster"
				+ File.separator + "transposedPCA.txt",true);
		
		List<List<Float>> dataAxes = getColumns(
				ConfigReader.getCREOrthologsDir() + File.separator + "pcaCluster" + 
		File.separator + "sampledBitScore_" + 3000+ "_bitScoreOrthologsKPneuOnly.txt", false);
		
		
		for( int x=0; x < dataAxes.size(); x++)
		{
			Holder h = getMax(pcaAxes, dataAxes.get(x));
			
			System.out.println(h.pcaAxis + " "+ h.spearmanR);
		}
		
		
	}
	
	private static class Holder
	{
		int pcaAxis;
		double spearmanR;
	}
	
	private static Holder getMax( List<List<Float>> pcaAxes, List<Float> data  ) throws Exception
	{
		Holder h= null;
		
		for( int x=0; x < pcaAxes.size(); x++ )
		{
			List<Float> innerList = pcaAxes.get(x);
			
			if( innerList.size() != data.size())
				throw new Exception("No");
			
			double val = Math.abs(Spearman.getSpear(innerList, data).getRs());
			
			if( h == null || h.spearmanR < val)
			{
				h = new Holder();
				h.spearmanR = val;
				h.pcaAxis = x + 1;
			}
		}
		
		return h;
	}
	
	private static List<List<Float>> getColumns(String inPath, boolean rFile) throws Exception
	{
		List<List<Float>> list = new ArrayList<List<Float>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(inPath)));
		
		String[] splits = reader.readLine().split("\t");
		
		int startPos = 1;
		
		if( rFile)
			startPos = 0;
		
		for(int x=startPos; x < splits.length; x++)
			list.add(new ArrayList<Float>());
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			splits = reader.readLine().split("\t");
			
			for( int x=1; x < splits.length; x++)
				list.get(x-1).add(Float.parseFloat(splits[x]));
		}
	
		
		reader.close();
		
		return list;
	}
}
