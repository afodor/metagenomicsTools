/** 
 * Author:  anthony.fodor@gmail.com
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */

package reduceOTU;

public class IndividualEdit implements Comparable<IndividualEdit>
{
	public enum EDIT_TYPE { INSERTION, DELETION, SUBSTITUITION };
	
	private final EDIT_TYPE editType;
	private final int postion;
	private final char base;
	
	public EDIT_TYPE getEditType()
	{
		return editType;
	}

	public int getPostion()
	{
		return postion;
	}
	public char getBase()
	{
		return base;
	}

	public IndividualEdit(EDIT_TYPE editType, int postion, char base)
	{
		super();
		this.editType = editType;
		this.postion = postion;
		this.base = base;
	}
	
	@Override
	public int compareTo(IndividualEdit o)
	{
		return this.postion - o.postion;
	}
	
	@Override
	public String toString()
	{
		String s= "I";
		
		if( this.editType.equals(EDIT_TYPE.DELETION))
			s = "D";
		else if ( this.editType.equals(EDIT_TYPE.SUBSTITUITION))
			s = "S";
		
		return s + postion + base;
	}
}
