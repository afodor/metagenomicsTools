package classExamples;

import parsers.OtuWrapper;

public class MungeCageEffectData
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = 
				new OtuWrapper("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\phylumPivotedTaxaAsColumns.txt");
		
		wrapper.writeNormalizedLoggedDataToFile("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\phylumPivotedTaxaAsColumnsLogNorm.txt");
		
		
	}
}
