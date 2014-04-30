package bottomUpTree.figures;

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



	private TabularRnaSeqParserFileLine(String s)
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
	}

}
