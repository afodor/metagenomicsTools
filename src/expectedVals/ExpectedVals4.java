package expectedVals;

public class ExpectedVals4
{
	private static final double rewards[] = {0.5, 1, 2.5,5,7.5,13,24,35};
	
	public static void main(String[] args)
	{
		for(double w =0.4; w <=1.0; w+=0.01)
		{
			double[] probs = getProbs(w);

			double ev =0;
			
			for( int x=0; x < rewards.length; x++ )
			{
				ev += probs[x] * rewards[x];
			}
			
			System.out.println(w + " " + ev);
		}
	}
	
	private static double[] getProbs(double w)
	{
		double[] probs = new double[rewards.length];
		
		probs[0] = 1-w;
		double sum =probs[0];
		
		for( int x=1; x < probs.length -1 ; x++)
		{
			probs[x] = Math.pow(w,x) * (1-w);
			sum += probs[x];
		}
		
	//	System.out.println(sum);
		probs[probs.length-1] = 1-sum;
		
	//	for(int x=0; x < probs.length; x++)
		//	System.out.println(x + " " + probs[x]);
		
		return probs;
	}
}
