package jobinLabRnaSeqHMMs;

public interface MarkovState
{
	public double[] getLogTransitionDistribution();
	public String getStateName();
	public double getLogEmissionProb(double inputProb, double foldChange);
}
