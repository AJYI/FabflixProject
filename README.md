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
    Within all the filename/path from above and excluding the context.xml file, these servlets all connected to to a datasource that is defined in the "public void init(ServletConfig config)" function. From there it looks within the context xml file and has connection pooling enabled within it. In other words, whenever the above files calls the init function, it then would utilize connection pooling. After the servlet finishes, these connections can be resued during future requests.
  - #### Explain how Connection Pooling works with two backend SQL.
    Since we have two back end servers(Master and the slave SQL), we would have two pools. When a servlet is called, it would use the pooled connections from the cache and after the servlet ends, it would return that used connection back to the pool for future requests. If more connections are needed, then the pool will grow larger.

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

    Within context xml, we used two resources. Their names were "jdbc/moviedb" and "jdbc/master". Whenever we accessed the servlets that would write, it would be routed to "jdbc/master". If that was the case, then whenever we needed to access the servlet that would read, it would read using the "jdbc/moviedb". Thus master would read/write and slave would only read.

    NOTE: We had to do the command: "RENAME USER 'mytestuser'@'localhost' TO 'mytestuser'@'%';" to be able to access databases remotely.

- # JMeter TS/TJ Time Logs

  - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    The log processes are located in the LogProcessing folder. To run log_processing, go into VSC and into the LogProcessing folder. Within it, open log_processing.py. On line 6 of the code, write in the file that the user wants to read and load it with TS and TJ values. Then run the script and you will get the average TS and TJ values in milli-seconds(ms).


- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**         | **Graph Results Screenshot**                   | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis**                                                                                                                                                                                                                                                                                                                                                                       |
| --------------------------------------------- | ---------------------------------------------- | -------------------------- | ----------------------------------- | ------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Case 1: HTTP/1 thread                         | ![Link](/IMG/HTTP_1_Thread_2.png)                | 185                        | 4.1677                              | 2.8943                    | Here is our test of HTTP with 1 thread. Compared to Case 1 in the following table, there is no significant difference between the two's average times. The time differences are more apparent in the following cases.                                                                                                                                                              |
| Case 2: HTTP/10 threads                       | ![Link](/IMG/HTTP_10_Threads_2.png)              | 326                        | 2.7685                              | 1.8489                    | Here is our test of HTTP with 10 threads. Compared to Case 1 of the HTTP with 1 thread, the average query time for this test was about 2x the amount, which is not surprising given that we are simulating 10 users at the same time. However, we were surprised to see that the TS and TJ are halved; most likely because the throughput was doubled in speed in this Case 2 as compared to Case 1. |
| Case 3: HTTPS/10 threads                      | ![Link](/IMG/HTTPS_10_Threads_2.png)             | 371                        |  4.0728                             | 2.8954                    | Here is our test of HTTPS with 10 threads. Unexpectedly, our average query time was slightly longer than that of Case 2 with the HTTP with 10 threads. What was even more surprising was that the TS and TJ were doubled in length of time, as we assumed that it would be shorter because of the overhead provided by HTTPS.                                                                                                                                      |
| Case 4: HTTP/10 threads/No connection pooling | ![Link](/IMG/HTTP_No_Conn_Pool_10_Threads_2.png) | 370                        | 6.0703                              | 4.5799                    | Here is our test without connection pooling. As compared to Cases 1 and 2, the average query time is slightly slower, as expected. It was unexpected that Case 3's query time was slightly longer, but not too significant of a difference.                                                                                                                                        |

| **Scaled Version Test Plan**                  | **Graph Results Screenshot**                          | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| --------------------------------------------- | ----------------------------------------------------- | -------------------------- | ----------------------------------- | ------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Case 1: HTTP/1 thread                         | ![Link](/IMG/Scaled_HTTP_1_Thread_2.png)                | 197                        | 2.5981                              | 1.9366                    | This is a test of our load balancer with 1 thread. As compared to our Case 1 test without the load balancer in the previous table, there is no significant difference, possibly because this only simulates a single user. The difference in average query time, TS, and TJ are more apparent in the following cases.                                                                                                                                                                                                                                                                                           |
| Case 2: HTTP/10 threads                       | ![Link](/IMG/Scaled_HTTP_10_Threads_2.png)              | 295                        | 3.6398                              | 2.6793                    | This is a test of our load balancer with 10 threads. As compared with our Case 2 test without the load balancer in the previous table, the average query run time is shorter, which demonstrates how well the load balancer is working in dividing up the requests; but at the same time, the TS and TJ of Case 2 are shorter than this case's, which was not expected at all. In comparing Case 3 test without the load balancer, the average query time, TS, and TJ are all somewhat or significant improved, which demonstrates that using a load balancer has a much greater impact on cost than does just simply using HTTPS, which was expected. At the same time, using connection pooling here compared to not using it in Case 4 of the previous table showed a significant difference, with the TS and TJ being doubled in Case 4, which was expected as well.  |
| Case 3: HTTP/10 threads/No connection pooling | ![Link](/IMG/Scaled_No_Conn_Pool_HTTP_10_Threads_2.png) | 310                        | 4.0756                              | 2.932                    |  This is a test of our load balancer with 10 threads and no connection pooling. Compared to Case 2 before this, the TS and TJ are significantly higher, as expected without the benefit of connection pooling. Also, as expected, our TS and TJ were shorter than that of Case 3 from the prior table, which demonstrates the benefit of having a load balancer reducing the running time on average.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
