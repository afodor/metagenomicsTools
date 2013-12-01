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
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import probabilisticNW.ProbSequence;


public class ReadCluster
{
	public static List<ProbSequence> readFromFile(String filePath, boolean removeSingletons) throws Exception
	{
		ObjectInputStream in =new ObjectInputStream(new GZIPInputStream(new 
				FileInputStream(filePath)));
		
		@SuppressWarnings("unchecked")
		List<ProbSequence> list = (List<ProbSequence>) in.readObject();
		
		in.close();
		
		if( removeSingletons)
			for(Iterator<ProbSequence> i = list.iterator(); i.hasNext(); )
				if( i.next().getNumRepresentedSequences() == 1)
					i.remove();
		
		for( ProbSequence probSeq : list )
			probSeq.validateProbSequence();
		
		return list;
	}
	
}
