package app;

import java.util.ArrayList;

import app.dto.CommodityLoss;
import app.dto.Country;
import app.dto.Persona;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 */

public class PageMission implements Handler {
    JDBCConnection jdbcConnection = new JDBCConnection();
    ArrayList<Persona> personaInfo = getAllPersonas();

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Our Mission</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + """
            <body>
                <div class="grid-container">
                    <div class="div1">
                        <div class='topnav'>
                            <a href='/'>H - Homepage</a>
                            <a href='mission.html'>A - Our Mission</a>
                            <a href='page2A.html'>H - View by Country</a>
                            <a href='page2B.html'>A - View by Food Group</a>
                            <a href='page3A.html'>A - Similar Locations</a>
                            <a href='page3B.html'>H - Explore More Foods</a>
                        </div>

                        <div class="div2"> 
                    <div class="main-heading">
                        <h1>Mission and Vision</h1>
                    </div>
                    
                    <div id="instructions-button">
                        <button>HOW TO USE</button>
                    </div>
                </div>
            
                <div class="divmiss"> 
                    <div class="mission-center">
                        <h2>Our Mission</h2>
                        <p>Did you know that in 2022, the world wasted an estimated 1.05 billion tonnes of food? This roughly accounts for 19% of the total food that was available to consumers that year. Even worse, this figure doesn't include the 13% that got lost in the supply chain (Knowledge For Policy). Seeing these alarming statistics is what prompted us to develop this website to spread awareness about the very pressing issue of food wastage. Our website primarily functions as a means for individuals, interested in preserving food and minimizing wastage, to access reliable and accurate information on the subject. We aim to provide users with comprehensive data on food loss at all levels of the supplychain and inform them of the various ways by which they can help preserve food. Users can filter data according to their needs and requirements, based on various factors like geographical location, food type, time period, etc. </center>
                        </p>
                    </div>
                </div>

                <div class="div4"> 
                    <div class = 'img1-container'>
                        <img src = "food.jpg" alt="FOOD WASTE">
                    </div>
                </div>

                <div class="div5"> 
                    <div class="subheadings">
                        <h2>PERSONA</h2>
                    </div>

""";
                
              

         //table
        html += """
            <div style="padding-left:25px; padding-right:25px; height: 500px; overflow:auto;">
                <table id="mission-page-table">
                    <tr>   
                    <th>PersonaID</th>
                    <th>Images</th>
                    <th>Names</th>
                     <th>Description</th>
                     <th>Needs</th>
                    <th>Goals</th>
                     <th>Skills</th>
                    </tr>
                
        """;

        html += """ 
                
        """;

        for (Persona persona : personaInfo) {
            html += "<tr><td>" + persona.getpersonaID() + "</td><td>" +persona.getImage() +"</td><td>"+ persona.getname() + "</td><td>"+ persona.getdescription() + "</td><td>" + persona.getneeds() + "</td><td>" + persona.getgoals() + "</td><td>" + persona.getskills() + "</td></tr>";
        }

        html += "</table></div></div>";

        html += """
        <div class="div6">
        <div class='footer'>
            <p>COSC2803 Studio Project (Apr24) -- by Hiba Ansari and Areeba Gill</p>
        </div>
    </div>
</div>
""";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

      //GETTING PERSONAS
      public ArrayList<Persona> getAllPersonas() {
        // Create the ArrayList of String objects to return
        ArrayList<Persona> personas = new ArrayList<Persona>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM PERSONA;"; //SQL query
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String ID  = results.getString("personaID"); //individual data value
                String image  = results.getString("image");
                String name  = results.getString("name");
                String description  = results.getString("description");
                String needs  = results.getString("needs");
                String goals  = results.getString("goals");
                String skills  = results.getString("skillsAndExperience");
// Create a Persona Object
                Persona persona = new Persona(ID, image, name, description, needs, goals, skills);
                // Add the country object to the array
                personas.add(persona);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just print the error
            System.err.println(e.getMessage());
            //e.printStackTrace();
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
                //e.printStackTrace();
            }
        }

        // Finally we return all of the countries
        return personas;
    }


}
