- # General

  - #### Team#:
        Team 22
  - #### Names:
        Alexander Yi and Elizabeth Wen
  - #### Project 5 Video Demo Link:
        [link]()
  - #### Instruction of deployment:
        1. Clone this repository using `git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-22.git`
        2. Import the project using intellij and choose Maven as the external model
        3. Edit the configurations for the tomcat server. Refer to ([canvas](https://canvas.eee.uci.edu/courses/36596/pages/intellij-idea-tomcat-configuration))
        4. If results dont show up after main page searching, do this command -> "SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));", then reload tomcat.
        5. Also when running the server check the file under "cs122b-spring21-team-22/WebContent/META-INF/context.xml". Within it change the master ip address if using a load balancer.
        6. Run the server.
  - #### Collaborations and Work Distribution:
        50/50 like always.

- # Connection Pooling

  - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.

        Configuration:
            cs122b-spring21-team-22/WebContent/META-INF/context.xml
        Codes:
            cs122b-spring21-team-22/src/AddToMovie.java
            cs122b-spring21-team-22/src/AddToStar.java
            cs122b-spring21-team-22/src/AdvancedSearchResultServlet.java
            cs122b-spring21-team-22/src/AutoCompleteServlet.java
            cs122b-spring21-team-22/src/EmployeeLoginServlet.java
            cs122b-spring21-team-22/src/GenreSearchServlet.java
            cs122b-spring21-team-22/src/HomePageSearchResultServlet.java
            cs122b-spring21-team-22/src/HomePageSearchResultServletAndroid.java
            cs122b-spring21-team-22/src/LoginServlet.java
            cs122b-spring21-team-22/src/MainPageServlet.java
            cs122b-spring21-team-22/src/MetadataServlet.java
            cs122b-spring21-team-22/src/MovieServlet.java
            cs122b-spring21-team-22/src/MovieServletAndroid.java
            cs122b-spring21-team-22/src/PaymentCCServlet.java
            cs122b-spring21-team-22/src/SessionReturn.java
            cs122b-spring21-team-22/src/StarsServlet.java
            cs122b-spring21-team-22/src/TitleSearchServlet.java
            cs122b-spring21-team-22/src/browseGenreListServlet.java

  - #### Explain how Connection Pooling is utilized in the Fabflix code.

  - #### Explain how Connection Pooling works with two backend SQL.

- # Master/Slave

  - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

        Configuration:
            cs122b-spring21-team-22/WebContent/META-INF/context.xml
        Write:
            cs122b-spring21-team-22/src/AddToMovie.java
            cs122b-spring21-team-22/src/AddToStar.java
        Read:
            cs122b-spring21-team-22/src/AdvancedSearchResultServlet.java
            cs122b-spring21-team-22/src/AutoCompleteServlet.java
            cs122b-spring21-team-22/src/EmployeeLoginServlet.java
            cs122b-spring21-team-22/src/GenreSearchServlet.java
            cs122b-spring21-team-22/src/HomePageSearchResultServlet.java
            cs122b-spring21-team-22/src/HomePageSearchResultServletAndroid.java
            cs122b-spring21-team-22/src/LoginServlet.java
            cs122b-spring21-team-22/src/MainPageServlet.java
            cs122b-spring21-team-22/src/MetadataServlet.java
            cs122b-spring21-team-22/src/MovieServlet.java
            cs122b-spring21-team-22/src/MovieServletAndroid.java
            cs122b-spring21-team-22/src/PaymentCCServlet.java
            cs122b-spring21-team-22/src/SessionReturn.java
            cs122b-spring21-team-22/src/StarsServlet.java
            cs122b-spring21-team-22/src/TitleSearchServlet.java
            cs122b-spring21-team-22/src/browseGenreListServlet.java

  - #### How read/write requests were routed to Master/Slave SQL?

- # JMeter TS/TJ Time Logs

  - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**         | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
| --------------------------------------------- | ---------------------------- | -------------------------- | ----------------------------------- | ------------------------- | ------------ |
| Case 1: HTTP/1 thread                         | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                       | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTPS/10 threads                      | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 4: HTTP/10 threads/No connection pooling | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |

| **Scaled Version Test Plan**                  | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
| --------------------------------------------- | ---------------------------- | -------------------------- | ----------------------------------- | ------------------------- | ------------ |
| Case 1: HTTP/1 thread                         | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                       | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTP/10 threads/No connection pooling | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
