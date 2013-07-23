/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package scripts.clusterManipulations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class GenerateSHFiles
{
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x <=20; x++)
		{
			BufferedWriter writer =new BufferedWriter(
					new FileWriter(new File("/projects/afodor/shotgunSequences/runSRR061115"+ x)));
			
			writer.write("date\n");
			writer.write("/users/afodor/ncbi-blast-2.2.28+/bin/blastn " + 
			"-query /projects/afodor/shotgunSequences/SRR061115.fasta_FILE_" + x +" " + 
					"-db /projects/afodor/dbs/ncbi.fasta -outfmt 7 " + 
			"-out /projects/afodor/shotgunSequences/SRR061115.fasta_FILE_" + x +"_TO_NCBI.txt " + 
					"-num_threads 4\n");
			
			writer.write("date\n");
			writer.flush(); writer.close();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/projects/afodor/shotgunSequences/runMany.sh")));
		
		for( int x=1; x <=20; x++)
		{
			writer.write("qsub -q \"viper\" -N \"Job" + x + "\" runSRR061115"+ x + "\n");
		}
		
		writer.flush();  writer.close();
	}
}
