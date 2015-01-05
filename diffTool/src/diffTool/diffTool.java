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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class diffTool {

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

	public diffTool() {
		FileWriter resultDesc;
		try {
			cleanUpTemDirs();
			resultDesc = new FileWriter(OUTPUT_DIR + "results.log");
			logFileBufWrtr = new BufferedWriter(resultDesc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(String outputDir, String file1, String file2) {
		
		BASELINE_DIR = file1;
		RUNTIME_DIR = file2;
		if(!outputDir.equals(""))
		{
			OUTPUT_DIR = outputDir;
			BASELINE_TEMP_DIR = OUTPUT_DIR + "temp_baseline/";
			RUNTIME_TEMP_DIR = OUTPUT_DIR + "temp_runtime/";
		}
			
		
		try {
			// FileWriter resultDesc = new FileWriter(OUTPUT_DIR +
			// "results.log");
			// BufferedWriter logFileBufWrtr = new BufferedWriter(resultDesc);

//			cleanUpTemDirs();

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
			baselineFilenames = listFilesForFolder(baseline_files);
			runtimeFilenames = listFilesForFolder(runtime_files);

			// Check the baseline files against the runtime files
			Iterator<String> rtItr = runtimeFilenames.iterator();
			while (rtItr.hasNext()) {
				String file = rtItr.next();
				if (!baselineFilenames.contains(file)) {
					missingFiles += "** runtime file " + file + " does not exist in the baseline\n";
//					logMessage("** runtime file " + file + " does not exist in the baseline");
				}
			}

			// Check the runtime files against the baseline files
			Iterator<String> blItr = baselineFilenames.iterator();
			while (blItr.hasNext()) {
				String file = blItr.next();
				if (runtimeFilenames.contains(file)) {
					compareFiles(BASELINE_DIR, RUNTIME_DIR, file);
				} else {
					missingFiles += "** runtime file " + file + " does not exist in the baseline\n";
//					logMessage("** baseline file " + file + " does not exist in the runtime output");
				}
			}
			logMessage("\n\n" + missingFiles);
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

		Boolean success = (new File(OUTPUT_DIR)).mkdirs();
		if (!success) {
			return false;
		}
		// Create temp folders
		success = (new File(BASELINE_TEMP_DIR)).mkdirs();
		if (!success) {
			return false;
		}
		success = (new File(RUNTIME_TEMP_DIR)).mkdirs();
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

	private void compareFiles(String baselinePath, String runtimePath,
			String filename) {

		try {
			logMessage(++lineNumber + ": File - " + filename);

			// Check if original files match
			if (!CompareDataInFiles.compareData(OUTPUT_DIR, baselinePath, runtimePath, filename)) {
				List<String> file1List = extractFileContents(baselinePath + filename);
				List<String> file2List = extractFileContents(runtimePath + filename);

				// Sort the original files to check for matching content
				if (createSortedTempFile(BASELINE_TEMP_DIR, filename, file1List)
						&& createSortedTempFile(RUNTIME_TEMP_DIR, filename,
								file2List)) {
					logMessage("  - Failed first content check. Checking for lines out of order");

					// Check if files with sorted content match
					if (!CompareDataInFiles.compareData(OUTPUT_DIR,
							BASELINE_TEMP_DIR, RUNTIME_TEMP_DIR, filename)) {
						logMessage("  - Content check failed for "
								+ BASELINE_TEMP_DIR + filename + " and "
								+ RUNTIME_TEMP_DIR + filename);
					} else {
						logMessage("  - The contents of the sorted files match");
					}
				}
			} else {
				// // Original files match so done
				logMessage("  - passed");
			}
//			System.out.println("");
//			logFileBufWrtr.write("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

		String inputFile1 = "";
		String inputFile2 = "";
		String outputDir = "";
		// create the command line parser
		CommandLineParser parser = new GnuParser();

		// create the Options
		Options options = new Options();

		options.addOption("h", false, "display help");

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

			if (cmd.hasOption("h")) {
				// print the date and time
				usage();
			}
			
			// has the buildfile argument been passed?
			if (cmd.hasOption("b")) {
				// Initialize the member variable
				inputFile1 = cmd.getOptionValue("b");
				System.out.println("Baseline file = " + inputFile1);
			} else {
				usage();
			}

			// has the build file argument been passed?
			if (cmd.hasOption("r")) {
				// Initialize the member variable
				inputFile2 = cmd.getOptionValue("r");
				System.out.println("Runtime file = " + inputFile2);
			} else {
				usage();
			}
			
			// has the build file argument been passed?
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

			diffTool obj = new diffTool();
			obj.run(outputDir, inputFile1, inputFile2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception caught - " + e.getMessage());
			e.printStackTrace();
//			System.out.println("Usage: ...");
		}

	}

	private static void usage() {
		// TODO Auto-generated method stub
		
		System.out.println("diffTool \n"
				         + "-h help\n"
				         + "-b <baseline dir> \n"
				         + "-r <runtime file> \n");
		System.exit(1);
	}

}
