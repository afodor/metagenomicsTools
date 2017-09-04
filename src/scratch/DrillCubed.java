package scratch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// A
public class DrillCubed
{
	
	private static Random random = new Random();
	public static final int NUM_SECONDS = 180;
	public static final int NUM_QUESTIONS = 20;
	
	private static void printIncorrect(List<Holder> list)
	{
		if( list.size()==0)
			return;
		
		System.out.println("\n\n\nYou got the following wrong!!!");
		
		for(Holder h : list)
		{
			System.out.println(h.question + " is " + h.answer + " not " + h.attemptedAnswer);
		}
	}
	
	private static String ignoreNonNumeric(String s )
	{
		StringBuffer buff  = new StringBuffer();
		
		for( int x= 0; x < s.length();x++)
		{
			char c = s.charAt(x);
			
			if( Character.isDigit(c) || c == '-')
				buff.append(c);
		}
		
		return buff.toString();
	}
	
	private static class Holder
	{
		String question;
		int answer;
		double attemptedAnswer;
		
	}
	
	private static Holder getSquare() 
	{
		int number = random.nextInt(15) + 1;
		
		Holder h = new Holder();
		h.question = number + " squared ";
		h.answer = number * number;
		
		return h;
	}
	
	private static Holder getCube() 
	{
		int number = random.nextInt(10) + 1;
		
		Holder h = new Holder();
		h.question = number + " cubed ";
		h.answer = number * number * number;
		
		return h;
	}
	
	private static Holder getSquareRoot() 
	{
		int number = random.nextInt(15) + 1;
		
		Holder h = new Holder();
		h.question = "Square root of " + number * number;
		h.answer = number;
		
		return h;
	}
	
	private static Holder getMultiplication() 
	{
		int firstNum = random.nextInt(12) + 1;
		int secondNum = random.nextInt(12) + 1;
		
		if( random.nextFloat() < .75)
			firstNum = firstNum * -1;
		
		if( random.nextFloat() < .75)
			secondNum = secondNum * -1;
		
		Holder h = new Holder();
		h.question = firstNum + " times " + secondNum;
		h.answer = firstNum * secondNum;
		
		return h;
	}
	
	private static Holder getCubeRoot() 
	{
		int number = random.nextInt(10) + 1;
		
		Holder h = new Holder();
		h.question = "cube root of " + number * number* number;
		h.answer = number;
		
		return h;
	}
	
	private static Holder getQuestion()
	{
		int choose = random.nextInt(6);
		
		if( choose == 0 )
			return getSquare();
		
		if( choose == 1)
			return getSquareRoot();
		
		if( choose == 2 )
			return getCube();
		
		if( choose == 3)
			return getCubeRoot();
		
		return getMultiplication();
	}
	
	public static void main(String[] args)
	{
		List<Holder> incorrect = new ArrayList<Holder>();
		 
		long startTime = System.currentTimeMillis();
		
		int numRight= 0;
		int numWrong =0;
		for( int x=0; x < NUM_QUESTIONS; x++)
		{
			long time =( System.currentTimeMillis() - startTime ) / 1000;
			System.out.println("\ntime = " + time + " seconds\n" + numRight + " right and " + numWrong + " wrong out of " + NUM_QUESTIONS + "\n");
			Holder h = getQuestion();
			System.out.println(h.question);
			
			try
			{
				h.attemptedAnswer= Integer.parseInt( ignoreNonNumeric( System.console().readLine()));
			}
			catch(Exception e)
			{
				
			}
			
			if( h.attemptedAnswer == h.answer)
			{
				System.out.println("Right");
				numRight++;
			}
			else
			{
				System.out.println("Wrong.  The answer is " + h.answer);
				incorrect.add(h);
				numWrong++;
			}
			
			
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
