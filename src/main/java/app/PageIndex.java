package app; 

import java.util.ArrayList;

import app.dto.CommodityLoss;
import app.dto.Country;
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

public class PageIndex implements Handler {

    //DATA-RELATED
    JDBCConnection jdbcConnection = new JDBCConnection();
    //ArrayList<Country> countryNames = jdbcConnection.getAllCountries();
    ArrayList<String> foodGroupNames = getAllFoodGroupNames();
    ArrayList<String> allYears = getAllYears();
    ArrayList<CommodityLoss> maxLoss = new ArrayList<>();

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        
        // Create a simple HTML webpage in a String
        String html = "<html>";
        String yearSelected = context.queryParam("year");
        System.out.println("year ===============> " + yearSelected);
        
        if (yearSelected != null) {
            maxLoss = jdbcConnection.getMaxLossByYear(Integer.parseInt(yearSelected));
        }

        // Add some Header information
        html = html + "<head>" + 
               "<title>Homepage</title>"; //name when hovering on tab

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' type='text/javascipt' href='food.js' />";
        html = html + "</head>";


        //HIBA: Making CSS grid and adding content in it
        html += """
            <body>
            <div>
            <div class="grid-container" id="page-1A">
                <div class="div1">
                    <div class="topnav">
                        <a href='/'>Home</a>
                        <a href='mission.html'>Our Mission</a>
                        <div class="dropdown">
                            <button class="dropbtn">More Data 
                                <i class="fa fa-caret-down"></i>
                            </button>
                            <div class="dropdown-content">
                                <a href='page2A.html'>View by Country</a>
                                <a href='page2B.html'>View by Food Group</a>
                                <a href='page3A.html'>Similar Locations</a>
                                <a href='page3B.html'>Explore More Foods</a>
                            </div>
                        </div>
                    
                </div>
                <div class="div2"> 
                    <div class="main-heading">
                        <h1>FOOD LOSS AND WASTE MONITOR</h1>
                    </div>
                    <div id="text-under-heading">
                        <h3>Choose a year to see the maximum loss percentage for every global commodity.</h3>
                    </div>
                </div>
            
                <div class="div3"> 
                    <div class="subheadings">
                        <h2>ABOUT FOOD LOSS AND WASTE</h2>
                    </div>
                    <div class="body-text"> 
                        <p>The food supply chain is what allows food to be brought to all households. It entails the harvesting, 
                        processing, packaging, transporting, selling and consumption of food from sources such as farms. While it
                        is a crucial process, it still has flaws in terms of its efficiency at transport and its consideration of
                        the environment. It has introduced new problems such as food loss and food wastage.</p>
                        
                        <p>Food loss mainly occurs earlier along the food supply chain--the harvesting stage--and up to, 
                        but not including, the sales stage. Food waste however, occurs at the retail (sales) and consumption stages.</p>
                    </div>
                </div>

                <div class="div4"> 
                        <img src = "food_waste.png" alt = "food" height=300px>
                </div>

                <div class="div5"> 
                    <div class="subheadings">
                        <h2>A QUICK LOOK</h2>
                    </div>
                        """;
        //HIBA
        //dropdown for year
        html += """
                    <div class='form'>
                        <form name="yearForm" id="yearForm" action="/">
                            <label for="year" style="padding-left: 1%;">Choose year:</label>
                            <select name="year" id="year">
        """;

        int[] allYears = new int[56];
        allYears[0] = 1968;
        for (int i = 0; i < allYears.length; i++) {
            allYears[i] = 1968 + i;
        }
        allYears[allYears.length - 1] = 2022;
        
        for (int i = 0; i < allYears.length - 1; i++) {
            html += "<option value='" + allYears[i] + "'>" + allYears[i] + "</option>";
        }

        html += """
                            </select>
                        </form>
                    </div>
        """;

        //table
        html += """
                    <div style="padding-left:25px; padding-right:25px; height: 500px; overflow:auto;">
                        <table id="landing-page-table">
                            <tr>   
                            <th>Commodity</th>
                            <th>Max Loss %</th>
                            </tr>
                    </div>
        """;

        html += """ 
                
        """;

        if (maxLoss.isEmpty()) {
            html += "<tr><td> Unavailable </td><td> Unavailable </td></tr>";
        }
        else if (!maxLoss.isEmpty()) {
            for (CommodityLoss loss : maxLoss) {
                html += "<tr><td>" + loss.getCommodity() + "</td><td>" + loss.getLossPercentage() + "</td></tr>";
            }
        }
        

        html += """
                        </table>
                    </div>
            </div>
            """;

        html += """
            <div class="div6">
                <div class='footer'>
                    <p>COSC2803 Studio Project (Apr24) -- by Hiba Ansari and Areeba Gill</p>
                </div>
            </div>
        </div>
        """;

        // Close Content div

        //Add Javascript
        html = html + """
            <script src="food.js"></script>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);

    }


    /* METHODS */
    //GETTING FOOD GROUP NAMES (COMMODITIES)
    public ArrayList<String> getAllFoodGroupNames() {
        // Create the ArrayList of String objects to return
        ArrayList<String> foodGroups = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DISTINCT descriptor FROM CPC"; //SQL query
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String foodName  = results.getString("descriptor"); //individual data value

                // Add the country object to the array
                foodGroups.add(foodName);
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
        return foodGroups;
    }

    public ArrayList<String> getAllYears() {
        // Create the ArrayList of String objects to return
        ArrayList<String> years = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT year FROM LossWasteOccurrence"; //SQL query
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String year  = results.getString("year"); //individual data value

                // Add the country object to the array
                years.add(year);
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
        return years;
    }
}
