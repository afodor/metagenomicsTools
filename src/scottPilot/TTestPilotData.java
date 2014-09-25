package scottPilot;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;
import utils.TTest;

public class TTestPilotData
{
	private static class Holder implements Comparable<Holder>
	{
		String taxaName;
		List<Double> aVals = new ArrayList<Double>();
		List<Double> bVals = new ArrayList<Double>();
		double pValue = 1;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return Double.compare(this.pValue, arg0.pValue);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper( ConfigReader.getScottPilotDataDir() + File.separator + 
				"taxaAsColumnsPilot.txt" );
		
		List<Holder> list = getHolders(wrapper);
	}
	
	private static List<Holder> getHolders( OtuWrapper wrapper )
		throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
		{
			Holder h= new Holder();
			h.taxaName = wrapper.getOtuNames().get(x);
			list.add(h);
			
			for( int y=0; y < wrapper.getSampleNames().size(); y=y+2)
			{
				String aName = wrapper.getSampleNames().get(y);
				if( ! aName.endsWith("a"))
					throw new Exception("No");
				
				String bName = wrapper.getSampleNames().get(y+1);
				if( ! bName.endsWith("b") && ! bName.endsWith("c"))
					throw new Exception("No " +  wrapper.getSampleNames().get(y+1));
				
				if( ! bName.startsWith(aName.replaceAll("a", "")))
					throw new Exception("No");
				
				h.aVals.add(wrapper.getDataPointsUnnormalized().get(y).get(x));
				h.bVals.add(wrapper.getDataPointsUnnormalized().get(y+1).get(x));
			}
			
			h.pValue = TTest.pairedTTest(h.aVals, h.bVals).getPValue();
			
		}
		
		Collections.sort(list);
		return list;
	}
}
