package diffTool;

import java.io.*;

public class CompareDataInFiles {

	public static boolean compareData(String output_dir, String baselinePath, String runtimePath, String filename)
	{
		// File names to Read & Write.
		String actualFile =  baselinePath + filename;
		String expectedFile = runtimePath + filename;
		String descptnFile = output_dir + filename + ".txt";
		boolean isSame = true;
		
		try {
			
			File checkFile1 = new File(actualFile);
			if(!checkFile1.exists())
			{
				System.out.println(actualFile + " does not exist");
				return false;
			}
			File checkFile2 = new File(expectedFile);
			if(!checkFile2.exists())
			{
				System.out.println(expectedFile + " does not exist");
				return false;
			}
			
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("File 1 - " + actualFile + "\n");
			stringBuffer.append("File 2 - " + expectedFile + "\n\n");
			// Create FileReader & Writer Objects.
			FileReader actualFileReader = new FileReader(actualFile);
			FileReader expctdFileReader = new FileReader(expectedFile);
			
			// Create Buffered Object.
			BufferedReader actlFileBufRdr = new BufferedReader(actualFileReader);
			BufferedReader expcFileBufRdr = new BufferedReader(expctdFileReader);

			// Get the file contents into String Variables.
			String actlFileContent = actlFileBufRdr.readLine();
			String expctdFileContent = expcFileBufRdr.readLine();

			int lineNumber = 1;

			if (actlFileContent != null || expctdFileContent != null) {

				// Check whether Actual file contains data or not
				while ((actlFileContent != null)) {

					// Check whether Expected file contains data or not
					if ((expctdFileContent) != null) {

						// Check whether both the files have same data in the
						// lines
						if (!actlFileContent.equals(expctdFileContent)) {
							stringBuffer.append("Line " + lineNumber + "\n" 
						     +   " - File 1:" + actlFileContent
							 + "\n - File 2:" + expctdFileContent + "\n");
							isSame = false;
						}
						lineNumber = lineNumber + 1;
						expctdFileContent = expcFileBufRdr.readLine();
					} else {
						stringBuffer.append("Line " + lineNumber + "\n" 
							     +   " - File 1:" + actlFileContent
								 + "\n - File 2:" + expctdFileContent + "\n");
						isSame = false;
						lineNumber = lineNumber + 1;
					}
					actlFileContent = actlFileBufRdr.readLine();
				}

				// Check for the condition : if Actual File has Data & Expected
				// File doesn't contain data.
				while ((expctdFileContent != null) && (actlFileContent == null)) {
					stringBuffer.append("Line " + lineNumber + "\n" 
						     +   " - File 1:" + actlFileContent
							 + "\n - File 2:" + expctdFileContent + "\n");
					isSame = false;
					lineNumber = lineNumber + 1;
					expctdFileContent = expcFileBufRdr.readLine();
				}
			} else {
				// Mention that both the files don't have Data.
				stringBuffer.append("Difference in Line " + lineNumber
						+ " :- Actual File contains :\n" + actlFileContent
						+ "\nExpected File Contains - \n" + expctdFileContent);
				isSame = false;
				System.out.println("false4");
			}

			// Check is there any difference or not.
			if (!isSame) {
				FileWriter resultDesc = new FileWriter(descptnFile);
				BufferedWriter resultFileBufWrtr = new BufferedWriter(resultDesc);
				resultFileBufWrtr.write(stringBuffer.toString());
				resultFileBufWrtr.newLine();
				resultFileBufWrtr.close();
			}

			// Close the streams.
			actualFileReader.close();
			expctdFileReader.close();
			actlFileBufRdr.close();
			expcFileBufRdr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSame;
	}

}
