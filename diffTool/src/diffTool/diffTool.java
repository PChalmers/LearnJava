package diffTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class diffTool {

	private String BASELINE_DIR = "data/baseline/SunOS";
	private String RUNTIME_DIR = "data/runtime/sunos/export";
	
	private Set<String> baselineFilenames = new HashSet<>();
	private Set<String> runtimeFilenames = new HashSet<>();
	
	public void run() {
		
		File baseline_files = new File(BASELINE_DIR);
		File runtime_files = new File(RUNTIME_DIR);
		
		baselineFilenames = listFilesForFolder(baseline_files);
		runtimeFilenames = listFilesForFolder(runtime_files);
		
		Iterator<String> blItr = baselineFilenames.iterator();
		while(blItr.hasNext())
		{
			String file = blItr.next();
			System.out.println(file);
			if(runtimeFilenames.contains(file))
			{
				compareFiles(BASELINE_DIR + file, RUNTIME_DIR + file);
			}
			else
			{
				System.out.println(file + " does not exist in both files");
			}
		}
		
		System.out.println("\n\n");
		
		Iterator<String> rtItr = runtimeFilenames.iterator();
		while(rtItr.hasNext())
		{
			System.out.println(rtItr.next());
		}		
		
	}
	
	private void compareFiles(String file1, String file2)  {
		
		try {
			List<String> file1List = loadFile(file1);
			List<String> file2list = loadFile(file2);
			
			Collections.sort(file1List);
			Collections.sort(file2list);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private List<String> loadFile(String file) throws IOException {
		// TODO Auto-generated method stub
		
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String inputLine;
		List<String> lineList = new ArrayList<String>();
		while ((inputLine = bufferedReader.readLine()) != null) {
			lineList.add(inputLine);
		}
		fileReader.close();
		return lineList;
	}

	public Set<String> listFilesForFolder(final File folder) {
		Set<String> filenames = new HashSet<>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	filenames.add(fileEntry.getName());
	        }
	    }
	    return filenames;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {

			diffTool obj = new diffTool();
			obj.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Usage: ...");
		}

	}

}
