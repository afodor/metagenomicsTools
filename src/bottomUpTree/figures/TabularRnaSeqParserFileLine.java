package bottomUpTree.figures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TabularRnaSeqParserFileLine
{

	private final String contig;
	private final String operonName;
	private final String operonLocation;
	private final double o_pValue_il02_ilaom02;
	private final double o_pValue_il12_ilaom12;
	private final double o_pValue_il20_ilaom20;
	private final String geneName;
	private final String geneProduct;
	private final int geneLocation;	
	private final double g_pValue_il02_ilaom02;
	private final double g_pValue_il12_ilaom12;
	private final double g_pValue_il20_ilaom20;
	private final double g_fc_il02_ilaom02;
	private final double g_fc_il12_ilaom12;
	private final double g_fc_il20_ilaom20;
	
	
	
	public String getContig()
	{
		return contig;
	}



	public String getOperonName()
	{
		return operonName;
	}



	public String getOperonLocation()
	{
		return operonLocation;
	}



	public double getO_pValue_il02_ilaom02()
	{
		return o_pValue_il02_ilaom02;
	}



	public double getO_pValue_il12_ilaom12()
	{
		return o_pValue_il12_ilaom12;
	}



	public double getO_pValue_il20_ilaom20()
	{
		return o_pValue_il20_ilaom20;
	}



	public String getGeneName()
	{
		return geneName;
	}



	public String getGeneProduct()
	{
		return geneProduct;
	}



	public int getGeneLocation()
	{
		return geneLocation;
	}



	public double getG_pValue_il02_ilaom02()
	{
		return g_pValue_il02_ilaom02;
	}



	public double getG_pValue_il12_ilaom12()
	{
		return g_pValue_il12_ilaom12;
	}



	public double getG_pValue_il20_ilaom20()
	{
		return g_pValue_il20_ilaom20;
	}



	public double getG_fc_il02_ilaom02()
	{
		return g_fc_il02_ilaom02;
	}



	public double getG_fc_il12_ilaom12()
	{
		return g_fc_il12_ilaom12;
	}



	public double getG_fc_il20_ilaom20()
	{
		return g_fc_il20_ilaom20;
	}

	public static List<TabularRnaSeqParserFileLine> parseFile(String filepath) throws Exception
	{
		List<TabularRnaSeqParserFileLine> list = new ArrayList<TabularRnaSeqParserFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			list.add(new TabularRnaSeqParserFileLine(s));
		}
		
		reader.close();
		
		return list;
	}

	private TabularRnaSeqParserFileLine(String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, "\t");
		this.contig = sToken.nextToken();
		this.operonName = sToken.nextToken();
		this.operonLocation = sToken.nextToken();
		this.o_pValue_il02_ilaom02 = Double.parseDouble(sToken.nextToken());
		this.o_pValue_il12_ilaom12 = Double.parseDouble(sToken.nextToken());
		this.o_pValue_il20_ilaom20 = Double.parseDouble(sToken.nextToken());
		this.geneName = sToken.nextToken();
		this.geneProduct = sToken.nextToken();
		this.geneLocation = Integer.parseInt(sToken.nextToken());
		this.g_pValue_il02_ilaom02 = Double.parseDouble(sToken.nextToken());
		this.g_pValue_il12_ilaom12 = Double.parseDouble(sToken.nextToken());
		this.g_pValue_il20_ilaom20 = Double.parseDouble(sToken.nextToken());
		this.g_fc_il02_ilaom02 = Double.parseDouble(sToken.nextToken());
		this.g_fc_il12_ilaom12 = Double.parseDouble(sToken.nextToken());
		this.g_fc_il20_ilaom20 = Double.parseDouble(sToken.nextToken());
		
		if( sToken.hasMoreTokens())
			throw new Exception("No " + sToken.hasMoreTokens());
	}

}
