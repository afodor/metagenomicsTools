package pca;

public interface DistanceMeasureInterface
{
	public double[][] getDistanceMatrix(double[][] d) throws Exception;
	public String getName();
}
