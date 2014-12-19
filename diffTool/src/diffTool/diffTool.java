package diffTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class diffTool {

	private String BASELINE_DIR = "data/baseline/Linux/";
	private String RUNTIME_DIR = "data/runtime/rhel/export/";
	
	private String BASELINE_TEMP_DIR = "temp_baseline/";
	private String RUNTIME_TEMP_DIR = "temp_runtime/";
	private String OUTPUT_DIR = "output/";
	
	private Set<String> baselineFilenames = new HashSet<>();
	private Set<String> runtimeFilenames = new HashSet<>();
	private BufferedWriter logFileBufWrtr;
	
	public diffTool()
	{
		FileWriter resultDesc;
		try {
			resultDesc = new FileWriter(OUTPUT_DIR + "results.log");
			logFileBufWrtr = new BufferedWriter(resultDesc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

		try {
//			FileWriter resultDesc = new FileWriter(OUTPUT_DIR + "results.log");
//			BufferedWriter logFileBufWrtr = new BufferedWriter(resultDesc);

			cleanUpTemDirs();

			File baseline_files = new File(BASELINE_DIR);
			File runtime_files = new File(RUNTIME_DIR);

			baselineFilenames = listFilesForFolder(baseline_files);
			runtimeFilenames = listFilesForFolder(runtime_files);

			// Check the baseline files against the runtime files
			Iterator<String> rtItr = runtimeFilenames.iterator();
			while (rtItr.hasNext()) {
				String file = rtItr.next();
				if (!baselineFilenames.contains(file)) {
					System.out.println("** runtime file " + file
							+ " does not exist in the baseline");
					logFileBufWrtr.write("** runtime file " + file
							+ " does not exist in the baseline\n");
				}
			}

			// Check the runtime files against the baseline files
			Iterator<String> blItr = baselineFilenames.iterator();
			while (blItr.hasNext()) {
				String file = blItr.next();
				if (runtimeFilenames.contains(file)) {
					compareFiles(BASELINE_DIR, RUNTIME_DIR, file);
				} else {
					System.out.println("** baseline file " + file
							+ " does not exist in the runtime output");
					logFileBufWrtr.write("** baseline file " + file
							+ " does not exist in the runtime output\n");
				}
			}
			logFileBufWrtr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean cleanUpTemDirs() {

		// Remove the temp folders and previous content
		deleteFolder(new File(BASELINE_TEMP_DIR));
		deleteFolder(new File(RUNTIME_TEMP_DIR));
		deleteFolder(new File(OUTPUT_DIR));
		
		//Create temp folders
		Boolean success = (new File(BASELINE_TEMP_DIR)).mkdirs();
		if (!success) {
		    return false;
		}
		success = (new File(RUNTIME_TEMP_DIR)).mkdirs();
		if (!success) {
		    return false;
		}
		success = (new File(OUTPUT_DIR)).mkdirs();
		if (!success) {
		    return false;
		}
		return true;
	}
	
	public void deleteFolder(File folder) {
		
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		// Delete the main folder
		folder.delete();
	}

	private void compareFiles(String baselinePath, String runtimePath, String filename)  {
		
		try {
			
			// Check if original files match
			if(!CompareDataInFiles.compareData(OUTPUT_DIR, baselinePath, runtimePath, filename))
			{
				List<String> file1List = extractFileContents(baselinePath + filename);
				List<String> file2List = extractFileContents(runtimePath + filename);
				
				// Sort the original files to check for matching content 
				if(createSortedTempFile(BASELINE_TEMP_DIR, filename, file1List) 
						&& createSortedTempFile(RUNTIME_TEMP_DIR, filename, file2List))
				{
					System.out.println("\n" + filename + " - Failed first content check. Checking for lines out of order");
					logFileBufWrtr.write("\n" + filename + " - Failed first content check. Checking for lines out of order");
					
					// Check if files with sorted content match
					if(!CompareDataInFiles.compareData(OUTPUT_DIR, BASELINE_TEMP_DIR, RUNTIME_TEMP_DIR, filename))
					{
						System.out.println("  - Content check failed for " + BASELINE_TEMP_DIR + filename + 
								           " and " + RUNTIME_TEMP_DIR + filename + "\n");
						logFileBufWrtr.write("  - Content check failed for " + BASELINE_TEMP_DIR + filename + 
						           " and " + RUNTIME_TEMP_DIR + filename + "\n");
					}
					else
					{
						System.out.println("  - The contents of the sorted files match\n");
						logFileBufWrtr.write("  - The contents of the sorted files match\n");
					}
				}
			}
			else
			{
//				// Original files match so done 
				System.out.println("The diff check of the original files " + filename + " passed");
				logFileBufWrtr.write("The diff check of the original files " + filename + " passed");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Boolean createSortedTempFile(String path, String filename, List<String> fileList) {

		Collections.sort(fileList);

		try {
			FileWriter resultDesc = new FileWriter(path + filename);
			BufferedWriter resultFileBufWrtr = new BufferedWriter(resultDesc);
			Iterator<String> lines = fileList.iterator();
			while (lines.hasNext()) {
				resultFileBufWrtr.write(lines.next());
				resultFileBufWrtr.newLine();
			}
			resultFileBufWrtr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	private List<String> extractFileContents(String file) throws IOException {
		
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
		
		try {

			
			
			
			
			diffTool obj = new diffTool();
			obj.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Usage: ...");
		}

	}

}
