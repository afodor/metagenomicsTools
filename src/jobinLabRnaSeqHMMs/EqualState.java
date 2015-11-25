package jobinLabRnaSeqHMMs;

public class EqualState implements MarkovState
{
	private static final double[] TRANS = { Math.log(0.33), Math.log(0.33), Math.log(0.33) };
	
	@Override
	public double[] getLogTransitionDistribution()
	{
		return TRANS;
	}
	
	@Override
	public String getStateName()
	{
		return "E";
	}
	
	@Override
	public double getLogEmissionProb(double inputProb, double foldChange)
	{
		return -Math.abs(inputProb);
	}
}
