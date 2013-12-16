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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import eTree.ENode;


public class ReadCluster
{
	public static List<ENode> readFromFile(String filePath, boolean removeSingletons, boolean validateDaughterAndParents) throws Exception
	{
		ObjectInputStream in =new ObjectInputStream(new GZIPInputStream(new 
				FileInputStream(filePath)));
		
		@SuppressWarnings("unchecked")
		List<ENode> list = (List<ENode>) in.readObject();
		
		in.close();
		
		if( removeSingletons)
			for(Iterator<ENode> i = list.iterator(); i.hasNext(); )
				if( i.next().getProbSequence().getNumRepresentedSequences() == 1)
					i.remove();
		
		for( ENode enode : list)
			enode.validateNodeAndDaughters(validateDaughterAndParents);
		
		return list;
	}
	
	public static HashMap<Float, List<ENode>> getMapByLevel(ENode root) throws Exception
	{	
		HashMap<Float, List<ENode>> mapByLevel = new HashMap<Float, List<ENode>>();
		addNodeAndChildren(root, mapByLevel);
		
		return mapByLevel;
		
	}
	
	private static void addNodeAndChildren( ENode node, HashMap<Float, List<ENode>> map)
	{
		List<ENode> innerList = map.get(node.getLevel());
			
		if( innerList == null)
		{
			innerList = new ArrayList<ENode>();
			map.put(node.getLevel(), innerList);
		}
			
		innerList.add(node);
	
		for( ENode d : node.getDaughters())
			addNodeAndChildren(d, map);
			
		
	}

	
}
