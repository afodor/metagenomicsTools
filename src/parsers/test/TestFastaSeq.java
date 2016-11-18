package parsers.test;

import org.junit.Test;

import junit.framework.TestCase;
import parsers.FastaSequence;

public class TestFastaSeq extends TestCase
{
	@Test
	public void testBasicLengthAndIdentity() throws Exception
	{
		String aHeader = "Seq1";
		String aSeq = "CCCGGGAAAA";
		
		FastaSequence fs = new FastaSequence(aHeader, aSeq);
		
		assertEquals(fs.getHeader(), ">" +  aHeader);
		assertEquals(fs.getSequence(), aSeq);
	}
	
	public void testGCContent() throws Exception
	{
		String aHeader = "Seq1";
		String aSeq = "ACGTACGTACGT";
		
		FastaSequence fs = new FastaSequence(aHeader, aSeq);
		
		assertEquals(0.5f, fs.getGCRatio(),0.0000001);
		
	}

}
