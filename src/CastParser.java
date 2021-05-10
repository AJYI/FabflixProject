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

import java.io.FileWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CastParser extends DefaultHandler {

    private String tempVal;
    private MoviesCast tempMovieCast;
    List<MoviesCast> moviesList;
    List<String> listOfInconsistencies;
    FileWriter fw;
    FileWriter castErrors;

    public CastParser() {
        moviesList = new ArrayList<MoviesCast>();
        listOfInconsistencies = new ArrayList<String>();
    }

    private void parseDocument(){
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();

        try {
            // Set up file to write into
            fw = new FileWriter("CastResults.txt");
            castErrors = new FileWriter("CastInconsistencies.txt");

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

    private void printData() {

        System.out.println("Number of Movies Inserted: '" + moviesList.size() + "'.");

        Iterator<MoviesCast> it = moviesList.iterator();
        Iterator<String> incons = listOfInconsistencies.iterator();

        try {
            while (it.hasNext()) {
                fw.write(it.next().toString());
                System.out.println(it.next().toString());
            }

            System.out.println("Number of inconsistencies found in casts124.xml: '" + listOfInconsistencies.size() + "'.");
            System.out.println("All inconsistencies found in casts124.xml: ");

            while (incons.hasNext()) {
                castErrors.write(incons.next());
                System.out.println(incons.next());
            }
        } catch (Exception e) {
            System.out.println("Error in writing data");
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
        Pattern pattern = Pattern.compile("[^a-z0-9,:'+.!/]-", Pattern.CASE_INSENSITIVE);
        Pattern namePattern = Pattern.compile("[^a-z]-", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        Matcher nameMatcher;
        boolean foundSpec;

        if(qName.equalsIgnoreCase("filmc")) {
            try {
                moviesList.add(tempMovieCast);
            } catch (Exception e) {
                System.out.println("Failure to add movieCast object into moviesList.");
            }
        } else if (qName.equalsIgnoreCase("f")) {
            try {
                matcher = pattern.matcher(tempVal);
                foundSpec = matcher.find();
                if(!foundSpec) {
                    tempMovieCast.setId(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Movie ID (<f>): " + tempVal);
                tempMovieCast.setId(null);
                System.out.println("Error in adding Id to tempMoviesCast - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("t")) {
            try {
                matcher = pattern.matcher(tempVal);
                foundSpec = matcher.find();
                if(!foundSpec) {
                    tempMovieCast.setTitle(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Title(<t>): " + tempVal);
                tempMovieCast.setTitle(null);
                System.out.println("Error in adding Title to tempMoviesCast - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("a")) {
            try {
                nameMatcher = namePattern.matcher(tempVal);
                foundSpec = nameMatcher.find();
                if(!foundSpec) {
                    tempMovieCast.addActor(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Actor (<a>): " + tempVal);
                tempMovieCast.addActor(null);
                System.out.println("Special character found. Setting actor to null");
                System.out.println("Failure in adding Actor to tempMoviesCast - tempVal: " + tempVal);
            }
        }
    }

    public static void main(String args[]) {
        CastParser castParser = new CastParser();
        castParser.runExample();
    }

}
