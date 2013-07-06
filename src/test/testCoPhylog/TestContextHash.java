package test.testCoPhylog;

import java.util.HashMap;
import java.util.LinkedHashMap;

import junit.framework.TestCase;

import org.junit.Test;

import coPhylog.BitHolder;
import coPhylog.ContextCount;
import coPhylog.ContextHash;

public class TestContextHash extends TestCase
{

	@Test
	public void test() throws Exception
	{
		String s= "AACAATTAACAA";
		
		HashMap<Long, ContextCount> map = new LinkedHashMap<>();
		
		ContextHash.addToHash(s, map, 2);
		
		for(Long l : map.keySet())
		{
			ContextCount cc = map.get(l);
			System.out.println( l + " " + BitHolder.getContext(l, 2) + " " + cc );
		}
			
	}

}
