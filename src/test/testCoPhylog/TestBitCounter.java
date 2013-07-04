package test.testCoPhylog;

import junit.framework.TestCase;
import coPhylog.BitHolder;

public class TestBitCounter extends TestCase
{

	public void testAdvance() throws Exception
	{
		BitHolder bh = new BitHolder(2);
		assertEquals(bh.getContextSize(), 2);
		
		String s= "TTACCG";
		
		assertEquals( bh.setToString(s), true);
		assertEquals( bh.getNumValidChars(), 5);
		assertEquals( bh.getMiddleChar(), 'A');
		assertEquals( bh.getIndex(), 4);
		
		
		long tBase= 0x03l;
		long expectedAnswer = tBase<< 2;
		expectedAnswer = expectedAnswer | tBase;
		expectedAnswer = expectedAnswer << 32 + (32-4);
		
		long cBase= 0x01l;
		long rightAnswer = cBase<< 2;
		rightAnswer= rightAnswer | cBase;
		rightAnswer = rightAnswer << (32-4);
		
		expectedAnswer = expectedAnswer | rightAnswer;
		assertEquals(expectedAnswer, bh.getBits());
		
		assertEquals( bh.advance(), true); 
		assertEquals( bh.getNumValidChars(), 6);
		assertEquals( bh.getMiddleChar(), 'C');
		assertEquals(bh.getIndex(), 5);
		assertEquals(s.charAt(bh.getIndex()), 'G');
		
		System.out.println( Long.toBinaryString(bh.getBits()) );
		
		expectedAnswer = tBase;
		expectedAnswer = expectedAnswer | tBase;
		expectedAnswer = expectedAnswer << 32 + (32-2);
		
		rightAnswer = cBase;
		rightAnswer = rightAnswer << 2;
		rightAnswer = rightAnswer | 0x2l;
		rightAnswer = rightAnswer << (32-4);
		
		expectedAnswer = expectedAnswer | rightAnswer;
		System.out.println( Long.toBinaryString(expectedAnswer) );
		
		assertEquals(expectedAnswer, bh.getBits());
	}
	
	public void testInitial() throws Exception
	{
		BitHolder bh = new BitHolder(3);
		
		String s= "CCCATTTCCCCCCCCCCCCC";
		
		
		assertEquals( bh.setToString(s),true);
		
		assertEquals(bh.getIndex(), 6);
		assertEquals(bh.getNumValidChars(),7);
		assertEquals(bh.getMiddleChar(),'A');
		
		//System.out.println( Long.toBinaryString(bh.getBits()));
		
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
		assertEquals(bh.getContextSize(), 3);
	}

}
