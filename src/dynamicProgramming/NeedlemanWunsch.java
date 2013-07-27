package dynamicProgramming;


public class NeedlemanWunsch
{
	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int DIAG =3;
	private static final int START = 4;
	
	private static class AlignmentCell
	{
		int direction;
		float score;
		
		public AlignmentCell(float score)
		{
			this.score = score;
		}
		
		
		public AlignmentCell(int direction, float score)
		{
			this.direction = direction;
			this.score = score;
		}
		
	}
	
	/*
	 * 
	 * 
	 * GapPenalty should be <=0
	 * 
	 * Set affinePenalty to positive to use linear gap penalty.
	 * Otherwise affinePenalty should be negative
	 * 
	 * 
	 */
	public static PairedAlignment globalAlignTwoSequences(String s1, 
										String s2, 
											SubstitutionMatrix sm, 
											int gapPenalty, int affinePenalty,
											boolean noPenaltyForBeginningOrEndingGaps ) throws Exception
	{   
		if( gapPenalty > 0 )
			throw new Exception("Gap penalty should be zero or negative");
		
		AlignmentCell[][] cels = new AlignmentCell[s1.length()+1][s2.length()+1];
		
		//System.out.println("Starting fill array step");
		//long startTime = System.currentTimeMillis();
		fillArray(s1, s2, sm, gapPenalty,affinePenalty, noPenaltyForBeginningOrEndingGaps, cels);
		//long endTime = System.currentTimeMillis();
		//System.out.println("Finished array fill with " + (endTime - startTime) / 1000f);
		
		return readArray(s1, s2, cels);
	}
	
	/*
	 * [0] is the first sequence
	 * [1] is an alignment line with '|' if the first and sequence match
	 * [2] is the second sequence
	 */
	public static String[] getPrettyPrintout( PairedAlignment pa ) 
	{
		String[] s = new String[3];
		
		s[0] = pa.getFirstSequence();
		
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < pa.getFirstSequence().length(); x++)
		{
			if( pa.getFirstSequence().charAt(x) == pa.getSecondSequence().charAt(x))
			{
				buff.append("|");
			}
			else
			{
				buff.append(" ");
			}
		}
		s[1] = buff.toString();
		s[2]= pa.getSecondSequence();
				
		return s;
	}
	
	
	public static PairedAlignment readArray( String s1, String s2, AlignmentCell[][] cels )
		throws Exception
	{
		int x = s1.length();
		int y = s2.length();
		AlignmentCell lastCel = cels[x][y];
		StringBuffer topString = new StringBuffer();
		StringBuffer leftString = new StringBuffer();
		
		float score = lastCel.score;
		
		while( lastCel.direction != START )
		{
			if( lastCel.direction == UP )
			{
				topString.append("-");
				leftString.append(s2.charAt(y-1));
				y--;
			} 
			else if ( lastCel.direction == LEFT)
			{
				topString.append(s1.charAt(x-1));
				leftString.append("-");
				x--;
			} 
			else if ( lastCel.direction == DIAG)
			{
				topString.append(s1.charAt(x-1));
				leftString.append(s2.charAt(y-1));
				x--;  y--;
			} 
			else throw new Exception("Logic error");
			
			lastCel = cels[x][y];
		}
		
		return makeAPairedAlignment(topString.reverse().toString(), 
					leftString.reverse().toString(), score);
	}
	
	private static PairedAlignment makeAPairedAlignment(
			final String s1, final String s2, final float score)
	{
		return new PairedAlignment()
		{
			public float getAlignmentScore()
			{
				return score;
			}
			
			public String getFirstSequence()
			{
				return s1;
			}
			
			public String getSecondSequence()
			{
				return s2;
			}
		};
	}
	
	@SuppressWarnings("unused")
	private static void dumpArray( AlignmentCell[][] cels )
	{
		for (int x=0; x < cels.length; x++)
		{
			AlignmentCell[] yArray = cels[x];
			
			for( int y=0; y < yArray.length; y++ )
			{
				System.out.print( cels[x][y].score + "_" + cels[x][y].direction + "  " );
			}
			
			System.out.println();
		}
	}
	
	private static void fillArray( String s1, 
									String s2, 
									SubstitutionMatrix sm, 
									int gapPenalty, 
									int affinePenalty, 
									boolean noPenaltyForBeginningOrEndingGaps,
									AlignmentCell[][] cels )
		throws Exception
	{
		cels[0][0] = new AlignmentCell(START, 0); 
		
		if( !noPenaltyForBeginningOrEndingGaps )
		{
			for( int x=1; x <= s1.length(); x++)
				cels[x][0] = new AlignmentCell(LEFT, 0);
			
			for( int x=1; x <= s2.length(); x++)
				cels[0][x] = new AlignmentCell(UP, 0);
		}
		else if( affinePenalty > 0) // linear gap
		{
			for( int x=1; x <= s1.length(); x++)
				cels[x][0] = new AlignmentCell(LEFT, x * gapPenalty );
			
			for( int x=1; x <= s2.length(); x++)
				cels[0][x] = new AlignmentCell(UP, x * gapPenalty);
		}
		else
		{
			cels[1][0] = new AlignmentCell(LEFT, gapPenalty);
			
			for( int x=2; x <= s1.length(); x++)
				cels[x][0] = new AlignmentCell(LEFT, gapPenalty  + x * affinePenalty);
			
			cels[0][1] = new AlignmentCell(UP, gapPenalty);
			for( int x=2; x <= s2.length(); x++)
				cels[0][x] = new AlignmentCell(UP, gapPenalty + x * affinePenalty);
		
		}
		
		
		for( int y=1; y <= s2.length(); y++)
			for( int x=1; x <= s1.length(); x++)
			{
				float top = Float.NEGATIVE_INFINITY;
				float left = Float.NEGATIVE_INFINITY;
				
				if( affinePenalty >0 )  // linear gap
				{
					top = cels[x][y-1].score + gapPenalty;
					left = cels[x-1][y].score + gapPenalty;
				}
				else
				{
					if( cels[x][y-1].direction == UP )
						top = cels[x][y-1].score + affinePenalty;
					else
						top = cels[x][y-1].score + gapPenalty;
						
					if( cels[x-1][y].direction == LEFT )
						left = cels[x-1][y].score + affinePenalty;
					else
						left =  cels[x-1][y].score + gapPenalty;
				}
				
				float diag = cels[x-1][y-1].score + 
						sm.getScore(s1.charAt(x-1), s2.charAt(y-1));
				
				float max = Math.max(top, Math.max(left, diag));
				
				AlignmentCell ac = new AlignmentCell(max);
				
				if( max == top)
					ac.direction = UP;
				else if ( max == left)
					ac.direction = LEFT;
				else if ( max == diag)
					ac.direction = DIAG;
				else throw new Exception("Logic error");
				
				cels[x][y] = ac;
			}
	}
	
	public static void main(String[] args) throws Exception
	{
		String seq1 = 
			"ACCCCCAAAAG";
			
		String seq2 = 
			"ACCCGTCAAAAG";
		
		SubstitutionMatrix sm = new DNASubstitutionMatrix();
		
		PairedAlignment pa = globalAlignTwoSequences( seq1,seq2 , sm, -1,99, true);
		
		System.out.println(pa.getFirstSequence());
		System.out.println(pa.getSecondSequence());
		System.out.println(pa.getAlignmentScore());
	}
}
