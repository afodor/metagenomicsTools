package scripts.TopeDiverticulosisDec2015;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class CreateRDPQSub
{
	private static final int NUMBER_CORES = 7;
	private static int countNum =0 ;

	public static final File FASTA_DIR = new File("C:\\topeDiverticulosis_Dec_2015\\fastaOut");
	public static final File RDP_OUT_DIR = new File( "C:\\topeDiverticulosis_Dec_2015\\rdpOut");
	public static final File RDP_RUN_DIR = new File("C:\\topeDiverticulosis_Dec_2015\\rdpRun");
	
	public static void main(String[] args) throws Exception
	{
		String[] files = FASTA_DIR.list();
		
		HashMap<Integer, BufferedWriter> map = new HashMap<Integer, BufferedWriter>();
		
		for( int x=0; x < NUMBER_CORES; x++)
		{
			map.put(x, new BufferedWriter(new FileWriter(new File(RDP_RUN_DIR +
				File.separator + "run_" + x + ".bat"))));
		}

		countNum= -1;
		
		for(String s : files)
			if( s.endsWith("fasta"))
		{
			countNum++;
			
			if( countNum == NUMBER_CORES)
				countNum =0;
			
			File fastaFile = new File(FASTA_DIR.getAbsolutePath() + File.separator + s);
			
			File rdpOutFile = new File(RDP_OUT_DIR.getAbsolutePath() + File.separator + 
					s  + "toRDP.txt");
			
			BufferedWriter writer = map.get(countNum);
			
			writer.write("java -jar C:\\rdp\\rdp_classifier_2.10.1\\dist\\classifier.jar " + 
					"-o \"" + rdpOutFile.getAbsolutePath()  + "\" -q \"" + fastaFile+ "\"\n" );
					
			writer.flush();  
		}
		
		for( BufferedWriter writer  : map.values())
		{
			writer.flush();  writer.close();
		}
	}
}
