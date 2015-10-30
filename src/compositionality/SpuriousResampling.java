package compositionality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpuriousResampling
{
	private static final Random RANDOM = new Random();
	private static final int SAMPLE_SIZE = 500;
	private static final float HIGH_ABUNDANCE_MEAN = 10000f;
	private static final float HIGH_ABUNDNACE_SD = HIGH_ABUNDANCE_MEAN/5;
	
	private static final float LOW_ABUNDANCE_MEAN = 5000f;
	private static final float LOW_ABUNDNACE_SF = LOW_ABUNDANCE_MEAN/20;
	
	private static final String HIGH = "HIGH";
	private static final String LOW1= "LOW1";
	private static final String LOW2 = "LOW2";
	
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			"c:\\temp\\SimulatedSpuriousCorrelationsAllGaussian.txt")));
		
		writer.write("category\tabundant1\tlow1\tlow2\tresampledAbundnat1\tresampledLow1\tresampledLow2\tresampledRatio1\tresampledRatio2\n");
		
		List<Double> abundant1 = 
			populateGaussian(HIGH_ABUNDANCE_MEAN, HIGH_ABUNDNACE_SD, 2*SAMPLE_SIZE);
		
		List<String> categories= new ArrayList<String>();
		
		for( int x=0 ; x < SAMPLE_SIZE * 2;x++)
		{
			if( RANDOM.nextFloat() <= 0.5f)
			{
				categories.add("A");
				abundant1.set(x, abundant1.get(x) * 5 );
			}
			else
			{
				categories.add("B");
			}
		}
		
		List<Double> low1 = populateGaussian(LOW_ABUNDANCE_MEAN, LOW_ABUNDNACE_SF, 2*SAMPLE_SIZE);
		List<Double> low2 = populateGaussian(LOW_ABUNDANCE_MEAN, LOW_ABUNDNACE_SF, 2*SAMPLE_SIZE);
		
		for( int x=0; x < SAMPLE_SIZE * 2; x++)
		{
			writer.write(categories.get(x) + "\t");
			writer.write( abundant1.get(x)+ "\t" );
			writer.write( low1.get(x) + "\t" );
			writer.write( low2.get(x) + "\t" );
			
			List<String> sample = resample(abundant1.get(x), low1.get(x), low2.get(x));
		
			int highCount =0;
			int lowCount1=0;
			int lowCount2=0;
			
			for(String s : sample)
			{
				if( s == HIGH)
					highCount++;
				else if (s == LOW1)
					lowCount1++;
				else if ( s== LOW2)
					lowCount2++;
				else throw new Exception("Logic error");
			}
			
			writer.write(highCount + "\t");
			writer.write(lowCount1 + "\t");
			writer.write(lowCount2 + "\t");
			
			double geoMean = geoMean(lowCount1, lowCount2, highCount);
			
			writer.write( (lowCount1 / geoMean) + "\t" );
			writer.write( (lowCount2 / geoMean) + "\n" );
		}

		writer.flush();  writer.close();
	}
	
	private static double geoMean(double x,double y, double z)
	{
		System.out.println(x + " " +y + " "+z);
		return Math.log( Math.pow(x*y*z, (1f/3f)));
	}
	
	static List<String> resample( double abundant1,double low1, double low2 )
	{
		List<String> list = new ArrayList<String>();
		List<String> returnList = new ArrayList<String>();
		
		for( int x=0; x < abundant1 + 0.5; x++ )
			list.add(HIGH);
		
		for( int x=0; x < low1 + 0.5; x++)
			list.add(LOW1);
		
		for( int x=0; x < low2 + 0.5; x++)
			list.add(LOW2);	
		
		Collections.shuffle(list);
		
		for( int x=0; x< 2000; x++)
			returnList.add (list.get(x));
		
		return returnList;
	}

	
	static List<Double> populateGaussian(double av, double sd, int n)
		throws Exception
	{
		
		List<Double> list = new ArrayList<Double>();
		
		for( int x=0;x <n;x++)
			list.add(av + RANDOM.nextGaussian()*sd);
		
		return list;
	}
}
