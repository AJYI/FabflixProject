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

public class MoviesParser extends DefaultHandler {

    private String tempVal;
    private Movies tempMovie;
    private DirectorFilms tempDirFilms;
    List<DirectorFilms> directorFilms;
    List<String> listOfInconsistencies;
    FileWriter fw;
    FileWriter errorWriter;

    public MoviesParser() {
        directorFilms = new ArrayList<DirectorFilms>();
        listOfInconsistencies = new ArrayList<String>();
    }

    // SAX Parser
    private void parseDocument(){
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            fw = new FileWriter("Mains243Output.txt");
            errorWriter = new FileWriter("Mains243Inconsistencies.txt");
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData(){

        System.out.println("Number of Movies Inserted '" + directorFilms.size() + "'.");

        Iterator<DirectorFilms> it = directorFilms.iterator();
        Iterator<String> incons = listOfInconsistencies.iterator();

        try {
            while (it.hasNext()) {
                System.out.println(it.next().toString());
                fw.write(it.next().toString());
            }

            System.out.println("Number of inconsistencies found in mains243.xml: '" + listOfInconsistencies.size() + "'.");
            System.out.println("All inconsistencies found in mains243.xml: ");

            while (incons.hasNext()) {
                errorWriter.write(incons.next());
                System.out.println(incons.next());
            }
        } catch (Exception e) {
            System.out.println("Error in writing data to file");
        }
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    // Looks for the starting tags to indicate when to create a new object of the associated classes
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("directorfilms")) {
            tempDirFilms  = new DirectorFilms();
            tempDirFilms.setMovieList();
        }
        else if (qName.equalsIgnoreCase("film")) {
            tempMovie = new Movies();
        }
    }

    // this function catches values in between tags
    // example: <fid></fid> catches the id value in between the start and end tags
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        Pattern pattern = Pattern.compile("[^a-z0-9,:'+.!/]", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        boolean foundSpec;

        if (qName.equalsIgnoreCase("directorfilms")) {
            // Add the directorFilm object to the List of directorFilms
            try {
                directorFilms.add(tempDirFilms);
                // System.out.println("Added another tempDirFilms to directorFilms");
            } catch (Exception e) {
                System.out.println("Error in adding tempDirFilms to list of directorFilms");
            }
        } else if (qName.equalsIgnoreCase("film")) {
            // If all required info from movie is gotten, then add the movie to the arraylist
            try {
                tempDirFilms.addMovie(tempMovie);
            } catch (Exception e) {
                System.out.println("Error in adding tempMovie to movieList inside tempDirectorFilms.");
            }
        }
        else if(qName.equalsIgnoreCase("dirname")) {
            // Set the name of the director
            try {
                matcher = pattern.matcher(tempVal);
                foundSpec = matcher.find();
                if(!foundSpec) {
                    tempDirFilms.setDirectorName(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Director Name (<dirname>): " + tempVal);
                System.out.println("Error in adding tempFilm to tempDirectorFilms - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("fid")) {
            // Set the ID of the movie
            try {
                tempMovie.setId(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("ID (<id>): " + tempVal);
                System.out.println("Error in adding tempFilm to tempDirectorFilms - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("t")) {
            // Set the title of the movie
            try {
                matcher = pattern.matcher(tempVal);
                foundSpec = matcher.find();
                if(!foundSpec) {
                    tempMovie.setTitle(tempVal);
                }
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Title (<t>): " + tempVal);
                System.out.println("Error in adding tempFilm to tempDirectorFilms - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("year")) {
            // Set the year of the movie
            try {
                tempMovie.setYear(Integer.parseInt(tempVal));
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Year (<year>): " + tempVal);
                tempMovie.setYear(null);
                System.out.println("Error in adding tempFilm to tempDirectorFilms - tempVal: " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("cat")) {
            try {
                tempMovie.setGenre(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Genre (<cat>): " + tempVal);
                System.out.println("Error in adding tempFilm to tempDirectorFilms - tempVal: " + tempVal);
            }
        }

    }

    public static void main(String[] args) {
        MoviesParser spe = new MoviesParser();
        spe.runExample();
    }
}


