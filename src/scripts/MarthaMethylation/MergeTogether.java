package scripts.MarthaMethylation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;

public class MergeTogether
{
	public static String PATHWAY_PLUS_META = "C:\\MarthaMethylation\\shanCountTables\\humann2LogNormPlusMeta.txt";
	
	private static String alphaNumericOnly(String s)
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < s.length(); x++)
		{
			char c=  s.charAt(x);
			if( Character.isAlphabetic(c) || Character.isDigit(c))
				buff.append(c);
			else
				buff.append("_");
		}
		
		return buff.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MgDefScoreParserLine>  map = MgDefScoreParserLine.getMgScoreMap();
		HashMap<String, ShanMetadataParserLine> metaMap = ShanMetadataParserLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				MetaMergePathways.PATHWAY_FILE_PIVOTED_LOG_NORM
				)));
		
		OtuWrapper wrapper = new OtuWrapper(MetaMergePathways.PATHWAY_FILE_PIVOTED_LOG_NORM);
		
		List<Boolean> includeList = wrapper.getKeepTaxaAbovePrevelanceThreshold(0.75);
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(PATHWAY_PLUS_META)));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write(topSplits[0] + "\tsubjectID\tBS_pred_methy\tBS_pred_methy_post\tmc_pred_methy\tmc_pred_methy_post");
		
		for( int x=1; x < topSplits.length; x++)
			if( includeList.get(x-1))
				writer.write("\t" + alphaNumericOnly(topSplits[x]));
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0]);
			
			ShanMetadataParserLine smpl = metaMap.get(splits[0]);
			
			MgDefScoreParserLine mdspl = map.get(smpl.getStudyID());
			
			writer.write("\t" + smpl.getStudyID());
			
			if( mdspl == null)
			{
				writer.write("\tNA\tNA\tNA\tNA");
			}
			else
			{
				writer.write("\t" + getNAOrVal(mdspl.getBS_pred_methy()) +"\t" 
							+ getNAOrVal( mdspl.getBS_pred_methy_post()) + "\t" + 
							getNAOrVal(	mdspl.getMc_pred_methy())  + "\t" +
							getNAOrVal(	mdspl.getMc_pred_methy_post()));
			}
			
			for( int x=1; x < splits.length; x++)
				if( includeList.get(x-1))
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static String getNAOrVal(Double d)
	{
		if( d== null)
			return "NA";
		
		return d.toString();
	}
}
