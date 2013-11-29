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

package kmerDatabase;

public class KmerQueryResult implements Comparable<KmerQueryResult>
{
	private final String id;
	private final int counts;
	
	public String getId()
	{
		return id;
	}
	
	public int getCounts()
	{
		return counts;
	}
	
	public KmerQueryResult(String id, int counts)
	{
		this.id = id;
		this.counts = counts;
	}

	@Override
	public String toString()
	{
		return this.id + " " + this.counts;
	}

	@Override
	public int compareTo(KmerQueryResult o)
	{
		return o.counts - this.counts;
	}
}
