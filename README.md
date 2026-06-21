# Food Loss Studio Project (Semester 1 - April 2024)
This program provides:

* A Java class for the Index page (index.html).
* JDBCConnection Java class, that uses the Food Loss Database. This class contains one method to return all Countries contained in the Database.
* CSS (```common.css```) file in the resources directory.
* Database:
    * ```foodloss.db``` - contains a database based on a subset of the foodloss ER Model.

Classes backing Web pages:
```bash
├── PageIndex.java                    - Homepage page for Level 1 Sub-task A
├── PageMission.java                  - Mission Statement page for Level 1 Sub-task B
├── PageST2/3.java                - Sets of 4 Java files backing the 4 pages for 4 Level2/3 sub-tasks. Student in group of 3 will need to add additional Java files
```

Other Classes:
```bash
├── java/app                                - Package location for all Java files for the webserver
│         ├── App.java                      - Main Application entrypoint for Javalin
│         └── JDBCConnection.java           - Example JDBC Connection class based on Studio Project Workshop content
├── java/helper                             - Location of the helper file for loading SQLite with JDBC
│         └── FoodProcessCSV.java            - Helper Java program to load SQLite database from the provided CSVs
```

Folders:
```bash
├── /src/main                    - Location of all files as required by build configuration
│         ├── java               - Java Source location
│         │    ├── app           - Package location for all Java files for the webserver
│         │    └── helper        - Location of the helper file for loading SQLite with JDBC
│         └── resources          - Web resources (html templates / style sheets)
│               ├── css          - CSS Style-sheets. Base example style sheet (common.css) provided
│               └── images       - Image files. Base example image (RMIT Logo) provided
│ 
├── /target                      - build directory (DO NOT MODIFY)
├── /database                    - The folder to store sqlite database files (*.db files) and data files (*.csv) related to the database
├── pom.xml                      - Configure Build (DO NOT MODIFY)
└── README.md                    - This file ;)
```

Current Libraries:
* org.xerial.sqlite-jdbc (SQLite JDBC library)
* javalin (lightweight Java Webserver)
* thymeleaf (HTML template) - https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html

Libraries required as dependencies:
* By javalin
   * slf4j-simple (lightweight logging)
* By xerial/jdbc
   * sqlite-jdbc

# Building & Running the code
The code can be run from the main web server program

## Running the Main web server
1. Open this project within VSCode
2. Allow VSCode to read the pom.xml file
 - Allow the popups to run and "say yes" to VSCode configuring the build
 - Allow VSCode to download the required Java libraries
3. To Build & Run
 - Open the ``src/main/java/app/App.java`` source file, and select "Run" from the pop-up above the main function
4. Go to: http://localhost:7000
