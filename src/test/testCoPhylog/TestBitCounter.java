package test.testCoPhylog;

import junit.framework.TestCase;
import coPhylog.BitHolder;

public class TestBitCounter extends TestCase
{

	public void test() throws Exception
	{
		BitHolder bh = new BitHolder(3);
		
		String s= "TCGATTAGAACCCAGAAAAA";
		
		
		bh.setToString(s);
		
		System.out.println( Long.toBinaryString(bh.getBits()));
		assertEquals(bh.getIndex(), 7);
		assertEquals(bh.getNumValidChars(),7);
	}

}
