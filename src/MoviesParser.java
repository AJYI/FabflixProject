import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
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
            errorWriter = new FileWriter("MoviesInconsistencies.txt");
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
            }

            System.out.println("Number of inconsistencies found in mains243.xml: '" + listOfInconsistencies.size() + "'.");
            System.out.println("All inconsistencies found in mains243.xml: ");

            while (incons.hasNext()) {
                errorWriter.write(incons.next());
                System.out.println(incons.next());
            }

            errorWriter.close();
        } catch (Exception e) {
            System.out.println("Error in writing movie inconsistencies to file");
        }


//
//        while (it.hasNext()) {
//            //System.out.println(it.next().)
//            System.out.println(it.next().toString());
//        }

//        System.out.println("Number of inconsistencies found in mains243.xml: '" + listOfInconsistencies.size() + "'.");
//        System.out.println("All inconsistencies found in mains243.xml: ");
//        while (incons.hasNext()) {
//            System.out.println(incons.next().toString());
//        }
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

    // t his function catches values in between tags
    // example: <fid></fid> catches the id value in between the start and end tags
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        Pattern pattern = Pattern.compile("[^a-z0-9,:'+.!/]-", Pattern.CASE_INSENSITIVE);
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

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        MoviesParser spe = new MoviesParser();
        spe.runExample();
        spe.addStarsToDatabase();
    }

    public void addStarsToDatabase() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            String url = "jdbc:mysql://localhost:3306/moviedb?rewriteBatchedStatements=true";
            String user = "mytestuser";
            String password = "My6$Password";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String listQuery = "select m.title, m.year, m.director from movies m;";
                PreparedStatement statement = conn.prepareStatement(listQuery);
                ResultSet rs = statement.executeQuery();

                HashSet<MovieHashData> hashTable = new HashSet<MovieHashData>();

                while(rs.next()){
                    String title = rs.getString("title");
                    String director = rs.getString("director");
                    Integer year = rs.getInt("year");
                    if(year == 0){
                        year = null;
                    }

                    MovieHashData temp = new MovieHashData();
                    temp.setDirectorName(director);
                    temp.setTitle(title);
                    temp.setYear(year);
                    hashTable.add(temp);
                }

                for (MovieHashData a: hashTable){
                    System.out.println("HashTable Entry " + a.getTitle() + " " + a.getDirectorName() + " " + a.getYear());
                }

                // To get the max starID
                String maxQuery = "select m.id from movies m order by m.id desc limit 1;";
                statement = conn.prepareStatement(maxQuery);
                rs = statement.executeQuery();
                rs.next();
                String currentMax = rs.getString("id");
                int currentId = Integer.parseInt(currentMax.substring(2));

                //Format = tt0499469

                //Using a convertor class
                HashSet<MovieHashData> movieSet = new HashSet<MovieHashData>();
                Iterator<DirectorFilms> it = directorFilms.iterator();
                while(it.hasNext()){
                    DirectorFilms temp = it.next();
                    List<Movies> movieTemp = temp.getMovies();

                    for (int i = 0; i < movieTemp.size(); i++){
                        MovieHashData temp2 = new MovieHashData();
                        temp2.setDirectorName(temp.getDirectorName());
                        temp2.setTitle(movieTemp.get(i).getTitle());
                        temp2.setYear(movieTemp.get(i).getYear());
                        movieSet.add(temp2);
                    }
                }

                conn.setAutoCommit(false);
                // INSERT INTO movies VALUES('tt0351795','Ripoux 3',2003,'Claude Zidi');
                String query1 = "INSERT INTO movies VALUES(?,?,?,?)";
                String query2 = "INSERT INTO genres_in_movies VALUES(1,?)";
                PreparedStatement statement1 = conn.prepareStatement(query1);
                PreparedStatement statement2 = conn.prepareStatement(query2);

                // Gotta process the movie now
                // some for loop in here
                for (MovieHashData a : movieSet){
                    // If the hash table already contains a
                    String id;
                    if (hashTable.contains(a)){
                        // Means we dont have to do anything because it already exists
                        System.out.println("Found a match -------------> " +a.getDirectorName());
                        continue;
                    }
                    // means that actor doesnt exist
                    else{
                        currentId++;
                        id = "tt" + currentId;
                    }
                    System.out.println("Adding movies: " + id + " |DirectorName " +a.getDirectorName() + " |MovieTitle: " + a.getTitle() + " |MovieYear: " + a.getYear());

                    if(a.getYear() == null){
                        continue;
                    }
                    if(a.getTitle() == null){
                        continue;
                    }
                    if(a.getDirectorName() == null){
                        continue;
                    }

                    // For query 2
                    statement2.setString(1, id);
                    // For query 1
                    statement1.setString(1, id);
                    statement1.setString(2, a.getTitle());
                    statement1.setInt(3, a.getYear());
                    statement1.setString(4, a.getDirectorName());

                    // Adding the batches
                    statement1.addBatch();
                    statement2.addBatch();
                }


                System.out.println("Entering into the database");
                System.out.println("Inserting " + movieSet.size() + " entries");
                statement1.executeBatch();
                statement2.executeBatch();
                conn.commit();

                statement.close();
                statement1.close();
                statement2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


