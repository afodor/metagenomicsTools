package test.testCoPhylog;

import utils.Translate;
import junit.framework.TestCase;
import coPhylog.BitHolder;

public class TestBitCounter extends TestCase
{
	
	public void testReverseTranscribe() throws Exception
	{
		String s = "ACCTTTACGGGGAAAGGTTAACCCAA";
		String reverseS = Translate.reverseTranscribe(s);
		System.out.println(reverseS);
		
		BitHolder bit1 = new BitHolder(5);
		BitHolder bit2 = new BitHolder(5);
		
		bit1.setToString(s, true);
		bit2.setToString(reverseS, false);
		
		System.out.println(bit1.getLeftSequence());
		
		assertEquals(bit1.getBits(), bit2.getBits());
		
		for(int x=0; x < 15; x++)
		{
			assertEquals(bit1.getBits(), bit2.getBits());
			assertEquals( bit1.getMiddleChar(), bit2.getMiddleChar() );
			assertEquals(bit2.getIndex(), x + 10);
			assertTrue( bit1.advance());
			assertTrue( bit2.advance());
		}
		
		assertFalse(bit1.advance());
		assertFalse(bit2.advance());
	}

	public void testInvalidChars() throws Exception
	{
		BitHolder bh = new BitHolder(2);
		assertEquals(bh.getContextSize(), 2);
		
		String s= "TTXCCGCCT";
		
		assertTrue( bh.setToString(s,false));
		
		assertEquals(bh.getMiddleChar(), 'G');
		assertEquals(bh.getNumValidChars(),5);
		assertEquals(bh.getIndex(),7);
		
		long cBase = 0x1l;
		long expectedAnswer = cBase<< 2;
		expectedAnswer = expectedAnswer | cBase;
		expectedAnswer = expectedAnswer << 32 + (32-4);
		
		long rightAnswer = cBase;
		rightAnswer = cBase<< 2;
		rightAnswer= rightAnswer | cBase;
		rightAnswer = rightAnswer << (32-4);
		
		expectedAnswer = expectedAnswer | rightAnswer;
		assertEquals(expectedAnswer, bh.getBits());
		
		
		assertTrue(bh.advance());
		assertEquals(bh.getIndex(), 8);
		assertEquals(bh.getMiddleChar(),'C');
		
		//CGCT
		expectedAnswer = ( 0x01l << 2 ) | (0x02l );
		expectedAnswer = expectedAnswer << 32 + (32-4);
		rightAnswer = ( 0x01l << 2 ) | (0x03l );
		rightAnswer = rightAnswer << (32-4);
		expectedAnswer = expectedAnswer | rightAnswer;
		assertEquals(expectedAnswer, bh.getBits());
		
		assertFalse(bh.advance());
	}
	
	public void testAdvance() throws Exception
	{
		BitHolder bh = new BitHolder(2);
		assertEquals(bh.getContextSize(), 2);
		
		String s= "TTACCG";
		
		assertEquals( bh.setToString(s,false), true);
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
		
		
		expectedAnswer = tBase;
		expectedAnswer = expectedAnswer | tBase;
		expectedAnswer = expectedAnswer << 32 + (32-2);
		
		rightAnswer = cBase;
		rightAnswer = rightAnswer << 2;
		rightAnswer = rightAnswer | 0x2l;
		rightAnswer = rightAnswer << (32-4);
		
		expectedAnswer = expectedAnswer | rightAnswer;
		
		assertEquals(expectedAnswer, bh.getBits());
		
		assertEquals( bh.advance(), false );
	}
	
	public void testInitial() throws Exception
	{
		BitHolder bh = new BitHolder(3);
		
		String s= "CCCATTTCCCCCCCCCCCCC";
		
		
		assertEquals( bh.setToString(s,false),true);
		
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
