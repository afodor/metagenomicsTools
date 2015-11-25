package jobinLabRnaSeqHMMs;

public interface MarkovChainLink
{
	public double getEmission();
	
	// may be null if the markovState is not known
	public MarkovState getMarkovState();
	
}
