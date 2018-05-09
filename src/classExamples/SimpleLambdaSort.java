package classExamples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleLambdaSort
{
	private static class Student
	{
		final String name;
		final int id;
		
		Student( String name, int id)
		{
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String toString()
		{
			return this.name + " " + this.id;
		}
	}
	
	public static void main(String[] args)
	{
		List<Student> list = new ArrayList<Student>();
		list.add( new Student( "Fred", 1));
		list.add(new Student("Bob", 2));
		list.add(new Student("Alice", 3));
		
		// the old way to sort by name
		Collections.sort(list, new Comparator<Student>()
		{
			@Override
			public int compare(Student o1, Student o2)
			{
				return o1.name.compareTo(o2.name);
			}
		});
		
		for(Student s : list)
			System.out.println(s);
		
		//as of Java 1.8, use Lambda functions to simplify the syntax
		//list.sort((o1, o2)->o1.name.compareTo(o2.name));
		//list.forEach(System.out::println);
		
		// makes it easier to see the logic of the sort 
		//list.sort((o1, o2)->o1.id-o2.id);
		//list.forEach(System.out::println);
	}
}
