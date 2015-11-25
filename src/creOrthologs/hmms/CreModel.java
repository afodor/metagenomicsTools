package creOrthologs.hmms;

import jobinLabRnaSeqHMMs.MarkovModel;
import jobinLabRnaSeqHMMs.MarkovState;

public class CreModel implements MarkovModel
{
	public static final double MIN_PROB = 1e-64;
	
	private static final MarkovState[] STATES = 
		{
			new DiffState(), new NonDiffState()
		};

	@Override
	public MarkovState[] getMarkovStates()
	{
		return STATES;
	}

	@Override
	public String getModelName()
	{
		return "cre";
	}

	private static final double[] initials = { 0.5, Math.log(0.5)};
	@Override
	public double[] getlogTransitionProbsFromInitialSilentState()
	{
		return initials;
	}

	private static final double[] terminals = { Math.log(1.0/1000.0), Math.log(1.0/1000.0)};
	
	@Override
	public double[] getlogTransitionProbsToTerminatingSilentState()
	{
		return terminals;
	}

}
