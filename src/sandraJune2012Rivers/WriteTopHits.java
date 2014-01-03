package sandraJune2012Rivers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.HitScores;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteTopHits
{
	private static HashMap<String, Double> getFractionMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getSandraRiverJune2012Dir()
				+File.separator +"abundantOTUSandraTrimmed.clustsize")));
		
		HashMap<String, Double> map =new LinkedHashMap<String, Double>();
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			String key = sToken.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			sToken.nextToken();
			
			map.put(key, Double.parseDouble(sToken.nextToken()));
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> fastaMap = 
				FastaSequence.getFirstTokenSequenceMap(ConfigReader.getSandraRiverJune2012Dir()  
						+File.separator + "silvaHitsToSandraStreamJune2012.fasta" );
		
		HashMap<String, Double> fractionMap = getFractionMap();
		
		double sum = 0;
		
		for(String s : fractionMap.keySet())
			sum += fractionMap.get(s);
		
		HashMap<String, HitScores> hitMap = HitScores.getTopHitsAsQueryMap(ConfigReader.getSandraRiverJune2012Dir()+
				File.separator + "otuToSivla108ByBlastn.txt");
		
		HashMap<String, NewRDPParserFileLine> rdpMap = NewRDPParserFileLine.getAsMapFromSingleThread(
				ConfigReader.getSandraRiverJune2012Dir() + File.separator + "sandraOTUtoRDP_byRDP2_4.txt");
		
		HashMap<String, NewRDPParserFileLine> refRDPMap = NewRDPParserFileLine.getAsMapFromSingleThread( 
				ConfigReader.getSandraRiverJune2012Dir() + File.separator + "silvaHitsByRdp2_4.txt");
 		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getSandraRiverJune2012Dir()  
				+File.separator + "rankAbundanceSandraJune2012.txt")));
		
		double cumulative=0;
		writer.write("queryID\tsilvaTargetID\tnumSequencesInOTU\trelativeAbundance\tcumulativeAbundance\t" + 
		"queryAlignmentLength\tpercentIdentity\teScore\tsilvaHeader\trdpLineDirect\trdpLineFromRef\n");
		
		for(String s : fractionMap.keySet())
		{
			writer.write(s + "\t");
			HitScores hs = hitMap.get(s);
			writer.write( fastaMap.get(hs.getTargetId()).getHeader() + "\t");
			writer.write(fractionMap.get(s) + "\t");
			writer.write(fractionMap.get(s) / sum + "\t");
			cumulative += fractionMap.get(s);
			writer.write( (1-cumulative / sum) + "\t");
			writer.write(hs.getQueryAlignmentLength() + "\t");
			writer.write(hs.getPercentIdentity() + "\t");
			writer.write(hs.getEScore() + "\t");
			writer.write(fastaMap.get( hs.getTargetId() ).getHeader() + "\t");
			writer.write(rdpMap.get(s).getSummaryString() + "\t");
			writer.write( refRDPMap.get(hs.getTargetId()).getSummaryString() + "\n");
		}
		
		writer.flush(); writer.close();
	}
}
