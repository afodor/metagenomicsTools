package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import scripts.ratSach2.MappingFileLine;
import utils.ConfigReader;

public class RarifySequences
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			
			OtuWrapper wrapper = new OtuWrapper(
					ConfigReader.getRachSachReanalysisDir()+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + level + 
					"_AsColumns.txt");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" 
							+ File.separator + "sparseThreeColumn_" + level +
							"rarified.txt")));
			
			writer.write("sample\taverageRarified\n");
			
			HashMap<String, MappingFileLine> metaMap = MappingFileLine.getMap();
			
			for( int y=0; y < wrapper.getSampleNames().size(); y++)
			{
				String sampleName = wrapper.getSampleNames().get(y);
				
				MappingFileLine mfl = metaMap.get(sampleName);
				
				if( ! mfl.getTissue().equals("Fecal content"))
				{
					System.out.println(sampleName + " " + mfl.getTissue());
					writer.write(sampleName + "\t");
					writer.write(wrapper.getRarefactionCurve(y, 50)[11400] + "\n");
				}
				
			}
			
			writer.flush();  writer.close();
		}
	}
}
