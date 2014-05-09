package adenomasFilter;

import java.util.ArrayList;
import java.util.List;

public class LookForSharedSNPs
{
	private static class SNPEvent
	{
		final int position;
		final char firstChar;
		final char secondChar;
		private List<Pairing> pairingList = new ArrayList<Pairing>();
		
		public SNPEvent(int position, char firstChar, char secondChar)
			throws Exception
		{
			if( firstChar ==secondChar)
				throw new Exception("Logic error");
			
			this.position = position;
			
			if( firstChar < secondChar)
			{
				this.firstChar = firstChar;
				this.secondChar = secondChar;
			}
			else
			{
				this.firstChar = secondChar;
				this.secondChar = firstChar;
			}
			
		}
		
		String getIdString()
		{
			return position + "_" + firstChar + "_" + secondChar;
		}
		
		@Override
		public int hashCode()
		{
			return getIdString().hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			SNPEvent se = (SNPEvent)obj;
			return this.getIdString().equals(se.getIdString());
		}
	}
	
	private static class Pairing
	{
		final String firstGenomeID;
		final String secondGenokeID;
		
		Pairing(String id1, String id2) throws Exception
		{
			if( id1.equals(id2))
				throw new Exception("Logic error");
			
			if( id1.compareTo(id2) < 0)
			{
				this.firstGenomeID = id1;
				this.secondGenokeID = id2;
			}
			else
			{
				this.firstGenomeID = id2;
				this.secondGenokeID= id1;
			}
		}
	}
}
