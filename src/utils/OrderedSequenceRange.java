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


package utils;

public class OrderedSequenceRange
{
	private final int startPosition;
	private final int endPosition;
	private boolean originallyForward;
	
	/**  if startSequence > endSequence, they will be switched
	 */
	public OrderedSequenceRange( int startPositionIn, int endPositionIn )
	{
		if ( startPositionIn > endPositionIn ) 
		{
			int temp = startPositionIn;
			startPositionIn = endPositionIn;
			endPositionIn = temp;
			originallyForward = false;
		}
		else
		{
			originallyForward = true;
		}
		
		this.startPosition = startPositionIn;
		this.endPosition = endPositionIn;
	}
	
	public boolean isInRange(int pos)
	{
		if ( pos >= startPosition && pos <= endPosition)	
			return true;
			
		return false;
	}
	
	public int getEndPosition()
	{
		return endPosition;
	}

	public int getStartPosition()
	{
		return startPosition;
	}
	
	public String toString()
	{
		return "Start: " + this.startPosition + " End: " + this.endPosition;
	}

	public boolean isOriginallyForward()
	{
		return originallyForward;
	}
	
	public int getLength()
	{
		return this.endPosition - this.startPosition;
	}
	
	public int getOverlap(OrderedSequenceRange otherRange)
	{
		return Math.min(this.endPosition, otherRange.endPosition) -
			Math.max(this.startPosition, otherRange.startPosition);
	}
	
	public static void main(String[] args)
	{
		OrderedSequenceRange osr = new OrderedSequenceRange(1,100);
		
		System.out.println( osr.getOverlap(new OrderedSequenceRange(50,150))  );
	}
}
