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