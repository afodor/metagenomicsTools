/**
 * Log normalize NEC and HPC data
 */
package jobinAnalysis_kw;

import java.io.File;
import parsers.OtuWrapper;

public class logNormalizeJobin {
	public static void main(String[] args) throws Exception {
		String folder = "C:\\Users\\kwinglee.cb3614tscr32wlt\\Documents\\Fodor\\JobinCollaboration\\";
		String[] projects = {"NEC\\nec", "HPC\\hpc"};
		String[] analyses = {"_cr_q19", "_PL_wTaxaRDP80", "_PL_wTaxaUCLUST"};
		String[] levels = {"", "_L2", "_L3", "_L4", "_L5", "_L6"};
		
		for(int p = 0; p < projects.length; p++) {
			for(int a = 0; a < analyses.length; a++) {
				for(int l = 0; l < levels.length; l++) {
					String file = folder + projects[p] + "_mrg" + analyses[a] + levels[l] + "_taxaAsCol";
					System.out.println(file);
					OtuWrapper wrapper = new OtuWrapper(file + ".txt");
					wrapper.writeLoggedDataWithTaxaAsColumns(new File(file + "_logNorm.txt"));
				}
			}
		}
	}
}
