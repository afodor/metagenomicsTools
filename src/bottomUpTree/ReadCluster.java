/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package bottomUpTree;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import probabilisticNW.ProbSequence;


public class ReadCluster
{
	public static void main(String[] args) throws Exception
	{
		ObjectInputStream in =new ObjectInputStream(new GZIPInputStream(new 
				FileInputStream("D:\\E_Tree_TestDir\\bottomClusters\\31R")));
		
		@SuppressWarnings("unchecked")
		List<ProbSequence> list = (List<ProbSequence>) in.readObject();
		
		in.close();
		
		for(ProbSequence ps : list)
			System.out.println(ps);
	}
	
}
