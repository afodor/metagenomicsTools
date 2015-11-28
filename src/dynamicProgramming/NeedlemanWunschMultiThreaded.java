package dynamicProgramming;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import parsers.FastaSequence;

public class NeedlemanWunschMultiThreaded
{
	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int DIAG =3;
	private static final int START = 4;
	
	private static final int NUM_THREADS = 2;
	
	private static class AlignmentCell
	{
		int direction;
		final float score;
		
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
											float gapPenalty, int affinePenalty,
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
	
	private static void fillArray( final String s1, 
									final String s2, 
									final SubstitutionMatrix sm, 
									final float gapPenalty, 
									final int affinePenalty, 
									final boolean noPenaltyForBeginningOrEndingGaps,
									final AlignmentCell[][] cels )
		throws Exception
	{
		cels[0][0] = new AlignmentCell(START, 0); 
		Semaphore semaphore = new Semaphore(NUM_THREADS);
		
		if( noPenaltyForBeginningOrEndingGaps )
		{
			for( int x=1; x <= s1.length(); x++)
				cels[x][0] = new AlignmentCell(LEFT, 0);
			
			for( int x=1; x <= s2.length(); x++)
				cels[0][x] = new AlignmentCell(UP, 0);
		}
		else if( affinePenalty > 0) // linear gap
		{
			final CountDownLatch cdl1 = new CountDownLatch(2);
			
			new Thread( new Runnable()
			{
				
				@Override
				public void run()
				{
					for( int x=1; x <= s1.length(); x++)
						cels[x][0] = new AlignmentCell(LEFT, x * gapPenalty );
					
					cdl1.countDown();
				}
			}).start();;
			
		
			new Thread(
					
				new Runnable()
				{
					
					@Override
					public void run()
					{
						for( int x=1; x <= s2.length(); x++)
							cels[0][x] = new AlignmentCell(UP, x * gapPenalty);
						
						cdl1.countDown();
					}
				}).start();;
				
				cdl1.await();
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
		{
			semaphore.acquire();
			
			new Thread(new RunAStrip(affinePenalty, gapPenalty, cels, s1, s2, y, sm, semaphore)).start();
		}
		
		int numAcquired =0;
		
		while(numAcquired < NUM_THREADS)
		{
			semaphore.acquire();
			numAcquired++;
		}
			
	}
	
	private static AlignmentCell getACel(int x, int y, int affinePenalty,AlignmentCell[][] cels,
				float gapPenalty, SubstitutionMatrix sm, String s1, String s2)
					throws Exception
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
		
		return ac;
		
	}
	
	private static class RunAStrip implements Runnable
	{
		private final int affinePenalty;
		private final float gapPenalty;
		private final AlignmentCell[][] cels;
		private final String s1;
		private final String s2;
		private final int y;
		private final SubstitutionMatrix sm;
		private final Semaphore semaphore;
		
		public RunAStrip(int affinePenalty, float gapPenalty2, AlignmentCell[][] cels,
				String s1, String s2, int y, SubstitutionMatrix sm, Semaphore semaphore)
		{
			this.affinePenalty = affinePenalty;
			this.gapPenalty = gapPenalty2;
			this.cels = cels;
			this.s1 = s1;
			this.s2 = s2;
			this.y = y;
			this.sm = sm;
			this.semaphore = semaphore;
		}
		
		@Override
		public void run()
		{
			try
			{
				for( int x=1; x <= s1.length(); x++)
				{
					while(true)
					{
						synchronized (cels)
						{
							if( cels[x][y-1] != null && cels[x-1][y-1] != null && cels[x-1][y-1] != null )
								break;
						}
						
						Thread.yield();
					}
					
					synchronized( cels)
					{
						AlignmentCell ac = 
								getACel(
								x, y, affinePenalty,cels,
								gapPenalty, sm, s1, s2);
						cels[x][y] = ac;
					}
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				System.exit(1);
			}
			finally
			{
				semaphore.release();
			}
			
		}
	}

	//@SuppressWarnings("unused")
	/**
	 *
	 *This class is experimental and is not thread safe;
	 *should not be used for real data...
	 */
	public static void main(String[] args) throws Exception
	{
		SubstitutionMatrix bm = new BlossumMatrix(
			"C:\\Users\\corei7\\git\\afodor.github.io\\classes\\prog2015\\Blosum50.txt"	);
		
		List<FastaSequence> fastaList = 
				FastaSequence.readFastaFile(
						"C:\\Users\\corei7\\git\\afodor.github.io\\classes\\prog2015\\twoSeqs.txt");
		
		List<Double> times = new ArrayList<Double>();
		
		for( int x=0; x < 1; x++)
		{

			long startTime = System.currentTimeMillis();
			PairedAlignment pa = globalAlignTwoSequences(fastaList.get(0).getSequence(), 
								fastaList.get(1).getSequence(), bm, -8,99,  false);
			
			double time= (System.currentTimeMillis() - startTime) / 1000.0 ;
			times.add(time);
			System.out.println(time);
			
			System.out.println(pa.getFirstSequence());
			System.out.println(pa.getMiddleString());
			System.out.println(pa.getSecondSequence());
			System.out.println(pa.getAlignmentScore());

		}
	} 
	
	/*  DNA alignment
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
	*/
}
