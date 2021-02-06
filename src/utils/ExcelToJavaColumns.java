package utils;

public class ExcelToJavaColumns
{
	public static void main(String[] args) throws Exception
	{
		int index =0;
		
		for ( char c2 = 65; c2 <=65+25; c2++)
		{
			String excelCol = "" + c2;
			System.out.println( excelCol + " " + index  );
			index++;
		}
		
		for( char c = 65; c <=68; c++)
			for ( char c2 = 65; c2 <=65+25; c2++)
			{
				String excelCol = "" + c + c2;
				System.out.println( excelCol + " " + index  );
				index++;
			}
	}
}
