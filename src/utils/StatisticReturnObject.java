
package utils;


public class StatisticReturnObject
{
    private final double score;
    private final double pValue;
    
    // r must be set externally
    private double r;
    
    public StatisticReturnObject(double score, double pValue)
    {
        this.score = score;
        this.pValue = pValue;
    }
    
    public void setR(double r)
	{
		this.r = r;
	}
    
    public double getR()
	{
		return r;
	}
    
    public double getRSquared()
    
    {
    	return r * r;
    }
    
    public double getPValue()
    {
        return pValue;
    }
    public double getScore()
    {
        return score;
    }
    
    
}
