## CS 122B Project 1

This project is a movie list web application. This application is made up of three main pages: a movie list page, a single movie page with star list, and a single star page with movie list.

### Demo Video URL

This video is a recording of a demonstration of our project on Amazon Web Services (AWS). In the video, we clone our git repository to our AWS instance, then build and populate our MySQL database with the the movie-data.sql file. Our web application is then deployed on an AWS instance. We then demonstrate how our web application works by navigating through the site.
Video URL PROJECT 1: [Video Link](https://drive.google.com/file/d/1s6JTrQert-9AaPNvnPzMKgnN08Rd1UaK/view?usp=sharing)
Video URL PROJECT 2: [Video Link](https://www.youtube.com/watch?v=oDBuBBeYzPM)
Video URL PROJECT 3: [Video Link](https://www.youtube.com/watch?v=2kSnEB26E5g)
Video URL PROJECT 4: [Video Link](https://www.youtube.com/watch?v=tmT0AC4gMrE)

### How to deploy the application using Tomcat

1. Clone this repository using `git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22.git`
2. Import the project using intellij and choose Maven as the external model
3. Edit the configurations for the tomcat server. Refer to ([canvas](https://canvas.eee.uci.edu/courses/36596/pages/intellij-idea-tomcat-configuration))
4. Run the server.

### Each member's contribution to Project 1

Due to finding out that we were supposed to git push our progress right after we finished the project, we got an old copy of our workspace and pushed our new workspace on it. While doing that, we tried to mimic how we incrementally worked on this project. Hopefully this doesn't become an issue. On the next project, we will make sure we git push our progress. In terms of contribution, the project was well balanced and fair for both team members.

### Each member's contribution to Project 2

Although this project has not been completed even after using the 24 hr grace period, our group contribution was still 50-50. Currently the parts that are NOT WORKING is pagination with N results, and adding into shopping cart (However, most of the shpping cart is implemented).

### Each member's contribution to Project 3

For contribution, both members contributed 50-50, with tasks being worked on together.

### Each member's contribution to Project 4

For contribution, both members contributed 50-50, since we had problems with project 3 submission, we couldnt upload to github. However, we worked on the project together on zoom call.

### List of Files Containing Queries W/ Prepared Statements

This is a list of all of the files containing queries using PreparedStatement, and links (from GitHub) for each file:

- SuperParser.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/SuperParser.java
- CastParser.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/CastParser.java
- MoviesParser.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/MoviesParser.java
- AddToStar.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/AddToStar.java
- AdvancedSearchResultServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/AdvancedSearchResultServlet.java
- browseGenreListServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/browseGenreListServlet.java
- EmployeeLoginServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/EmployeeLoginServlet.java
- GenreSearchServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/GenreSearchServlet.java
- LoginServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/LoginServlet.java
- MainPageServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/MainPageServlet.java
- MovieServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/MovieServlet.java
- PaymentCCServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/PaymentCCServlet.java
- StarsServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/StarsServlet.java
- TitleSearchServlet.java, https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22/blob/main/src/TitleSearchServlet.java
-

### Project 3 Parsing Time Optimization Strategies

Our first strategy was to create a parser class that could be used to parse all three XML files. Even though it was longer, it was better for optimization
compared to creating a separate parser class tailored for each specific XML file. The parser class we made helped us in our second optimization strategy, in pulling in and inserting information from and to the database.

Our second strategy was to optimize the runtime of our data insertion into the database. In our database insertion function, we first fetched all data information from the XML files regarding a particular table in our database (e.g., pulling all information regarding actors for the stars table), and stored all information for each table inside a separate hashmap. Before inserting into our database, we checked with the hash table to see if the actor or movie already exists inside. If it exists, then we do not insert; if it does, then we insert using batch. Compared to the naive approach, which to us was creating a parsing class for each file, this was much simpler to understand and implement, and made it easier to keep duplicates
out of the database.

### Inconsistent Data Reporting

All of our inconsistent data was written out to three separate files, found here: Inconsistencies.txt, CastInconsistencies.txt, and MoviesInconsistencies.txt

### Location of SQL Files

All SQL files can be found in the folder SQL-Files in the GitHub repository.

### Substring matching design:

Within title search: All of the queries that were prepared statement were in the format of "Letter%" because since we're finding movies based on title, the only thing important is the first character.

Within Genre search: There was no search based on substring

Within Advanced Search: All of the queries withing search is "%string%. This is because when users search for movies, they might forget some keywords. For example, if someone searched Batman, and were expecting the results of "The Batman", then there would be not results for that. Same applies for the movie star and movie director.

### Project 4 conditions:

For project 4, we decided to make a java file to make and create a full text search table. To run it, use the command: mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="FullTextSearch".
To run the android file, we zipped it up withing .\cs122b-spring21-team-22\Android. So to run it, you must unzip it and have the server running in the background (LOCAL SERVER).
