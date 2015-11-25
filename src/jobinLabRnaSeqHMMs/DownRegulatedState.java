package jobinLabRnaSeqHMMs;

public class DownRegulatedState implements MarkovState
{
	private static final double[] TRANSITIONS = {Math.log(0.5f), Math.log(0.5f), Math.log(0) };
	
	@Override
	public String getStateName()
	{
		return "D";
	}
	
	@Override
	public double[] getLogTransitionDistribution()
	{
		return TRANSITIONS;
	}
	
	@Override
	public double getLogEmissionProb(double inputProb, double foldChange)
	{
		if(foldChange > 0 || inputProb > 0)
			return Double.NaN;
		
		if( inputProb == 0 )
			return 1- foldChange;
		
		return Math.log( 1 - Math.exp( - Math.abs(inputProb) ));
		
	}
}
