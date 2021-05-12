## CS 122B Project 1

This project is a movie list web application. This application is made up of three main pages: a movie list page, a single movie page with star list, and a single star page with movie list.

### Demo Video URL

This video is a recording of a demonstration of our project on Amazon Web Services (AWS). In the video, we clone our git repository to our AWS instance, then build and populate our MySQL database with the the movie-data.sql file. Our web application is then deployed on an AWS instance. We then demonstrate how our web application works by navigating through the site.
Video URL PROJECT 1: [Video Link](https://drive.google.com/file/d/1s6JTrQert-9AaPNvnPzMKgnN08Rd1UaK/view?usp=sharing)
Video URL PROJECT 2: [Video Link](https://www.youtube.com/watch?v=oDBuBBeYzPM)
Video URL PROJECT 3: [Video Link] ()

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



### Project 3 Queries W/ Prepared Statements
This is the file name and link (from GitHub) of the file containing our queries using PreparedStatement:
- File Name: SuperParser.java
- GitHub Link: 


### Project 3 Parsing Time Optimization Strategies
Our first strategy was to create a parser class that could be used to parse all three XML files. Even though it was longer, it was better for optimization
compared to creating a separate parser class tailored for each specific XML file. The parser class we made helped us in our second optimization strategy, in pulling in and inserting information from and to the database.

Our second strategy was to optimize the runtime of our data insertion into the database. In our database insertion function, we first fetched all data information from the XML files regarding a particular table in our database (e.g., pulling all information regarding actors for the stars table), and stored all information for each table inside a separate hashmap. Before inserting into our database, we checked with the hash table to see if the actor or movie already exists inside. If it exists, then we do not insert; if it does, then we insert using batch. Compared to the naive approach, which to us was creating a parsing class for each file, this was much simpler to understand and implement, and made it easier to keep duplicates
out of the database.


### Inconsistent Data Reporting
All of our inconsistent data was written out to a separate file, found here: 


### Substring matching design:

Within title search: All of the queries that were prepared statement were in the format of "Letter%" because since we're finding movies based on title, the only thing important is the first character.

Within Genre search: There was no search based on substring

Within Advanced Search: All of the queries withing search is "%string%. This is because when users search for movies, they might forget some keywords. For example, if someone searched Batman, and were expecting the results of "The Batman", then there would be not results for that. Same applies for the movie star and movie director.
