package scratch;

public class Circle
{
	private final double radius;
	
	public double getRadius()
	{
		return radius;
	}
	
	public Circle(double radius)
	{
		this.radius = radius;
	}
	
	public static void main(String[] args) throws Exception
	{
		Circle c1 = new Circle(4);
		System.out.println(c1.getRadius());
		

		Circle c2 = new Circle(5);
		System.out.println(c2.getRadius());
	}
}
