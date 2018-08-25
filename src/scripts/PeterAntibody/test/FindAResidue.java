package scripts.PeterAntibody.test;

import java.util.Map;

import scripts.PeterAntibody.CountFeatures;


public class FindAResidue
{
	public static void main(String[] args) throws Exception
	{
		Map< String, Map<String,Map<String,Character>>> map = CountFeatures.getMap();
		
		for(String s : map.keySet())
			System.out.println(s);
		
		 Map<String,Map<String,Character>> map2 = map.get("mouse_germline");
		 
		 for(String s : map2.keySet())
			 System.out.println(s);
	}
}
