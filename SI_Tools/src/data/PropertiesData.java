package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PropertiesData {
	
	// Map the version to the properties
	HashMap<String, PropertiesFileRecord> propertyResults = new HashMap<String, PropertiesFileRecord>();
	// Set of unique fields in the 
	static String configFile = "Data/propertyFiles2.cfg";

	static public String Version_500 = "5.0.0";
	static public String Version_490 = "4.9.0";
	
	public PropertiesData() {
		
		// Read in the property files
		readPropertyFiles();	
	}

	public Set<String> getUniqueFields() {
		
		Set<String> uniqueFields = new HashSet<>();
		Set<String> keys = propertyResults.keySet();
		System.out.println(keys.size() + " keys found\n\n");
		Iterator<String> keysitr = keys.iterator();
		while(keysitr.hasNext())
		{
			String filename = keysitr.next();
			PropertiesFileRecord record = propertyResults.get(filename);
			
			Map<String, Integer> properties = record.getUniquePropertiesList();
			Set<String> propertiesKeys = properties.keySet();
			Iterator<String> propertylist = propertiesKeys.iterator();
			
			while(propertylist.hasNext())
			{
				String property = propertylist.next();
				
				if(!uniqueFields.contains(property)) {
					uniqueFields.add(property);					
				}
			}
		}
		return uniqueFields;
		
	}
	
	public Map<String, String> getDataForField(String version, String keyToFind) {

		Map<String, String> resultEntry = new HashMap<>();
		Set<String> uniqueFields = getUniqueFields();

		if (uniqueFields.contains(keyToFind)) {

			PropertiesFileRecord fileData = propertyResults.get(version);

			Map<String, String> data = fileData.getDataByProperty(keyToFind);
			Set<String> filenames = data.keySet();
			Iterator<String> fileNamesItr = filenames.iterator();
			while (fileNamesItr.hasNext()) {
				String filename = fileNamesItr.next();
				String value = data.get(filename);
				resultEntry.put(filename, value);
			}
		}
		return resultEntry;
	}
			

	public void readPropertyFiles() {

		// Declarations
		try {

			readConfigFile(configFile);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param files
	 * @param configFile
	 * @throws Exception 
	 */
	private void readConfigFile(String configFile)
			throws Exception {

		// Prepare file for reading data
		BufferedReader reader = new BufferedReader(new FileReader(configFile));

		// Add all lines from file to ArrayList.
		String line;

		// Check for a line in the file
		while ((line = reader.readLine()) != null) {

			PropertiesFileRecord newProperties = new PropertiesFileRecord();
			// Check for valid content in the line
			String[] parts = line.split(",");
			if (parts.length < 4) {
				continue;
			}
			// Divide up the line
			newProperties.setVersion(parts[0]);
			newProperties.setPath(parts[1]);
			newProperties.setSolution(parts[2]);

			for (int i = 3; i < parts.length; i++) {
				newProperties.setFileProperties(parts[i],
						new HashMap<String, String>());
			}
			if(newProperties.readPropertiesFiles())
			{
				System.out.println(newProperties.toString());
				propertyResults.put(newProperties.getVersion(), newProperties);
			}
		}

		// Close it.
		reader.close();
	}
	
	public void WritePropertiesFileSunmmary() {
		// Setup file to write to
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("filename.txt"), "utf-8"));
			writer.write("Something");

			// For each version
			Iterator<String> versions = propertyResults.keySet().iterator();
			while(versions.hasNext())
			{
				String version = versions.next();
				
				PropertiesFileRecord data = propertyResults.get(version);
				Iterator<String> properties = data.getUniquePropertiesList();
			}

			// For each solution

			// Write out data header

			// Write out filename then values for each property

			// Close file
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

	}
	
	
//	/**
//	 * @param files
//	 * @param configFile
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 */
//	private void readConfigFile(String configFile)
//			throws FileNotFoundException, IOException {
//
//        // Prepare file for reading data
//		BufferedReader reader = new BufferedReader(new FileReader(configFile));
//
//		// Add all lines from file to ArrayList.
//		String line;
//		PropertiesFileRecord newProperties = new PropertiesFileRecord();
//		Boolean processSection = false;
//		
//		// Check for a line in the file
//		while ((line = reader.readLine()) != null) {
//
//			// Check for valid content in the line
//			String[] parts = line.split(":");
//			if (parts.length != 2) {
//				continue;
//			}
//			// Divide up the line
//			String field = parts[0];
//			String value = parts[1];
//
//			
//			if (field.equals("Start")) {
//				newProperties = new PropertiesFileRecord();
//				processSection = true;
//			}
//
//			if (processSection == true) {
//				switch (field) {
//				case "Version":	{
//					newProperties.setVersion(value);
//					break;
//				}
//				case "Path": {
//					newProperties.setPath(value);
//					break;
//				}
//				case "Solution": {
//					newProperties.setSolution(value);
//					break;
//				}
//				case "File": {
//					newProperties.setFileProperties(value, new HashMap<String, String>());
//					break;
//				}
//				case "Stop":
//				{
//					processSection = false;
//					try {
//						
//						newProperties.readPropertiesFiles();
//						System.out.println(newProperties.toString());
//						propertyResults.put(newProperties.getVersion(), newProperties);
//						
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						System.out.println("Results not added for " + newProperties.getVersion() + newProperties.getPath());
//					}
//					break;
//				}
//				}
//			}
//		}
//
//		// Close it.
//		reader.close();
//	}
}