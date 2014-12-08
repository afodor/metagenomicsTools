package ruralVsUrban;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import parsers.OtuWrapper;
import utils.Avevar;
import utils.TTest;

public class QuickEffectSizes
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper =new OtuWrapper("D:\\MachineLearningJournalClub\\testData.txt");
		List<Holder> holders = getHolders(wrapper);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D:\\MachineLearningJournalClub\\effectSizes.txt")));
		writer.write("name\tmeanCase\tmeanControl\tcaseSampleSize\tcontrolSampleSize\tdiff\tpooledSD\teffectSize\tpValue\n");
		
		for(Holder h : holders ) 
		{
			writer.write(h.otuName +"\t");
			Avevar caseAve = new Avevar(h.caseVals);
			Avevar controlAve =new Avevar(h.controlVals);
			List<Double> combined = new ArrayList<Double>();
			combined.addAll(h.caseVals);
			combined.addAll(h.controlVals);
			Avevar combinedAve =new Avevar(combined);
			writer.write(caseAve.getAve() + "\t");
			writer.write(h.caseVals.size() + "\t");
			writer.write(h.controlVals.size() + "\t");
			writer.write(controlAve.getAve() + "\t");
			double diff = Math.abs(caseAve.getAve()  - controlAve.getAve() );
			writer.write(diff + "\t");
			writer.write(combinedAve.getSD() + "\t");
			writer.write((diff/combinedAve.getSD()) + "\t");
			
			double pValue = 1;
			
			try
			{
				pValue = TTest.ttestFromNumberUnequalVariance(h.caseVals, h.controlVals).getPValue();
			}
			catch(Exception ex)
			{
				
			}
			
			writer.write(pValue + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder
	{
		String otuName;
		List<Double> caseVals =new ArrayList<Double>();
		List<Double> controlVals =new ArrayList<Double>();
	}
	
	private static List<Holder> getHolders(OtuWrapper wrapper) throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
		{
			Holder h= new Holder();
			list.add(h);
			h.otuName = wrapper.getOtuNames().get(x);
			
			for( int y=0; y < wrapper.getSampleNames().size(); y++)
			{
				String name = wrapper.getSampleNames().get(y);
				
				if( name.endsWith("case"))
					h.caseVals.add(wrapper.getDataPointsUnnormalized().get(y).get(x));
				else if ( name.endsWith("control"))
					h.controlVals.add(wrapper.getDataPointsUnnormalized().get(y).get(x));
				else throw new Exception("No " + name);
			}
		}
		
		return list;
	}
}
