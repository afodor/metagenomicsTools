package jobinLabRnaSeqHMMs;


public class BackwardsAlgorithm
{ 	private final double[][] logProbs;

	public double[][] getLogProbs()
	{
		return logProbs;
	}
	
	public BackwardsAlgorithm( MarkovModel model, double[] emissions, double[] foldChanges ) throws Exception
	{
		this.logProbs = new double[model.getMarkovStates().length][emissions.length];
		
		setLastValues(model,emissions.length-1);
		setValuesAfterLast(model,emissions, foldChanges);
	}
	
	private void setValuesAfterLast(MarkovModel model, double[] emissions, double[] foldChanges) throws Exception
	{
		for( int e=emissions.length-1; e > 0; e--)
		{
			
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				double logP=Double.NaN;
				
				for( int y=0; y < model.getMarkovStates().length; y++ )
				{
					double logQ = model.getMarkovStates()[x].getLogTransitionDistribution()[y]+
				    model.getMarkovStates()[y].getLogEmissionProb(emissions[e], foldChanges[e])+
				    logProbs[y][e];
				        	
				    logP = ForwardAlgorithm.logPAddedToQ(logP, logQ);
				}					//summing up all markov states to the first state
	            logProbs[x][e-1] =logP;
			}//for every state
			
		}//through all emissions 
	}
	
	private void setLastValues( MarkovModel model, int lastPos )
	{
		for(int x=0; x < model.getMarkovStates().length; x++ )
		{							
			logProbs[x][lastPos] =model.getlogTransitionProbsToTerminatingSilentState()[x];
		}
	}	
}