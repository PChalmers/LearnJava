package Selection;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import data.PropertiesData;


public class Selection {

	// Initialize the data
	PropertiesData data = new PropertiesData();

	public Selection() {

	}

	public void run() {

		// Get a list of all unique fields in the data
		Set<String> fields = data.getUniqueFields();
		
		// Print out the data 
		Iterator<String> e = fields.iterator();
		while (e.hasNext()) {
			String field = e.next();

			Map<String, String> dataResults = data.getDataForField(PropertiesData.Version_500, field);
			Set<String> files = dataResults.keySet();
			Iterator<String> filesItr = files.iterator();
			while(filesItr.hasNext())
			{
				String filename = filesItr.next();
				System.out.println("The file " + filename + " has the value " + dataResults.get(filename));
			}
		}
		data.WritePropertiesFileSunmmary();
	}

	public static void main(String[] args) {

		try {

			Selection obj = new Selection();
			obj.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Usage: ...");
		}
		
	}

}
