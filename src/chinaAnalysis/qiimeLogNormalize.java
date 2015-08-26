/**
 * log normalize QIIME results
 */
package chinaAnalysis;

import java.io.File;

import parsers.OtuWrapper;

public class qiimeLogNormalize {
	public static void main(String[] args) throws Exception {
		String folder = "C:\\Users\\kwinglee.cb3614tscr32wlt\\Documents\\Fodor\\China\\qiime";
		String[] files = {"", "_L2", "_L3", "_L4", "_L5", "_L6"};
		
		for(int i = 0; i < files.length; i++) {
			String file = folder + File.separator + "otu_table" + files[i] + "_taxa_as_col.txt";
			System.out.println(file);
			OtuWrapper wrapper = new OtuWrapper(file);
			wrapper.writeLoggedDataWithTaxaAsColumns(new File(folder + File.separator + "otu_table" + files[i] + "_taxaAsCol-log-norm.txt"));
		}
	}
}
