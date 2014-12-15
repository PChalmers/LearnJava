package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PropertiesFileRecord {

	String version;
	String path;
	String solution;
	
	// Map of filename -> properties
	Map<String, Map<String, String>> fileData = new HashMap<>();
	
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Map<String, String> getFileProperties(String file) {
		return fileData.get(file);
	}
	public void setFileProperties(String file, HashMap<String, String> propertiesFileRecord) {
		fileData.put(file, propertiesFileRecord);
	}
	
	public Set<String> getFileNames()
	{
		return fileData.keySet();
	}
	
	public Boolean readPropertiesFiles() throws Exception {
		// TODO Auto-generated method stub
		
		Set<String> files = fileData.keySet();

		Iterator<String> itr = files.iterator();

		while (itr.hasNext()) {
			String filename = itr.next();
			try {
				// Read property file and return the properties in a Map
				fileData.put(filename, readPropertyFile(path + filename));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}

		return true;
	}

	
	/**
	 * @param properties
	 */
	public Map<String, Integer> getUniquePropertiesList() {
		// Print each line.
		
		Map<String, Integer> propertiyList = new HashMap<>();
		
		Iterator<String> filenameItr = fileData.keySet().iterator();
		while (filenameItr.hasNext())
		{
			String filename = filenameItr.next();
			Map<String, String> data = fileData.get(filename);
			Iterator<String> itr2 = data.keySet().iterator();
			while (itr2.hasNext())
			{
				String field = itr2.next();
				if(propertiyList.containsKey(field))
				{
					propertiyList.put(field, propertiyList.get(field)+1);
				}
				else
				{
					propertiyList.put(field, 1);
				}
			}
		}
		return propertiyList;
	}
	
	public Map<String, String> getDataByProperty(String property)
	{
		Map<String, String> outPutResults = new HashMap<>();
		Set<String> keys = fileData.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext())
		{
			String filename = itr.next();
			Map<String, String> data = fileData.get(filename);
			Iterator<String> itr2 = data.keySet().iterator();
			while (itr2.hasNext())
			{
				String field = itr2.next();
				if(property.equals(field))
				{
					outPutResults.put(filename, data.get(field));
				}
			}
		}
		
		return outPutResults;
	}

	/**
	 * @param properties
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Map<String, String> readPropertyFile(String filename) 
			throws FileNotFoundException, IOException {
		
		Map<String, String> results = new HashMap<>();
		
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
					results.put(parts[0], parts[1]);
				}
			}
		}

		// Close it.
		reader.close();
		
		return results;
	}
	
	public String toString()
	{
		String outputString = "Summay: \n" + "The Version is : " + version + "\n" 
	                         + "The Solution is : " + solution + "\n" 
				             + "The Path is : " + path + "\n";
		
		Set<String> keys = fileData.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext())
		{
			String filename = itr.next();
			outputString+= "\nProperties for file: " + filename + "\n";
			Map<String, String> data = fileData.get(filename);
			Iterator<String> itr2 = data.keySet().iterator();
			while (itr2.hasNext())
			{
				String field = itr2.next();
				outputString+= "Field: " + field + ", value: " + data.get(field) + "\n";
			}
		}
		return outputString;
	}
	
	
}
