package dynamicProgramming;

public abstract class PairedAlignment
{  
    abstract public String getFirstSequence();
	abstract public String getSecondSequence();
	abstract public float getAlignmentScore();
	
	@Override
	public String toString()
	{
		return getFirstSequence() + "\n" + getSecondSequence() + "\n" +
					getAlignmentScore();
	}
	
	public int getNumMatchesExcludingNs()
	{
		int sum =0;
		
		String s1 = getFirstSequence();
		String s2 = getSecondSequence();
		
		for( int x=0; x < s1.length(); x++)
		{
			char c1 = s1.charAt(x);
			char c2 = s2.charAt(x);
			
			if( c1 != '-' && c1 != 'N' && c1 == c2)
				sum++;
		}
		
		return sum;
	}
	
	public String getMiddleString()
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < getFirstSequence().length();x++)
		{
			if( getFirstSequence().charAt(x) == getSecondSequence().charAt(x) )
				buff.append("|");
			else 
				buff.append("*");
		}
		
		return buff.toString();
	}
}
