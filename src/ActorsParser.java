import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class ActorsParser extends DefaultHandler {

//    List<Actors> actorsList;
    Set<Actors> actorsList;
    private Actors tempActor;
    private String tempVal;
    List<String> listOfInconsistencies;
    Set<String> dupes;

    public ActorsParser() {
        //actorsList = new ArrayList<Actors>();
        actorsList = new HashSet<Actors>();
        listOfInconsistencies = new ArrayList<String>();
        dupes = new HashSet<String>();
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

        System.out.println("Number of Actors Inserted: '" + actorsList.size() + "'.");

        Iterator<Actors> it = actorsList.iterator();
        Iterator<String> incons = listOfInconsistencies.iterator();

        while (it.hasNext()) {
            Actors temp = it.next();
            System.out.println("NAME: " + temp.getName() + " |BIRTHYEAR: " + temp.getBirthYear());
        }

        System.out.println("Number of inconsistencies found in actors63.xml: '" + listOfInconsistencies.size() + "'.");
        System.out.println("All inconsistencies found in actors63.xml: ");
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
                if(dupes.contains(tempActor.getName())){
                    System.out.println("###############WARNING############################\n"
                            + tempActor.getName());
                }

                dupes.add(tempActor.getName());

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
                if (!tempVal.isEmpty()) {
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
