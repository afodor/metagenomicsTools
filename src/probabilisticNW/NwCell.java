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


package probabilisticNW;

public class NwCell
{
	private float score;
	
	public enum Direction { DIAGNOL, UP, LEFT, INIT };
	
	private final Direction direction;
	
	public NwCell( float score, Direction direction )
	{
		this.score = score;
		this.direction = direction;
	}
	
	public float getScore()
	{
		return score;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public String toString()
	{
		return direction + "_" + score;
	}
	
}
