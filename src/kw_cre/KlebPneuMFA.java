/*
 * subset Broad core genome alignments to only include Kleb pneu genomes
 */
package kw_cre;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import utils.ConfigReader;

public class KlebPneuMFA {
	public static void main(String[] args) throws Exception {
		String mfa = ConfigReader.getCHSDir() + File.separator + 
				"BroadTrees" + File.separator + "all76.mfa";
		String classify = ConfigReader.getCHSDir() + File.separator + 
				"rbh" + File.separator + "GenomeToClass.txt";
		String output = ConfigReader.getCHSDir() + File.separator + 
				"BroadTrees" + File.separator + "kpneu.mfa";
		
		//get list of CHS klebsiella pneuoniae genomes
		Set<Integer> kpneu = new HashSet<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(classify)));
		String line = br.readLine();//header
		for(line=br.readLine(); line!=null; line=br.readLine()) {
			String genome = line.split("\t")[0];
			if(genome.contains("kleb") && genome.contains("pneu") && 
					genome.contains("chs")) {
				String[] sp = genome.split("_");
				kpneu.add(Integer.parseInt(sp[sp.length-1].replace(".0", "")));
			}
		}
		br.close();
		System.out.println("Number genomes: " + kpneu.size());
		
		//filter mfa file
		br = new BufferedReader(new FileReader(new File(mfa)));
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(output)));
		boolean write = true;//if true, is kleb pneu, so write
		int num = 0;
		for(line=br.readLine(); line!=null; line=br.readLine()) {
			if(line.startsWith(">")) {
				int genome = Integer.parseInt(line.replace(">", "").split("_")[0]);
				write = kpneu.contains(genome);
				if(write) {
					num++;					
				}
			}
			if(write) {
				out.write(line + "\n");
			}
		}
		br.close();
		out.close();
		System.out.println("Number genomes written: " + num);
	}

}
