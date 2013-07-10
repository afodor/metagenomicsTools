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


package sequenceRuns;

import java.io.File;
import java.util.List;

public abstract class RunDescription
{
	
	public abstract List<File> getQueryFastaFiles() throws Exception;
	public abstract List<File> getTargetFastaFiles() throws Exception;
	
	public abstract List<File> getSearchResultFiles() throws Exception;
	
	public abstract String getQueryDescription() throws Exception;
	public abstract String getTargetDescription() throws Exception;
	
	public abstract String getSearchString() throws Exception;
	public abstract boolean resultFilesGZipped() throws Exception;
	
	public abstract File getTopResultsDir() throws Exception;
	
}