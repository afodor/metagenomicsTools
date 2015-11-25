package jobinLabRnaSeqHMMs;

public class ForwardAlgorithm
{
	// the first index corresponds to state (k)
	// the second index is the prob. corresponding to each emission
	private final double[][] logProbs;
	
	public ForwardAlgorithm( MarkovModel model, double[] chainEmissions, double[] foldChanges) throws Exception
	{
		this.logProbs = new double[model.getMarkovStates().length][chainEmissions.length];
		//  a hack here- we always start in the equal state - will not be general to other models
		logProbs[0][0] = Double.NaN;
		logProbs[1][0] = -chainEmissions[0];
		logProbs[2][0] = Double.NaN;
		
		setValuesAfterFirst(model, chainEmissions, foldChanges);
	}
	
	
	public double[][] getLogProbs()
	{
		return logProbs;
	}
	
	private void setValuesAfterFirst(MarkovModel model, double[] chainEmissions, double[] foldChanges) 
		throws Exception
	{
		for( int e=1; e < chainEmissions.length; e++)
		{
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				double sum = logProbs[0][e-1] + 
							model.getMarkovStates()[0].getLogTransitionDistribution()[x];
				
				for( int y=1; y < model.getMarkovStates().length; y++ )
				{
					double logP = sum;
					
					double logQ = logProbs[y][e-1] + 
							model.getMarkovStates()[y].getLogTransitionDistribution()[x];
					
					sum = logPAddedToQ(logP, logQ);
					
				}
					
				logProbs[x][e] = 
					model.getMarkovStates()[x].getLogEmissionProb(chainEmissions[e],
							foldChanges[e])+ sum;
			}
		}
		
	}
	
	public static double logPAddedToQ(double logP, double logQ)
	{
		if( Double.isNaN(logP) || Double.isInfinite(logP) )
			return logQ;
		
		if( Double.isNaN(logQ) || Double.isInfinite(logQ))
			return logP;
		
		return logP + Math.log(1 + Math.exp(logQ - logP));
	}
	
}
