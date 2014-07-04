package topeCheck;

import java.io.File;
import java.util.List;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class RdpCheck
{
	public static void main(String[] args) throws Exception
	{
		List<NewRDPParserFileLine> list = 
				NewRDPParserFileLine.getRdpList(ConfigReader.getTopeCheckDir() +
						File.separator + "allToRdp.txt");
		
		int numOver50 =0;
		
		for( NewRDPParserFileLine rdp : list)
		{
			NewRDPNode node = rdp.getTaxaMap().get(NewRDPParserFileLine.PHYLUM);
			
			if( node != null && node.getScore() >80)
				numOver50++;
		}
		
		System.out.println(numOver50 + " " + list.size());
	}
}
