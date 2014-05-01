package bottomUpTree.figures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import utils.ConfigReader;

public class RnaSeqTabularToJSON
{
	public static void main(String[] args) throws Exception
	{
		TabularRnaSeqParserFileLine root =
				TabularRnaSeqParserFileLine.getAsTree(ConfigReader.getJanelleRNASeqDir() + 
						File.separator + "NC_101_4JSON.txt");
		
	//	for( TabularRnaSeqParserFileLine t : root.getChildren() )
		//	System.out.println(t.getGeneName());
		
		//System.out.println("Got root with " + root.getChildren().size());
		
		writeJSon(root, new File(ConfigReader.getETreeTestDir() + 
						File.separator + "rnaSeqDemo.json"));
	}
	
	private static void writeJSon(TabularRnaSeqParserFileLine root,
							File outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writeNodeAndChildren(writer, root);
		
		writer.flush();  writer.close();
	}
	
	private static void writeNodeAndChildren( BufferedWriter writer,  
						TabularRnaSeqParserFileLine node) throws Exception
	{
		//System.out.println(node.getGeneName());
		writer.write("{\n");
		writer.write( "\"position\" : \"" + 	
				(node.getIsOperon() ? node.getOperonLocation() : node.getGeneLocation()) + "\",\n" );

		writer.write( "\"level\" : \"");
		
		if( node.getGeneName().equals("contig"))
			writer.write("contig\",\n");
		else if( node.getGeneName().equals("root"))
			writer.write("root\",\n");
		else if( node.getIsOperon())
			writer.write("operon\",\n");
		else
			writer.write("gene\",\n");
		
		writer.write( "\"contig\" : \"" + node.getContig()+ "\",\n" );
		writer.write( "\"gene product\" : \"" + node.getGeneProduct()+ "\",\n" );
		writer.write( "\"fc2weeks\" : \"" + node.getG_fc_il02_ilaom02()+ "\",\n" );
		writer.write( "\"fc12weeks\" : \"" + node.getG_fc_il12_ilaom12()+ "\",\n" );
		writer.write( "\"fc20weeks\" : \"" + node.getG_fc_il20_ilaom20()+ "\",\n" );
		
		writer.write( "\"pValue2Weeks\" : \"" + 	
				getNegativeLog(node.getIsOperon() ? node.getO_pValue_il02_ilaom02(): node.getG_pValue_il02_ilaom02()) 
								+ "\",\n" );
		
		writer.write( "\"pValue12Weeks\" : \"" + 	
				getNegativeLog(node.getIsOperon() ? node.getO_pValue_il12_ilaom12(): node.getG_pValue_il12_ilaom12()) 
								+ "\",\n" );

		writer.write( "\"pValue20Weeks\" : \"" + 	
				getNegativeLog(node.getIsOperon() ? node.getO_pValue_il20_ilaom20(): node.getG_pValue_il20_ilaom20()) 
								+ "\"" );
		if( node.getChildren() != null)
		{
			writer.write(",\n");
			writer.write( "\"children\" : [\n");
			
			for( Iterator<TabularRnaSeqParserFileLine> i = node.getChildren().iterator(); i.hasNext(); )
			{
				writeNodeAndChildren(writer, i.next());
				writer.write( i.hasNext() ? ",\n" : "]\n");
			}	
		}
		else
			writer.write("\n");
		
		writer.write("}\n");
	}
	
	private static double getNegativeLog( double val )
	{
		double aLog = -Math.log10(val);
		
		if( Double.isInfinite(aLog) || Double.isNaN(aLog))
			return 0;
		
		return aLog;
	}
}
