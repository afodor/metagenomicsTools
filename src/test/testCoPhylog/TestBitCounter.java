package test.testCoPhylog;

import junit.framework.TestCase;
import coPhylog.BitHolder;

public class TestBitCounter extends TestCase
{

	public void test() throws Exception
	{
		BitHolder bh = new BitHolder(3);
		
		String s= "CCCCCCCCCCCCCCCCCCCC";
		
		
		bh.setToString(s);
		
		assertEquals(bh.getIndex(), 7);
		assertEquals(bh.getNumValidChars(),7);
		assertEquals(bh.getMiddleChar(),'C');
		
		System.out.println( Long.toBinaryString(bh.getBits()));
		
	}

}
