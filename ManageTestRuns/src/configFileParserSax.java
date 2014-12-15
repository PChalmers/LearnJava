
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class configFileParserSax extends DefaultHandler {

     private static List<Employee> employees = new ArrayList<Employee>();
     private static Employee empl = null;
     private static String text = null;

     @Override
     // A start tag is encountered.
     public void startElement(String uri, String localName, String qName, Attributes attributes)
          throws SAXException {

          switch (qName) {
               // Create a new Employee.
               case "Employee": {
                    empl = new Employee();
                    empl.setID(attributes.getValue("ID"));
                    break;
               }
          }
     }

     @Override
     public void endElement(String uri, String localName, String qName) throws SAXException {
          switch (qName) {
               case "Employee": {
                    // The end tag of an employee was encountered, so add the employee to the list.
                    employees.add(empl);
                    break;
               }
               case "Firstname": {
                    empl.setFirstname(text);
                    break;
               }
               case "Lastname": {
                    empl.setLastname(text);
                    break;
               }
               case "Age": {
                    empl.setAge(Integer.parseInt(text));
                    break;
               }
               case "Salary": {
                    empl.setSalary(Double.parseDouble(text));
                    break;
               }
          }
     }
     
     @Override
     public void characters(char[] ch, int start, int length) throws SAXException {
          text = String.copyValueOf(ch, start, length).trim();
     }

     public static void main(String[] args) throws ParserConfigurationException,
          SAXException, IOException {

          if (args.length != 1)
               throw new RuntimeException("The name of the XML file is required!");

          SAXParserFactory parserFactor = SAXParserFactory.newInstance();
          SAXParser parser = parserFactor.newSAXParser();
          configFileParserSax handler = new configFileParserSax();

          parser.parse(new File(args[0]), handler);

          // Print all employees.
          for (Employee empl : employees)
               System.out.println(empl.toString());
     }
}

