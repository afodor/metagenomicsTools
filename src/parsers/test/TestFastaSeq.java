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

}
