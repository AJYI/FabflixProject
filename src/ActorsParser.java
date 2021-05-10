import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class ActorsParser extends DefaultHandler {

    List<Actors> actorsList;
    private Actors tempActor;
    private String tempVal;
    List<String> listOfInconsistencies;

    public ActorsParser() {
        actorsList = new ArrayList<Actors>();
        listOfInconsistencies = new ArrayList<String>();
    }

    private void parseDocument(){
        // get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("actors63.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData(){

        System.out.println("No of Actors '" + actorsList.size() + "'.");

        Iterator<Actors> it = actorsList.iterator();
        Iterator<String> incons = listOfInconsistencies.iterator();

        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }

        System.out.println("All inconsistencies found: ");

        while (incons.hasNext()) {
            System.out.println(incons.next().toString());
        }
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            tempActor = new Actors();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor")) {
            // Add the directorFilm object to the List of directorFilms
            try {
                actorsList.add(tempActor);
//                System.out.println("Added another Actor to actorsList");
            } catch (Exception e) {
                System.out.println("Error in adding Actors to actorsList");
            }
        } else if (qName.equalsIgnoreCase("stagename")) {
            // If all required info from movie is gotten, then add the movie to the arraylist
            try {
                tempActor.setName(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Actor Name (<stagename>): " + tempVal);
                System.out.println("Error in setting actor's name - tempVal " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("dob")) {
            // If all required info from movie is gotten, then add the movie to the arraylist
            try {
                tempActor.setBirthYear(Integer.parseInt(tempVal));
            } catch (Exception e) {
                if (!tempVal.equals(null)) {
                    listOfInconsistencies.add("Actor Year Of Birth (<dob>): " + tempVal);
                }
//                tempActor.setBirthYear(null);
                System.out.println("Error in setting actor's year of birth - tempVal " + tempVal);
            }
        }
    }

    public static void main(String[] args) {
        ActorsParser spe = new ActorsParser();
        spe.runExample();
    }


}
