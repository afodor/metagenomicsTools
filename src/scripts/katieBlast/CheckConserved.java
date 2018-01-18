package scripts.katieBlast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import dynamicProgramming.BlossumMatrix;
import dynamicProgramming.NeedlemanWunsch;
import dynamicProgramming.PairedAlignment;
import parsers.FastaSequence;
import utils.ConfigReader;

public class CheckConserved
{
	/*
	 * F214
R223
S344
G345
F405
C503
E505
H536
F537
E637
G873
	 */
	
	/*
	public static final String[] EXPECTED_CONSERVED = 
		{
				"F214",
				"R223",
				"S344",
				"G345",
				"F405",
				"C503",
				"E505",
				"H536",
				"F537",
				"E637",
				"G873"
		};
		*/
	
	public static final String[] EXPECTED_CONSERVED = 
		{
		"F405",
		"C503",
		"H536",
		"E637",
		"G873"
			};
	
	private static boolean isConserved(String seq)
	{
		for( String s : EXPECTED_CONSERVED)
		{
			char c = s.charAt(0);
			
			int position = Integer.parseInt(s.substring(1)) - 1;
			
			if(  seq.charAt(position) != c) 
				return false;
		}
		
		return true;
	}
	
	private static boolean isConserved(HashMap<Integer, Character> map) throws Exception
	{
		for( String s : EXPECTED_CONSERVED)
		{
			char c = s.charAt(0);
			
			int position = Integer.parseInt(s.substring(1)) - 1;
			
			if(  map.get(position) != c) 
				return false;
		}
		
		return true;
	}
	
	public static HashMap<Integer, Character> getConsMap(PairedAlignment pa) throws Exception
	{
		HashMap<Integer, Character> map = new HashMap<>();
		int index=0;
		
		for( int x=0; x < pa.getFirstSequence().length(); x++)
		{
			char c = pa.getFirstSequence().charAt(x);
			
			if( c != '-' )
			{
				map.put(index, pa.getSecondSequence().charAt(x));
				index++;
			}
		}
		
		return map;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		BlossumMatrix bm = new BlossumMatrix("C:\\Users\\corei7\\git\\metagenomicsTools\\src\\scripts\\katieBlast\\blossom50.txt");
		
		BufferedWriter writer = new BufferedWriter( new FileWriter( new File( ConfigReader.getKatieBlastDir() + File.separator + 
				"matching_Conserved_2YAJ_from5.fas")));
		
		String topSeq = FastaSequence.readFastaFile(ConfigReader.getKatieBlastDir() + File.separator + 
				"2YAJ.txt").get(0).getSequence();
		
		if( ! isConserved(topSeq))
			throw new Exception("Expecting topseq to be conserved ");
		
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getKatieBlastDir() + File.separator + 
				"matching_2YAJ.txt");
		
		int numConserved =0;
		int index =0;
		HashSet<String> targets = new LinkedHashSet<>();
		for(FastaSequence fs : list )
		{
			String secondSeq = fs.getSequence().replaceAll("x", "");
			PairedAlignment pa = 
			NeedlemanWunsch.globalAlignTwoSequences(topSeq,secondSeq, bm, -8, -99, false);
			
			HashMap<Integer, Character> map = getConsMap(pa);
			
			if(isConserved(map))
			{
				String targetSeq = fs.getSequence().replaceAll("-", "");
				
				if( ! targets.contains(targetSeq))
				{
					targets.add(targetSeq);	
					numConserved++;
					/*
					writer.write(fs.getFirstTokenOfHeader() + " " + nf.format( 100*(1-pa.getDistance()))+  "\n");
					writer.write(pa.getFirstSequence() + "\n");
					writer.write(pa.getMiddleString() + "\n");
					writer.write(pa.getSecondSequence() + "\n");
					writer.write("\n\n");
					*/
					
					writer.write(fs.getHeader() + "\n");
					writer.write(fs.getSequence() + "\n");
					writer.flush();
				}
					
			}
			
			index++;
			
			System.out.println(index + " " + numConserved);
		}
		
		writer.flush();  writer.close();
		
		List<String> targetList = new ArrayList<>(targets);

		for( int x=0; x < targetList.size()-1; x++)
		{
			String xSeq = targetList.get(x);
			
			for( int y=x+1; y < targetList.size(); y++ )
			{
				String ySeq = targetList.get(y);
				
				PairedAlignment pa = 
						NeedlemanWunsch.globalAlignTwoSequences(xSeq,ySeq, bm, -8, -99, false);
				
				System.out.println(x + " " + y + " " + nf.format( 100*(1-pa.getDistance())));
			}
		}
		
	}
}
