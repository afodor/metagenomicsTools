package scripts.ratSach2.rdpAnalysis.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class TestPValuesAcrossTissue
{
	private static HashMap<String, Double> getAPValueMap(String file) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		return map;
	}
}
