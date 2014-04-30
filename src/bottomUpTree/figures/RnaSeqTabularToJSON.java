package bottomUpTree.figures;

import java.util.List;

public class RnaSeqTabularToJSON
{
	public static void main(String[] args) throws Exception
	{
		List<TabularRnaSeqParserFileLine> list = 
				TabularRnaSeqParserFileLine.parseFile("D:\\Janelle_RNA_SEQ\\spotfireFigures\\NC_101_4JSON.txt");
		
		System.out.println(list.size());
		
		
	}
}
