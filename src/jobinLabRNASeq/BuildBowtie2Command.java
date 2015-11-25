package jobinLabRNASeq;

public class BuildBowtie2Command
{
	private static final String[] leftPairs = { 
			"1MM19_ATCACG_L008_R1_001.fastq","2MM19_CGATGT_L008_R1_001.fastq", "3MM19_TTAGGC_L008_R1_001.fastq",
			"4MM19_TGACCA_L008_R1_001.fastq", "5MM19_ACAGTG_L008_R1_001.fastq", "6MM19_GCCAAT_L008_R1_001.fastq",
			"7MM19_CAGATC_L008_R1_001.fastq", "8MM19_ACTTGA_L008_R1_001.fastq", "9MM19_GATCAG_L008_R1_001.fastq",
			"10MM19_TAGCTT_L008_R1_001.fastq", "11MM19_GGCTAC_L008_R1_001.fastq"
			};
	
	
	private static final String[] rightPairs = {  
			  
			"1MM19_ATCACG_L008_R2_001.fastq", "2MM19_CGATGT_L008_R2_001.fastq","3MM19_TTAGGC_L008_R2_001.fastq",
			"4MM19_TGACCA_L008_R2_001.fastq", "5MM19_ACAGTG_L008_R2_001.fastq", "6MM19_GCCAAT_L008_R2_001.fastq",
			 "7MM19_CAGATC_L008_R2_001.fastq", "8MM19_ACTTGA_L008_R2_001.fastq", "9MM19_GATCAG_L008_R2_001.fastq",
			 "10MM19_TAGCTT_L008_R2_001.fastq", "11MM19_GGCTAC_L008_R2_001.fastq"
			 };
	
	
	public static void main(String[] args) throws Exception
	{
		for( int x=0; x < 11; x++)
		{
			if( ! leftPairs[x].startsWith("" + (x+1)))
				throw new Exception("No");
			

			if( ! rightPairs[x].startsWith("" + (x+1)))
				throw new Exception("No");
			
			System.out.print("nice ~/bowtie2/bowtie2-2.0.0-beta5/bowtie2 -p 2  -x /home/afodor/bowtie2/pairedToNC101/allNC -1 ");
			
			System.out.print("/home/afodor/rnaseq/RNAseqFiles/" + leftPairs[x] );
		
			System.out.print(" -2 ");
			
			System.out.print("/home/afodor/rnaseq/RNAseqFiles/" + rightPairs[x] );
			System.out.print(" -S ");
			System.out.print( "/home/afodor/bowtie2/pairedToNC101/" + (x+1) + "_rnaToNC101ByBowtie.txt &" );
			System.out.println("\n");
		}	
		
		/*
		System.out.println("\n\n\n");
		for( int x=0; x < 11; x++)
		{
			System.out.println("/home/afodor/samtools-0.1.18/samtools view -bS " +
					"/home/afodor/bowtie2/pairedToNC101/" + (x+1) + "_rnaToNC101ByBowtie.txt > " +
					"/home/afodor/bowtie2/pairedToNC101/" + (x+1) + "_rnaToNC101ByBowtie.bam &"
					);
		}
		
		
		System.out.println("\n\n\n");
		for( int x=0; x < 11; x++)
		{
			System.out.println("/home/afodor/samtools-0.1.18/samtools sort " +
					"/home/afodor/bowtie2/pairedToNC101/" + (x+1) + "_rnaToNC101ByBowtie.bam " +
					"/home/afodor/bowtie2/pairedToNC101/" + (x+1) + "_rnaToNC101BySortedBowtie &"
					);
		}
		
		System.out.println("\n\n\n");
		for( int x=0; x < 11; x++)
		{
			System.out.println("/home/afodor/samtools-0.1.18/samtools index " +
					"/home/afodor/bowtie2/pairedToNC101/" + (x+1) + "_rnaToNC101BySortedBowtie.bam &"
					);
		}*/
	}
}
