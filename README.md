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
        4. Run the server.
        5. If results dont show up after main page searching, do this command -> "SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));", then reload tomcat.
  - #### Collaborations and Work Distribution:
        50/50 like always.

- # Connection Pooling

  - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.

  - #### Explain how Connection Pooling is utilized in the Fabflix code.

  - #### Explain how Connection Pooling works with two backend SQL.

- # Master/Slave

  - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

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
