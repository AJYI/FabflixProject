## CS 122B Project 1 

This project is a movie list web application. This application is made up of three main pages: a movie list page, a single movie page with star list, and a single star page with movie list.

### Demo Video URL


This video is a recording of a desmonstration of our project on Amazon Web Services (AWS). In the video, we clone our git repository to our AWS instance, then build and populate our MySQL database with the the movie-data.sql file. Our web application is then deployed on an AWS instance. We then demonstrate how our web application works by navigating through the site.


### How to deploy the application using Tomcat

1) In the project files, find the folder marked 'target'
2) Find the file marked 'cs122b-spring21-project1.war.' Get the location of the file by right-clicking on it and selecting 'Open in Terminal,' and then running 'pwd' in the terminal.
3) (optional) If needed, place the file somewhere where you can easily navigate to it later.
4) Open up the Tomcat Web Applicatoin manager, by doing the following: get your public IPv4 address from your EC2 instance, and open it along with the 8080/manager/html on your web browser (for example, 34.208.237.5:8080/manager/html)
5) Go to "Select WAR file to upload," and upload the war file from Steps 2 and 3.
6) Deploy the file. The application should appear on your browser.


### Each member's contribution to Project 1

