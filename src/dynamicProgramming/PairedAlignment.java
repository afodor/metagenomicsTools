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
}
