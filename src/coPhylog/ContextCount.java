package coPhylog;

public class ContextCount
{
	private byte numA=-128;
	private byte numC=-128;
	private byte numG=-128;
	private byte numT=-128;
	
	public void incrementA()
	{
		if( numA <= 127 )
			numA++;
	}
	
	public void incrementC()
	{
		if( numC <= 127)
			numC++;
	}
	
	public void incrementG()
	{
		if( numG <= 127)
			numG++;
	}
	
	public void incrementT()
	{
		if( numT<=127)
			numT++;
	}
	
	
	public boolean isSingleton()
	{
		if( numA > -127 )
			return false;

		if( numC > -127 )
			return false;
		
		if( numG > -127 )
			return false;

		if( numT > -127 )
			return false;
		
		return true;
	}
	
	public void increment(char c)
	{
		if( c == 'A' )
			incrementA();
		else if ( c== 'C')
			incrementC();
		else if( c == 'G')
			incrementG();
		else if( c == 'T')
			incrementT();
	}

	public int getNumA()
	{
		return numA+128;
	}

	public int getNumC()
	{
		return numC+128;
	}

	public int getNumG()
	{
		return numG+128;
	}

	public int getNumT()
	{
		return numT+128;
	}

	@Override
	public String toString()
	{
		return
		"[" + (numA+128) + "," + (numC+128) + "," + (numG+128) + "," + (numT+128) + "]";
	}
	
	
}
