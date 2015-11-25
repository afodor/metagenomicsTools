package jobinLabRnaSeqHMMs;

public interface MarkovModel
{
	abstract public MarkovState[] getMarkovStates();
	abstract public String getModelName();
	
	abstract public double[] getlogTransitionProbsFromInitialSilentState();
	
	abstract public double[] getlogTransitionProbsToTerminatingSilentState();
}
