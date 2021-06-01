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

import java.io.FileWriter;

/*
Not a servlet
 */

public class SuperParser extends DefaultHandler {

    //List<Actors> actorsList;
    HashSet<Actors> actorsList;
    private Actors tempActor;
    private String tempVal;
    List<String> listOfInconsistencies;
    HashSet<String> dupesActors;
    FileWriter fw;

    public SuperParser() {
        actorsList = new HashSet<Actors>();
        listOfInconsistencies = new ArrayList<String>();
        dupesActors = new HashSet<String>();
    }

    private void parseDocument(String file){
        // get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            // Set up filewriter
            fw = new FileWriter("Inconsistencies.txt");
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse(file, this);

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

        try {
            while (it.hasNext()) {
                Actors temp = it.next();
                System.out.println("NAME: " + temp.getName() + " |BIRTHYEAR: " + temp.getBirthYear());
            }

            System.out.println("Number of inconsistencies found: '" + listOfInconsistencies.size() + "'.");
            System.out.println("All inconsistencies found: ");

            fw.write("List of Inconsistencies: ");
            while (incons.hasNext()) {
                fw.write(incons.next().toString());
                System.out.println(incons.next().toString());
            }

            // fw.write("\nList of duplicates: ");

            System.out.println("Number of dupes found: " + dupesActors.size() + ".");
            System.out.println("Here are the dupe actors:");
            for (String s : dupesActors){
                // fw.write(s);
                System.out.println(s);
            }

            fw.close();
        } catch (IOException e) {
            System.out.println("Error in writing to file");
        }


    }

    public void runExample(String file) {
        parseDocument(file);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor") || qName.equalsIgnoreCase("filmc")) {
            tempActor = new Actors();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor") || qName.equalsIgnoreCase("filmc")) {
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
        else if (qName.equalsIgnoreCase("a")) {
            try {
                tempActor.setName(tempVal);
            } catch (Exception e) {
                listOfInconsistencies.add("Movie Actor (<a>): " + tempVal);
            }
        }
    }

    public void addStarsToDatabase() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            String url = "jdbc:mysql://localhost:3306/moviedb?rewriteBatchedStatements=true";
            String user = "mytestuser";
            String password = "My6$Password";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                // We want to do some prepared statement
                //String executeQuery = "BEGIN;\nuse moviedb;\n";
                String listQuery = "select s.id, s.name, s.birthYear from stars s;";
                PreparedStatement statement = conn.prepareStatement(listQuery);
                ResultSet rs = statement.executeQuery();

                HashSet<Actors> hashTable = new HashSet<Actors>();

                while(rs.next()){
                    String starId = rs.getString("id");
                    String starName = rs.getString("name");
                    Integer starBirthYear = rs.getInt("birthYear");
                    if(starBirthYear == 0){
                        starBirthYear = null;
                    }

                    Actors temp = new Actors();
                    temp.setName(starName);
                    temp.setBirthYear(starBirthYear);
                    hashTable.add(temp);
                }

                for (Actors a: hashTable){
                    System.out.println("HashTable Entry " + a.getName() + " " + a.getBirthYear());
                }

                // To get the max starID
                String maxQuery = "select s.id from stars s order by s.id desc limit 1";
                statement = conn.prepareStatement(maxQuery);
                rs = statement.executeQuery();
                rs.next();
                String currentMax = rs.getString("id");
                int currentId = Integer.parseInt(currentMax.substring(2));



                conn.setAutoCommit(false);
//                String query = "INSERT INTO STARS(?, ?, ?) VALUES(?, ?, ?)";
//                statement = conn.prepareStatement(query);

                String query = "INSERT INTO STARS(id, name) VALUES(?, ?)";
                String query2 = "INSERT INTO STARS(id, name, birthYear) VALUES(?, ?, ?)";
                statement = conn.prepareStatement(query);
                PreparedStatement statement1 = conn.prepareStatement(query2);

                // some for loop in here
                for (Actors a : actorsList){
                    // If the hash table already contains a
                    String id;
                    if (hashTable.contains(a)){
                        // Means we dont have to do anything because it already exists
                        System.out.println("Found a match -------------> " +a.getName());
                        continue;
                    }
                    // means that actor doesnt exist
                    else{
                        currentId++;
                        id = "nm" + currentId;
                    }
                    System.out.println("Adding stars: " + id + " and " +a.getName());

                    if (a.getBirthYear() == null){
                        //INSERT INTO stars (id, name) VALUES('nm0247038','Christine Eads');
                        statement.setString(1, id);
                        statement.setString(2, a.getName());
                        statement.addBatch();
                    }
                    else{
//                        //INSERT INTO stars (id, name, birthYear) VALUES('nm0246984','Halfdan E',1965);
//                        String query = "INSERT INTO STARS(id, name, birthYear) VALUES(?, ?, ?)";
                        statement1.setString(1, id);
                        statement1.setString(2, a.getName());
                        statement1.setInt(3, a.getBirthYear());
                        statement1.addBatch();
                    }
                }

                System.out.println("Entering into the database");
                System.out.println("Inserting " + actorsList.size() + " entries");
                statement.executeBatch();
                statement1.executeBatch();
                conn.commit();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        SuperParser spe1 = new SuperParser();
        spe1.runExample("actors63.xml");
        spe1.runExample("casts124.xml");
        spe1.printData();
        spe1.addStarsToDatabase();

        MoviesParser spe2 = new MoviesParser();
        spe2.runExample();
        spe2.addStarsToDatabase();

        CastParser castParser = new CastParser();
        castParser.runExample();
        castParser.addStarsToDatabase();
    }
}
