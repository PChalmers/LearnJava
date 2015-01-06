package diffTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class diffTool {

	private Boolean VERBOSE_STATE = false;
	private String BASELINE_DIR = "";
	private String RUNTIME_DIR = "";

	private String OUTPUT_DIR = "output/";
	private String BASELINE_TEMP_DIR = OUTPUT_DIR + "temp_baseline/";
	private String RUNTIME_TEMP_DIR = OUTPUT_DIR + "temp_runtime/";


	private Set<String> baselineFilenames = new HashSet<>();
	private Set<String> runtimeFilenames = new HashSet<>();
	private BufferedWriter logFileBufWrtr;
	private int lineNumber = 0;
	private String missingFiles = "";

	public diffTool(Boolean verbose, String outputDir) throws Exception {
		FileWriter resultDesc;
		try {
			VERBOSE_STATE = verbose;
			if(!outputDir.equals(""))
			{
				OUTPUT_DIR = fixPathEnding(outputDir);
			}
			
			cleanUpTemDirs();
			resultDesc = new FileWriter(OUTPUT_DIR + "results.log");
			logFileBufWrtr = new BufferedWriter(resultDesc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(String filePath1, String filePath2) {
		
		BASELINE_DIR = fixPathEnding(filePath1);
		RUNTIME_DIR = fixPathEnding(filePath2);

		try {

			File baseline_files = new File(BASELINE_DIR);
			File runtime_files = new File(RUNTIME_DIR);
			
			if(!baseline_files.exists())
			{
				throw new IOException("invalid folder path for baseline_files - " + BASELINE_DIR);
			}
			if(!runtime_files.exists())
			{
				throw new IOException("invalid folder path for runtime_files - " + RUNTIME_DIR);
			}
			
			Map<String, Set<String>> baselineNames = listFilesForFolder(BASELINE_DIR, baseline_files);
			Map<String, Set<String>> runtimeNames = listFilesForFolder(RUNTIME_DIR, runtime_files);

			Iterator<String> baselineItr = baselineNames.keySet().iterator();
			
			while (baselineItr.hasNext()) {
				
				String baselineDir = baselineItr.next();
				String currentBaselineDir = "";
				String currentRuntimeDir = "";
				
				// Handle case where the root dir is the current dir being diffed
				if(baselineDir.equals(BASELINE_DIR))
				{
					// Get the entry from the hashmap for the files in the main dir
					baselineFilenames = baselineNames.get(BASELINE_DIR);
					currentBaselineDir = BASELINE_DIR;
					runtimeFilenames = runtimeNames.get(RUNTIME_DIR);
					currentRuntimeDir = RUNTIME_DIR;
					
				}
				else
				{
					if(runtimeNames.containsKey(baselineDir))
					{
						// Get the entry in the hashmap for the current dir
						baselineFilenames = baselineNames.get(baselineDir);
						currentBaselineDir = BASELINE_DIR + fixPathEnding(baselineDir);
						runtimeFilenames = runtimeNames.get(baselineDir);
						currentRuntimeDir = RUNTIME_DIR + fixPathEnding(baselineDir);
					} else {
						// Do not continue with this dir since it does not exist in both baseline and runtime
					}
				}

				// Check the baseline files against the runtime files
				Iterator<String> rtItr = runtimeFilenames.iterator();
				while (rtItr.hasNext()) {
					String file = rtItr.next();
					if (!baselineFilenames.contains(file)) {
						missingFiles += "** runtime file " + file
								+ " does not exist in the baseline\n";
					}
				}
				// Check the runtime files against the baseline files
				Iterator<String> blItr = baselineFilenames.iterator();
				while (blItr.hasNext()) {
					String file = blItr.next();
					if (runtimeFilenames.contains(file)) {
						compareFiles(currentBaselineDir, currentRuntimeDir, file);
					} else {
						missingFiles += "** baseline file " + file
								+ " does not exist in the runtime dir\n";
					}
				}
				logMessage("\n\n" + missingFiles);
			}
			logFileBufWrtr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cleanUpTemDirs() throws Exception {
		
		BASELINE_TEMP_DIR = OUTPUT_DIR + "temp_baseline/";
		RUNTIME_TEMP_DIR = OUTPUT_DIR + "temp_runtime/";
		
		// Remove the temp folders and previous content
		deleteFolder(new File(BASELINE_TEMP_DIR));
		deleteFolder(new File(RUNTIME_TEMP_DIR));
		deleteFolder(new File(OUTPUT_DIR));

		// Create new dirs
		Boolean success = (new File(OUTPUT_DIR)).mkdirs();
		if (!success) {
			throw new Exception("Can not create Output dir - " + OUTPUT_DIR);
		}
		// Create temp folders
		success = (new File(BASELINE_TEMP_DIR)).mkdirs();
		if (!success) {
			throw new Exception("Can not create Baseline dir - " + BASELINE_TEMP_DIR);
		}
		success = (new File(RUNTIME_TEMP_DIR)).mkdirs();
		if (!success) {
			throw new Exception("Can not create Runtime dir - " + RUNTIME_TEMP_DIR);
		}
	}

	private String fixPathEnding(String stringToFix) {
		// Create new dir names
		if(!stringToFix.endsWith("/"))
		{
			stringToFix = stringToFix += "/";
		}
		return stringToFix;
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

	private void compareFiles(String baselinePath, String runtimePath,
			String filename) {

		try {
			String currentFileStatus = "";
			Boolean filePassed = false;
			currentFileStatus += ++lineNumber + ": File - " + filename + "\n";

			// Check if original files match
			if (!CompareDataInFiles.compareData(OUTPUT_DIR, baselinePath, runtimePath, filename)) {
				
				currentFileStatus += "  - Failed first content check. Checking for lines out of order\n";

				List<String> file1List = extractFileContents(baselinePath + filename);
				List<String> file2List = extractFileContents(runtimePath + filename);

				// Sort the original files to check for matching content
				if (createSortedTempFile(BASELINE_TEMP_DIR, filename, file1List)
						&& createSortedTempFile(RUNTIME_TEMP_DIR, filename, file2List)) {

					// Check if files with sorted content match
					if (!CompareDataInFiles.compareData(OUTPUT_DIR, BASELINE_TEMP_DIR, RUNTIME_TEMP_DIR, filename)) {
						currentFileStatus += "  - Failed second check for sorted file contents\n";
					} else {
						currentFileStatus += "  - Passed - The contents of the sorted files match\n";
					}
				}
				else
				{
					currentFileStatus += "  - Failed sorting the files!\n";
				}
			} else {
				// // Original files match so done
				filePassed = true;
				currentFileStatus += "  - Passed diff check\n";
			}
			if(VERBOSE_STATE || !filePassed) logMessage(currentFileStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void logMessage(String message) throws IOException {
		System.out.println(message);
		logFileBufWrtr.write(message + "\n");
	}

	private Boolean createSortedTempFile(String path, String filename,
			List<String> fileList) {

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

	public Map<String, Set<String>> listFilesForFolder(String foldername, final File folder) {
		Map<String, Set<String>> filenames = new HashMap<>();
		Map<String, Set<String>> tempFilenames = new HashMap<>();
		Set<String> files = new HashSet<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				tempFilenames = listFilesForFolder(fileEntry.getName(), fileEntry);
				Iterator<String> pathItr = tempFilenames.keySet().iterator();
				while(pathItr.hasNext())
				{
					String nextPath = pathItr.next();
					filenames.put(fixPathEnding(foldername) + fixPathEnding(nextPath), tempFilenames.get(nextPath));
					}
				filenames.putAll(tempFilenames);
			} else {
				files.add(fileEntry.getName());
				filenames.put(foldername, files);
			}
		}
		return filenames;
	}

	public static void main(String[] args) {

		Boolean verbose = false;
		String inputFile1 = "";
		String inputFile2 = "";
		String outputDir = "";
		
		// create the command line parser
		CommandLineParser parser = new GnuParser();

		// create the Options
		Options options = new Options();

		options.addOption("h", false, "display help");
		options.addOption("v", false, "Verbose mode");
		
		OptionBuilder.withArgName("baselinefile");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("baseline dir");
		Option baselineFile = OptionBuilder.create("b");
		options.addOption(baselineFile);

		OptionBuilder.withArgName("runtimefile");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("runtime dir");
		Option runtimeFile = OptionBuilder.create("r");
		options.addOption(runtimeFile);

		OptionBuilder.withArgName("outputdir");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("output dir");
		Option outputdir = OptionBuilder.create("o");
		options.addOption(outputdir);
		
		try {
			// parse the command line arguments
			CommandLine cmd = parser.parse(options, args);

			// print the usage
			if (cmd.hasOption("h")) {
				usage();
			}
			
			// print the file diffs that passed as well as failed
			if (cmd.hasOption("v")) {
				verbose = true;
			}
			
			// has the baseline files argument been passed?
			if (cmd.hasOption("b")) {
				// Initialize the member variable
				inputFile1 = cmd.getOptionValue("b");
				System.out.println("Baseline files = " + inputFile1);
			} else {
				usage();
			}

			// has the runtime files argument been passed?
			if (cmd.hasOption("r")) {
				// Initialize the member variable
				inputFile2 = cmd.getOptionValue("r");
				System.out.println("Runtime files = " + inputFile2);
			} else {
				usage();
			}
			
			// has the output dir argument been passed?
			if (cmd.hasOption("o")) {
				// Initialize the member variable
				outputDir = cmd.getOptionValue("o");
				System.out.println("Output Dir = " + outputDir);
			}

		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
			usage();
		}

		try {

			diffTool obj = new diffTool(verbose, outputDir);
			obj.run(inputFile1, inputFile2);
		} catch (Exception e) {

			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void usage() {
		
		System.out.println("diffTool \n"
				         + "-h help\n"
				         + "-v Verbose          - default false\n"
				         + "-b <baseline dir> \n"
				         + "-r <runtime file> \n"
				         + "-o <output dirname> - default \"output\"\n");
		System.exit(1);
	}
}
