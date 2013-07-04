package test.testCoPhylog;

import junit.framework.TestCase;
import coPhylog.BitHolder;

public class TestBitCounter extends TestCase
{

	public void test() throws Exception
	{
		BitHolder bh = new BitHolder(3);
		
		String s= "CCCATTTCCCCCCCCCCCCC";
		
		
		bh.setToString(s);
		
		assertEquals(bh.getIndex(), 7);
		assertEquals(bh.getNumValidChars(),7);
		assertEquals(bh.getMiddleChar(),'A');
		
		System.out.println( Long.toBinaryString(bh.getBits()));
		
		long cBase= 0x01l;
		long expectedAnswer = cBase<< 2;
		expectedAnswer = expectedAnswer | cBase;
		expectedAnswer = expectedAnswer << 2;
		expectedAnswer = expectedAnswer | cBase;
		expectedAnswer = expectedAnswer << (32-6);
		expectedAnswer = expectedAnswer << (32);
		
		long tBase = 0x03l;
		long rightAnswer = tBase << 2;
		rightAnswer = rightAnswer | tBase;
		rightAnswer = rightAnswer << 2;
		rightAnswer = rightAnswer | tBase;
		rightAnswer = rightAnswer << (32-6);
		
		expectedAnswer = expectedAnswer | rightAnswer;
		assertEquals(expectedAnswer, bh.getBits());
		
		
	}

}
