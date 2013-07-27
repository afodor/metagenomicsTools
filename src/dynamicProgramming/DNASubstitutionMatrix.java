package dynamicProgramming;

public class DNASubstitutionMatrix implements SubstitutionMatrix
{
	public float getScore(char c1, char c2) throws Exception
	{
		
		if( ! isValidDnaChar(c1) || ! isValidDnaChar(c2) )
			return 0;
		
		if( c1 == c2)
			return 1;
		
		return -3;
	}
	
	private static boolean isValidDnaChar(char c)
	{
		if( c== 'A' || c== 'C' || c =='G' || c == 'T' )
			return true;
		
		return false;
	}
	
	public String getSubstitutionMatrixName()
	{
		return "DNA_Matrix";
	}
}
