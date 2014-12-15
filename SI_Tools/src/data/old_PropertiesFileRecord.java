package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class old_PropertiesFileRecord {

	// Hash for storing properties
	Map<String, String> properties = new HashMap<>();
	

	public old_PropertiesFileRecord(String fileName) throws FileNotFoundException, IOException {
		super();
		readPropertyFile(fileName);

	}

	/**
	 * @param properties
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readPropertyFile(String filename) 
			throws FileNotFoundException, IOException {
		// New BufferedReader.
		BufferedReader reader = new BufferedReader(new FileReader(filename));

		// Add all lines from file to ArrayList.
		while (true) {
		    String line = reader.readLine();
		    if (line == null) {
			break;
		    }
			if (!line.startsWith("#")) {
				String[] parts = line.split("=");
				if(parts.length == 2) {					
					properties.put(parts[0], parts[1]);
				}
			}
		}

		// Close it.
		reader.close();
	}
	
	
//	private void readPropertyFiles() {
//		// Read in the property files
//		data.readPropertyFiles();
//
//		Set<String> fields = data.getUniqueFields();
//		Iterator<String> e = fields.iterator();
//		while (e.hasNext()) {
//			String field = e.next();
//			System.out.println("The field " + field + " has " + data.getNumForUniqueFeild(field) + " values");
//
//			Map<String, Map<String, String>> results = data.getDataForUniqueFeild(field);
//			Set<String> propertyFiles = results.keySet();
//			Iterator<String> pf = propertyFiles.iterator();
//			
//			while (pf.hasNext()) {
//				String file = pf.next();
//				System.out.println("The file " + file + " contains " + field + "=" + results.get(file).get(field));
//			}
//		}
//	}

}
