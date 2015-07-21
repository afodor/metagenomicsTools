package compositionality;

import java.util.List;

public class SpuriousInference
{
	public static final int SAMPLE_SIZE = 50;
	public static final int HIGH_ABUNDANCE = 10000;
	public static final int LOW_ABUNDNACE = 1000;
	private static final float HIGH_ABUNDANCE_MEAN = 10000f;
	private static final float HIGH_ABUNDACE_SD = HIGH_ABUNDANCE_MEAN/5;
	private static final float B_GROUP_MULTIPLIER = 0.5f;
	
	private static final float LOW_ABUNDANCE_MEAN = 1000f;
	private static final float LOW_ABUNDNACE_SD = LOW_ABUNDANCE_MEAN/20;
	
	
	public static void main(String[] args) throws Exception
	{
		List<Double> aList = 
				SpuriousResampling.populateGaussian(HIGH_ABUNDANCE_MEAN, HIGH_ABUNDACE_SD, SAMPLE_SIZE);
		
		
		
	}
}
