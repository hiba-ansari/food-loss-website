package app;

import java.util.ArrayList;

import app.dto.CommoditiesAllDetails;
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

public class PageST2A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2A.html";
    JDBCConnection jdbcConnection = new JDBCConnection();
    ArrayList<Country> countryNames = jdbcConnection.getAllCountries();
    // ArrayList<CommodityLoss> maxLoss = new ArrayList<>();
    ArrayList<CommoditiesAllDetails> selectedData = new ArrayList<>();

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String        
        String html = "<html>";

        //for getting user-selected countries
        String selectedCountry;
        ArrayList<String> selectedCountries = new ArrayList<>();
        if (!context.queryParams("country").isEmpty()) {
            if (context.queryParams("country").size() == 1) {
                selectedCountry = context.queryParam("country");
                selectedCountries.add(selectedCountry);
                System.out.println("country ===============> " + context.queryParam("country"));
            }
            else if (context.queryParams("country").size() > 1) {
                selectedCountries = (ArrayList<String>) context.queryParams("country");
                System.out.println("countries ===============> " + context.queryParams("country"));
            }
        }

        String startYear = context.queryParam("startYear");
        System.out.println("start ===============> " + startYear);
        String endYear = context.queryParam("endYear");
        System.out.println("end ===============> " + endYear);

        String selectedColumn;
        ArrayList<String> selectedColumns = new ArrayList<>();
        if (!context.queryParams("showColumns").isEmpty()) {
            if (context.queryParams("showColumns").size() == 1) {
                selectedColumn = context.queryParam("showColumns");
                selectedColumns.add(selectedColumn);
                System.out.println("column ===============> " + context.queryParam("showColumns"));
            }
            else if (context.queryParams("showColumns").size() > 1) {
                selectedColumns = (ArrayList<String>) context.queryParams("showColumns");
                System.out.println("columns ===============> " + context.queryParams("showColumns"));
            }
        }

        if (!selectedCountries.isEmpty() && startYear != null && endYear != null && Integer.parseInt(endYear) > Integer.parseInt(startYear)) {
            selectedData = jdbcConnection.getLossFromYearRange(selectedCountries, startYear, endYear, selectedColumns);
        }

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 2.1</title>";

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
                        <h1>View by Country</h1>
                    </div>
                    <div id="text-under-heading">
                        <h3>Use this page to sort by country.</h3>
                    </div>
                    <div id="instructions-button">
                        <button>HOW TO USE</button>
                    </div>
                </div>
            
                <div class="div3"> 
                    <div class="subheadings">
                        <h2>Text</h2>
                    </div>
                    <div class="body-text"> 
                        <p>Text</p>
                    </div>
                </div>

                <div class="div4"> 
                    <div class = 'img-container'>
                        <img src = "food_waste.png" alt = "food">
                    </div>
                </div>

                <div class="div5"> 
                    <div class="subheadings">
                        <h2>TABLE</h2>
                    </div>
                        """;
        //HIBA
        //get country
        html += """
            <form name="yearRangeForm" id="yearRangeForm" action="/page2A.html">
                <label for="country">Choose a country:</label>
                    <select name="country" id="country" multiple>
        """;

        for (Country country : countryNames) {
            html += "<option value='" + country.getM49Code() + "'>" + country.getName() + "</option>"; //FIXME: ADD CHECKBOXES
        }

        //get start year
        html += """
                    </select>
                <label for="startYear">Choose start year:</label>
                    <select name="startYear" id="startYear">
        """;
        
        int[] allYears = new int[56];
        allYears[0] = 1968;
        for (int i = 0; i < allYears.length; i++) {
            allYears[i] = 1968 + i;
        }
        allYears[allYears.length - 1] = 2022;
        
        for (int i = 0; i < allYears.length - 1; i++) {
            if (allYears[i] != 1968) {
                html += "<option value='" + allYears[i] + "'>" + allYears[i] + "</option>";
            } 
            else {
                html += "<option value='" + allYears[i] + "'selected>" + allYears[i] + "</option>";
            }
        }

        //get end year
        html += """
                </select>
                <label for="endYear">Choose end year:</label>
                    <select name="endYear" id="endYear">
        """;
        
        for (int i = 0; i < allYears.length - 1; i++) {
            if (allYears[i] != 2022) {
                html += "<option value='" + allYears[i] + "'>" + allYears[i] + "</option>";
            } 
            else {
                html += "<option value='" + allYears[i] + "'selected>" + allYears[i] + "</option>";
            }
        }

        //submit button
        html += """
                    </select>
                <br>
                Show fields:
                <input type="checkbox" id="commodity" name="showColumns" value="cpcCode" checked>
                <label> Commodity</label>

                <input type="checkbox" id="activity" name="showColumns" value="activity" checked>
                <label> Activity</label>

                <input type="checkbox" id="stage" name="showColumns" value="foodSupplyStage" checked>
                <label> Stage</label>

                <input type="checkbox" id="cause" name="showColumns" value="cause" checked>
                <label> Cause</label><br>

                <input type="submit" value="Submit">
                <input type="reset" value="Clear All">
            </form>
                """;

        //table
        html += """
            <div style="height:400px;overflow:auto;">
                <table id="landing-page-table">
                    <tr>   
                    <th>Country</th>
                    <th>Start Year</th>
                    <th>Loss</th>
                    <th>End Year</th>
                    <th>Loss</th>
                    <th>End Year</th>
                    <th>Loss</th>
                    <th>Cause</th>
                    <th>Food Supply Stage</th>
                    <th>Difference</th>
                    </tr>
        """;

        html += """ 
                
        """;

        for (CommoditiesAllDetails table : selectedData) {
            html += "<tr><td>" + table.getCountry() + "</td>" +
            "<td>" + table.getYear1() + "</td>" + 
            "<td>" + table.getLoss1() + "</td>" + 
            "<td>" + table.getYear2() + "</td>" + 
            "<td>" + table.getLoss2() + "</td>" + 
            "<td>" + table.getDifference() + "</td>" +
            "<td>" + table.getM49Code() + "</td>" + 
            "<td>" + table.getCpcCode() + "</td>" + 
            "<td>" + table.getRegionName() + "</td>" + 
            "<td>" + table.getCommodity() + "</td>" +
            "<td>" + table.getActivity() + "</td>" +
            "<td>" + table.getStage() + "</td>" +
            "<td>" + table.getCause() + "</td>" +
            "</tr>";
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

}
