package coPhylog;

public class ContextCount
{
	private int numA=0;
	private int numC=0;
	private int numG=0;
	private int numT=0;
	
	public void incrementA()
	{
		numA++;
	}
	
	public void incrementC()
	{
		numC++;
	}
	
	public void incrementG()
	{
		numG++;
	}
	
	public void incrementT()
	{
		numT++;
	}

	public int getNumA()
	{
		return numA;
	}

	public int getNumC()
	{
		return numC;
	}

	public int getNumG()
	{
		return numG;
	}

	public int getNumT()
	{
		return numT;
	}

	
	
	
}
