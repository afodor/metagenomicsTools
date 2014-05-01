package bottomUpTree.figures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class TabularRnaSeqParserFileLine
{

	private String contig;
	private String operonName;
	private String operonLocation;
	private double o_pValue_il02_ilaom02;
	private double o_pValue_il12_ilaom12;
	private double o_pValue_il20_ilaom20;
	private String geneName;
	private String geneProduct;
	private int geneLocation;	
	private double g_pValue_il02_ilaom02;
	private double g_pValue_il12_ilaom12;
	private double g_pValue_il20_ilaom20;
	private double g_fc_il02_ilaom02;
	private double g_fc_il12_ilaom12;
	private double g_fc_il20_ilaom20;
	
	private List<TabularRnaSeqParserFileLine> children = null;
	
	public List<TabularRnaSeqParserFileLine> getChildren()
	{
		return children;
	}
	
	private boolean isOperon = false;
	
	public boolean getIsOperon() 
	{
		return isOperon;
	}
	
	private static TabularRnaSeqParserFileLine cloneAsOperon( TabularRnaSeqParserFileLine parent )
	{
		TabularRnaSeqParserFileLine newT = new TabularRnaSeqParserFileLine();
		
		newT.contig = parent.contig;
		newT.operonName = parent.operonName;
		newT.operonLocation = parent.operonLocation;
		newT.o_pValue_il20_ilaom20 = parent.o_pValue_il20_ilaom20;
		newT.o_pValue_il12_ilaom12 = parent.o_pValue_il12_ilaom12;
		newT.o_pValue_il02_ilaom02 = parent.o_pValue_il02_ilaom02;
		newT.isOperon = true;
		newT.children = new ArrayList<TabularRnaSeqParserFileLine>();
		
		return newT;
	}
	
	public static TabularRnaSeqParserFileLine getAsTree(String filepath)
		throws Exception
	{
		List<TabularRnaSeqParserFileLine> list =parseFile(filepath);
		
		TabularRnaSeqParserFileLine root = new TabularRnaSeqParserFileLine();
		
		HashMap<String, TabularRnaSeqParserFileLine> operonMap = 
				new HashMap<String, TabularRnaSeqParserFileLine>();
		
		for( TabularRnaSeqParserFileLine t : list)
		{
			TabularRnaSeqParserFileLine daughter = operonMap.get(t.getOperonName());
			
			if( daughter == null)
			{
				daughter = cloneAsOperon(t);
				root.children.add(t);
				operonMap.put(t.getOperonName(), daughter);
			}
			else
			{
				equalOperonDataOrThrow(t, daughter);
			}
		}
		
		for( TabularRnaSeqParserFileLine t : list)
		{
			TabularRnaSeqParserFileLine parentOperon = operonMap.get(t.operonName);
			parentOperon.children.add(t);
		}
		
		return root;
	}
	
	private static void equalOperonDataOrThrow(TabularRnaSeqParserFileLine t1, TabularRnaSeqParserFileLine t2) 
		throws Exception
	{
		if( t1.o_pValue_il02_ilaom02!= t2.o_pValue_il02_ilaom02)
			throw new Exception("No");
		
		if( t1.o_pValue_il12_ilaom12 != t2.o_pValue_il12_ilaom12)
			throw new Exception("No");
		
		if( t1.o_pValue_il20_ilaom20 != t2.o_pValue_il20_ilaom20)
			throw new Exception("No");
		
		if( ! t1.operonName.equals(t2.operonName))
			throw new Exception("Logic error");
		
		if( ! t1.contig.equals(t2.contig) )
			throw new Exception("NO");
		
	}
	
	// gets the root node
	private TabularRnaSeqParserFileLine()
	{
		this.contig = "root";
		this.operonName = "root";
		this.operonLocation = "root";
		this.o_pValue_il02_ilaom02 = 0;
		this.o_pValue_il12_ilaom12 = 0;
		this.o_pValue_il20_ilaom20 =0;
		this.g_pValue_il02_ilaom02 =0;
		this.g_pValue_il12_ilaom12 =0;
		this.g_pValue_il20_ilaom20 =0;
		this.g_fc_il02_ilaom02 =0;
		this.g_fc_il12_ilaom12 =0;
		this.g_fc_il20_ilaom20=0;
		this.geneLocation = 0;
		this.geneName = "root";
		this.geneProduct = "root";
		this.children = new ArrayList<TabularRnaSeqParserFileLine>();
	}
	
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
