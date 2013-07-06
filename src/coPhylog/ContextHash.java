package coPhylog;

import java.util.HashMap;

public class ContextHash
{
	/*
	 * Not thread safe
	 */
	public static void addToHash( String s , HashMap<Long, ContextCount> map,
					int contextSize) throws Exception
	{
		BitHolder bh = new BitHolder(contextSize);
		bh.setToString(s, false);
		process(bh, map);
		
		while( bh.advance())
			process(bh, map);
		
		bh.setToString(s, true);
		process(bh, map);
		
		while( bh.advance())
			process(bh, map);
		
	}
	
	/*
	 * Not thread safe
	 */
	private static void process(BitHolder bh, HashMap<Long, ContextCount> map) throws Exception
	{
		long aLong = bh.getBits();
		
		ContextCount cc = map.get(aLong);
		
		if( cc == null)
		{
			cc = new ContextCount();
			map.put(aLong, cc);
		}
		
		cc.increment(bh.getMiddleChar());
	}
}
