package scratch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Drill
{
	
	
	public static final int NUM_SECONDS = 60;
	public static final int NUM_QUESTIONS = 100;
	
	private static void printIncorrect(List<String> list)
	{
		if( list.size()==0)
			return;
		
		System.out.println("\n\n\nYou got the following wrong!!!");
		
		for(String s : list)
		{
			System.out.println(s);
		}
	}
	
	public static void main(String[] args)
	{
		List<String> incorrect = new ArrayList<String>();
		 
		Random random = new Random();
		
		long startTime = System.currentTimeMillis();
		
		int numRight= 0;
		int numWrong =0;
		for( int x=0; x < NUM_QUESTIONS; x++)
		{
			int num1=12;
			///int num1 = random.nextInt(8) + 5;
			int num2 = random.nextInt(12) + 1;
			
			int inputAnswer = -1;
			int actualAnswer = -1;
			String questionString= null;
			
			if( random.nextBoolean()) // multiplication
			{
				actualAnswer = num1 * num2;
				questionString= num1 + " * " + num2;
				
			}
			else // division
			{
				int product = num1 * num2;
				actualAnswer = product / num1;
				
				questionString = product + " / " + num1;
			}
			
			System.out.println( questionString);
			
			try
			{
				inputAnswer = Integer.parseInt(System.console().readLine());
			}
			catch(Exception e)
			{
				
			}
			
			long time =( System.currentTimeMillis() - startTime ) / 1000;
			
			if( inputAnswer == actualAnswer)
			{
				System.out.println("RIGHT!!!!!");
				numRight++;
			}
			else
			{
				System.out.println("WRONG:  Answer was " + actualAnswer);
				numWrong++;
				incorrect.add(questionString + " = " + actualAnswer + " not " + inputAnswer);
			}
			
			System.out.println("Time = " + time + " number right = " + numRight + " out of " + (numRight + numWrong));
			
			System.out.println("\n\n\n");
			
			if( time >= NUM_SECONDS)
			{
				System.out.println("TIME'S UP!");
				printIncorrect(incorrect);
				System.exit(1);
			}
		}
		
		printIncorrect(incorrect);
	}
}
