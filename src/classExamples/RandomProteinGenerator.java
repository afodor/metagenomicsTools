package classExamples;

public class RandomProteinGenerator
{
	/* constructor 
	 *  
	 *  if useUniformFrequencies == true, the random proteins have an equal probability of all 20 residues.
	 *  
	 *  if useUniformFrequencies == false, the 20 residues defined by
	 *  { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y' }
	 *  
	 *  have a distribution of 
	 *  
	 *  {0.072658f, 0.024692f, 0.050007f, 0.061087f,
        0.041774f, 0.071589f, 0.023392f, 0.052691f, 0.063923f,
        0.089093f, 0.023150f, 0.042931f, 0.052228f, 0.039871f,
        0.052012f, 0.073087f, 0.055606f, 0.063321f, 0.012720f,
        0.032955f}
	 * 
	 */
	public RandomProteinGenerator(boolean useUniformFrequencies)
	{
	}
	
	
	/*
	 * Returns a randomly generated protein of length.
	 */
	public String getRandomProtein(int length)
	{
		return null;
	}
	
	/*
	 * Returns the probability of seeing the given sequence
	 * given the underlying residue frequencies represented by
	 * this class.  For example, if useUniformFrequencies==false in 
	 * constructor, the probability of "AC" would be 0.072658 *  0.024692
	 */
	public double getExpectedFrequency(String protSeq)
	{
		return -1;
	}
	
	/*
	* calls getRandomProtein() numIterations times generating a protein with length equal to protSeq.length().
	 * Returns the number of time protSeq was observed / numIterations
	 */
	public double getFrequencyFromSimulation( String protSeq, int numInterations )
	{
		return -1;
	}
}
