import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CastParser extends DefaultHandler {

    private String tempVal;
    private MoviesCast tempMovieCast;
    List<MoviesCast> moviesList;
    List<String> listOfInconsistencies;

    public CastParser() {
        moviesList = new ArrayList<MoviesCast>();
        listOfInconsistencies = new ArrayList<String>();
    }

    private void parseDocument(){
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData(){

        System.out.println("Number of Movies Inserted: '" + moviesList.size() + "'.");

        Iterator<MoviesCast> it = moviesList.iterator();
        Iterator<String> incons = listOfInconsistencies.iterator();

        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }

        System.out.println("Number of inconsistencies found in casts124.xml: '" + listOfInconsistencies.size() + "'.");
        System.out.println("All inconsistencies found in casts124.xml: ");

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
        if (qName.equalsIgnoreCase("filmc")) {
            tempMovieCast = new MoviesCast();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("filmc")) {
            try {
                moviesList.add(tempMovieCast);
            } catch (Exception e) {
                System.out.println("Failure to add movieCast object into moviesList.");
            }
        } else if (qName.equalsIgnoreCase("f")) {
            try {
                tempMovieCast.setId(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Movie ID (<f>): " + tempVal);
                System.out.println("Error in adding Id to tempMoviesCast - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("t")) {
            try {
                tempMovieCast.setTitle(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Title(<t>): " + tempVal);
                System.out.println("Error in adding Title to tempMoviesCast - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("a")) {
            try {
                tempMovieCast.addActor(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Actor (<a>): " + tempVal);
                System.out.println("Failure in adding Actor to tempMoviesCast - tempVal: " + tempVal);
            }
        }
    }

    public static void main(String args[]) {
        CastParser castParser = new CastParser();
        castParser.runExample();
    }

}
