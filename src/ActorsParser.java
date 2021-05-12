import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import javax.sql.DataSource;
import java.io.IOException;

public class ActorsParser extends DefaultHandler {

    //List<Actors> actorsList;
    HashSet<Actors> actorsList;
    private Actors tempActor;
    private String tempVal;
    List<String> listOfInconsistencies;
    HashSet<String> dupesActors;

    public ActorsParser() {
        actorsList = new HashSet<Actors>();
        listOfInconsistencies = new ArrayList<String>();
        dupesActors = new HashSet<String>();
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

        System.out.println("Number of dupes found: " + dupesActors.size() + ".");
        System.out.println("Here are the dupe actors:");
        for (String s : dupesActors){
            System.out.println(s);
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
                // When dupes are found
                if(actorsList.contains(tempActor)){
                    dupesActors.add(tempActor.getName());
                }
                // Else the good case
                else{
                    actorsList.add(tempActor);
                }
            } catch (Exception e) {
                System.out.println("Error in adding Actors to actorsList");
            }
        } else if (qName.equalsIgnoreCase("stagename")) {
            // If all required info from movie is gotten, then add the movie to the arraylist
            try {
                tempActor.setName(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Actor Name (<stagename>): " + tempVal);
            }
        } else if (qName.equalsIgnoreCase("dob")) {
            // If all required info from movie is gotten, then add the movie to the arraylist
            try {
                tempActor.setBirthYear(Integer.parseInt(tempVal));
            } catch (Exception e) {
                if (!tempVal.isEmpty()) {
                    listOfInconsistencies.add("Actor Year Of Birth (<dob>): " + tempVal);
                }
            }
        }
    }

//    public void addToDatabase() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        try {
//            String url = "jdbc:mysql://localhost:3306/moviedb";
//            String user = "mytestuser";
//            String password = "My6$Password";
//
//            try (Connection conn = DriverManager.getConnection(url, user, password)) {
//                // We want to do some prepared statement
//                //String executeQuery = "BEGIN;\nuse moviedb;\n";
//
//                // Preparing the query
//                String query = "call addXMLStar(?, ?)";
//
//                conn.setAutoCommit(false);
//                PreparedStatement statement = conn.prepareStatement(query);
//
//                for (int i = 0; i < actorsList.size(); i++) {
//                    // Print Statement
//                    //System.out.println("Adding the entry " + actorsList.get(i).getName() + "," + actorsList.get(i).getBirthYear() + " to the stars database");
//
//                    // Declare our statement
//                    statement.setString(1, actorsList.get(i).getName());
//
//                    if (actorsList.get(i).getBirthYear() == null) {
//                        statement.setNull(2, Types.INTEGER);
//                    } else {
//                        statement.setInt(2, actorsList.get(i).getBirthYear());
//                    }
//                    statement.addBatch();
//                }
//                statement.executeBatch();
//                conn.commit();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ActorsParser spe = new ActorsParser();
        spe.runExample();
        //spe.addToDatabase();
    }
}
