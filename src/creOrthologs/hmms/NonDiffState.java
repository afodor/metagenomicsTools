package creOrthologs.hmms;

import jobinLabRnaSeqHMMs.MarkovState;

public class NonDiffState implements MarkovState
{
	
	private static final double[] TRANSITIONS = {Math.log(0.5f), Math.log(0.5f)};
	
	@Override
	public String getStateName()
	{
		return "diff";
	}
	
	@Override
	public double[] getLogTransitionDistribution()
	{
		return TRANSITIONS;
	}
	

	// foldChange is ignored (to avoid having to make a new interface)
	public double getLogEmissionProb(double inputProb, double foldChange)
	{
		double prob = Math.max( CreModel.MIN_PROB,  Math.exp( - Math.abs(inputProb) ));
		
		return Math.log(prob);
	}
}
