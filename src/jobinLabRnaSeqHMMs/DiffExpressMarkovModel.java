package jobinLabRnaSeqHMMs;

public class DiffExpressMarkovModel implements MarkovModel
{
	private static final MarkovState[] STATES = 
		{
			new DownRegulatedState(), new EqualState(),new UpregulatedState()
		};
	
	@Override
	public MarkovState[] getMarkovStates()
	{	
		return STATES;
	}
	
	@Override
	public String getModelName()
	{
		return "DiffExpress";
	}
	
	private static final double[] initials = { 0, Math.log(1), 0};
	
	@Override
	public double[] getlogTransitionProbsFromInitialSilentState()
	{
		return initials;
	}
	
	private static final double[] terminals = { Math.log(1.0/1000.0), Math.log(1.0/1000.0), Math.log(1.0/1000.0) };
	
	@Override
	public double[] getlogTransitionProbsToTerminatingSilentState()
	{
		return terminals;
	}
}
