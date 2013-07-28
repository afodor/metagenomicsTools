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

package test.testReduceOTU;


import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSuite1 
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for covariance.test");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestHashHolder.class));
		suite.addTest(new TestSuite(TestBandwithConstrainedAlignerFromLeft.class));
		suite.addTest(new TestSuite(TestBandwithConstrainedAlignerFromRight.class));
		
		//$JUnit-END$
		return suite;
	}
}
