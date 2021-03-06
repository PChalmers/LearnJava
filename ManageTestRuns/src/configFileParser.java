
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class configFileParser {

	//No generics
	List<Employee> myEmpls;
	Document dom;
	TestRunDetails testdetails;


	public configFileParser(){
		//create a list to hold the employee objects
		myEmpls = new ArrayList<Employee>();
	}

	public void runExample() {
		
		//parse the xml file and get the dom object
		parseXmlFile();
		
		//get each employee element and create a Employee object
		parseDocument();
		
		//Iterate through the list and print the data
//		printData();
		
	}
	
	
	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse("test_runs.xml");
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	private void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <platforms> elements
		NodeList nl = docEle.getElementsByTagName("platforms");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the employee element
				Element el = (Element)nl.item(i);
				//get the Employee object
				testdetails.setPlatform(getNodeValues(el));
			}
		}
		
		//get a nodelist of <platforms> elements
		nl = docEle.getElementsByTagName("streams");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the employee element
				Element el = (Element)nl.item(i);
				//get the Employee object
				testdetails.setStreams(getNodeValues(el));
			}
		}
//		
//		//get a nodelist of <run> elements
//		nl = docEle.getElementsByTagName("run");
//		if(nl != null && nl.getLength() > 0) {
//			for(int i = 0 ; i < nl.getLength();i++) {
//				//get the employee element
//				Element el = (Element)nl.item(i);
//				//get the Employee object
//				getPlatforms(el);
//			}
//		}
	}


	/**
	 * I take an employee element and read the values in, create
	 * an Employee object and return it
	 * @param empEl
	 * @return
	 */
	private Map<String, String> getNodeValues(Element empEl) {
		
		Map<String, String> returnValue = new HashMap<String, String>();
		//for each <employee> element get text or int values of 
		//name ,id, age and name
		String platform = empEl.getTagName();
		NodeList children = empEl.getChildNodes();
		for(int i=0;i<children.getLength();i++)
		{
			Node child = children.item(i);
			if(child instanceof Element)
			{
				Element childElement = (Element)child;
				String node = childElement.getTagName();
				System.out.println(node);
				Text textNode = (Text) childElement.getFirstChild();
				String value = textNode.getData().trim();
				System.out.println(value);
				returnValue.put(node, value);
			}
		}
		return returnValue;
	}


	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	
	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	/**
	 * Iterate through the list and print the 
	 * content to console
	 */
	private void printData(){
		
		System.out.println("No of Employees '" + myEmpls.size() + "'.");
		
		Iterator<Employee> it = myEmpls.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}

	
	public static void main(String[] args){
		//create an instance
		configFileParser dpe = new configFileParser();
		
		//call run example
		dpe.runExample();
	}

}

