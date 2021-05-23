import java.sql.*;
import java.util.HashSet;

public class FullTextSearch{
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/moviedb?rewriteBatchedStatements=true";
        String user = "mytestuser";
        String password = "My6$Password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Dropping ft table if it exists");
            String dropQuery = "DROP TABLE IF EXISTS ft";
            PreparedStatement dropStatement = conn.prepareStatement(dropQuery);
            dropStatement.execute();

            System.out.println("Creating ft table");
            String createTable = "CREATE TABLE ft(\n" +
                    "entryID INT AUTO_INCREMENT,\n" +
                    "movieID varchar(10),\n" +
                    "movieTitle Text,\n" +
                    "movieYear integer,\n" +
                    "movieDirector varchar(100),\n" +
                    "genres varchar(100),\n" +
                    "actors varchar(100),\n" +
                    "starId varchar(100),\n" +
                    "ratings float,\n" +
                    "PRIMARY KEY(entryID),\n" +
                    "FULLTEXT(movieTitle)\n" +
                    ");";
            PreparedStatement createTableStatement = conn.prepareStatement(createTable);
            createTableStatement.execute();

            String movieQuery = "SELECT \n" +
                    "    m.id AS 'movieID',\n" +
                    "    m.title AS 'movieTitle',\n" +
                    "    m.year AS 'movieYear',\n" +
                    "    m.director AS 'movieDirector',\n" +
                    "    SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT g.name), ',', 3) AS 'genres',\n" +
                    "    s.actors 'actors',\n" +
                    "    s.starId 'starId',\n" +
                    "    r.rating AS 'ratings'\n" +
                    "FROM\n" +
                    "    genres g,\n" +
                    "    genres_in_movies gim,\n" +
                    "    (select m.id 'movieId', \n" +
                    "SUBSTRING_INDEX(GROUP_CONCAT(combinedStars.name),',',3) 'actors',\n" +
                    "SUBSTRING_INDEX(GROUP_CONCAT(combinedStars.id),',',3) 'starId'\n" +
                    "from movies m, \n" +
                    "(SELECT \n" +
                    "    s.id 'id',\n" +
                    "    s.name 'name',\n" +
                    "    s.birthYear 'birthYear',\n" +
                    "    sim.movieId 'movieId'\n" +
                    "FROM\n" +
                    "    stars_in_movies sim,\n" +
                    "    (SELECT \n" +
                    "    s.id 'id',\n" +
                    "    s.name 'name',\n" +
                    "    s.birthYear 'birthYear'\n" +
                    "FROM\n" +
                    "    stars_in_movies sim,\n" +
                    "    stars s\n" +
                    "WHERE\n" +
                    "    sim.starId = s.id\n" +
                    "GROUP BY s.id, s.name, birthYear\n" +
                    "ORDER BY COUNT(s.id) DESC) s\n" +
                    "WHERE\n" +
                    "    sim.starId = s.id\n" +
                    "GROUP BY s.id, sim.movieId) as combinedStars\n" +
                    "where m.id = combinedStars.movieId\n" +
                    "group by m.id) s,\n" +
                    "    movies m\n" +
                    "        LEFT JOIN\n" +
                    "    ratings r ON m.id = r.movieId\n" +
                    "WHERE\n" +
                    "    m.id = gim.movieId\n" +
                    "        AND gim.genreId = g.id\n" +
                    "        AND m.id = s.movieId\n" +
                    "GROUP BY m.id\n" +
                    "order by m.title asc";

            PreparedStatement statement1 = conn.prepareStatement(movieQuery);
            ResultSet rs = statement1.executeQuery();

            // Initialize a HashSet
            HashSet<FullTextSearchMovies> hashTable = new HashSet<FullTextSearchMovies>();

            System.out.println("Hashing the movies table");
            // Inserting into the hashTable
            while(rs.next()){
                String movieID = rs.getString("movieID");
                String movieTitle = rs.getString("movieTitle");
                int movieYear = rs.getInt("movieYear");
                String movieDirector = rs.getString("movieDirector");
                String genres = rs.getString("genres");
                String actors = rs.getString("actors");
                String starId = rs.getString("starId");
                Float ratings = rs.getFloat("ratings");

                FullTextSearchMovies temp = new FullTextSearchMovies(movieID, movieTitle, movieYear, movieDirector, genres, actors, starId, ratings);
                if(hashTable.contains(temp)){
                    System.out.println("Something happened here");
                    System.out.println(temp.getId());
                }
                hashTable.add(temp);
            }

            // For the unique entryID
            conn.setAutoCommit(false);
            String query = "INSERT INTO ft(entryID, movieID, movieTitle, movieYear, movieDirector, genres, actors, starId, ratings) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement2 = conn.prepareStatement(query);

            System.out.println("Inserting into the ft table, this may take a bit");
            // Adding into the ft database
            int n = 1;
            for(FullTextSearchMovies m: hashTable){
                statement2.setInt(1, n);
                statement2.setString(2, m.getId());
                statement2.setString(3, m.getTitle());
                statement2.setInt(4, m.getYear());
                statement2.setString(5, m.getDirectors());
                statement2.setString(6, m.getGenres());
                statement2.setString(7, m.getActors());
                statement2.setString(8, m.getStarIds());

                if(m.getRatings() == 0){
                    statement2.setNull(9, Types.NULL);
                }
                else{
                    statement2.setFloat(9, m.getRatings());
                }

                statement2.addBatch();
                n++;
            }

            statement2.executeBatch();
            conn.commit();

            dropStatement.close();
            createTableStatement.close();
            statement1.close();
            statement2.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
