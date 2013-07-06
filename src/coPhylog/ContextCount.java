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
	
	public int getTotal()
	{
		return numA + numC + numG + numT;
	}
	
	public boolean isSingleton()
	{
		if( numA > 1 )
			return false;

		if( numC > 1 )
			return false;
		
		if( numG > 1 )
			return false;

		if( numT > 1 )
			return false;
		
		return true;
	}
	
	public void increment(char c)
	{
		if( c == 'A' )
			numA++;
		else if ( c== 'C')
			numC++;
		else if( c == 'G')
			numG++;
		else if( c == 'T')
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

	@Override
	public String toString()
	{
		return
		"[" + numA + "," + numC + "," + numG + "," + numT + "]";
	}
	
	
}
