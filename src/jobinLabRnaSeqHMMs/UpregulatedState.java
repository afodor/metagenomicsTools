package jobinLabRnaSeqHMMs;

public class UpregulatedState implements MarkovState
{
	private static final double[] TRANSITIONS = {Math.log(0), Math.log(0.5), Math.log(0.5)};
	
	@Override
	public double getLogEmissionProb(double inputProb, double foldChange)
	{
		if(foldChange < 0 || inputProb < 0)
			return Double.NaN;
		
		if( inputProb == 0 )
			return 1- 1/foldChange;
		
		return Math.log( 1 - Math.exp( - Math.abs(inputProb) ));
	}
	
	@Override
	public double[] getLogTransitionDistribution()
	{
		return TRANSITIONS;
	}
	
	@Override
	public String getStateName()
	{
		return "U";
	}
}
