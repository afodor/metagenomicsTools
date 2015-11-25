package jobinLabRNASeq;

public class TimeAnnotations
{
	public static String getAnnotation(String fileName) throws Exception
	{
		if(fileName.startsWith("7MM") || fileName.startsWith("8MM") ||
						fileName.startsWith("9MM") || fileName.startsWith("10MM") || fileName.startsWith("11MM"))
			return "18weeks";
		
		if(fileName.startsWith("1MM") || fileName.startsWith("2MM") ||
				fileName.startsWith("3MM"))
			return "2days";


		if(fileName.startsWith("4MM") || fileName.startsWith("5MM") ||
				fileName.startsWith("6MM"))
			return "12weeks";
		
		throw new Exception("Could not find annotation for " + fileName);
	}
}
