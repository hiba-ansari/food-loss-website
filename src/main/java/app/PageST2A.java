package app;

import java.util.ArrayList;

import app.dto.CommoditiesAllDetails;
import app.dto.CommodityLoss;
import app.dto.Country;
import app.dto.LossData;
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
    ArrayList<LossData> allDataWithinRange = new ArrayList<>();

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

        String sortOption = context.queryParam("sortBy");
        System.out.println("sort by ===============> " + context.queryParam("sortBy"));

        String sortDirection = context.queryParam("sortDir");
        System.out.println("order by ===============> " + context.queryParam("sortDir"));

        String allDataOrNot = context.queryParam("showAllData");
        System.out.println("show all data? ===============> " + context.queryParam("showAllData"));

        if (!selectedCountries.isEmpty() && 
                startYear != null && endYear != null && 
                Integer.parseInt(endYear) > Integer.parseInt(startYear) &&
                allDataOrNot == null) {
            selectedData = jdbcConnection.getLossDifferenceForTwoYears(selectedCountries, startYear, endYear, selectedColumns, sortOption, sortDirection);
        }
        if (!selectedCountries.isEmpty() && 
                startYear != null && endYear != null && 
                Integer.parseInt(endYear) > Integer.parseInt(startYear) &&
                allDataOrNot != null) {
            allDataWithinRange = jdbcConnection.getLossForYearRange(selectedCountries, startYear, endYear, selectedColumns, sortOption, sortDirection);
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
            <div>
            <div class="grid-container" id="page-2A">
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
                        <h1>View by Country</h1>
                    </div>
                    <div id="text-under-heading">
                        <h3>Use this page to sort by country and filter as needed.</h3>
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
                <label for="country" style="padding-left: 1%;">Choose 1 or more countries:</label>
                    <select name="country" id="country" style="width: 400px; padding-left: 1%;" multiple>
        """;

        for (Country country : countryNames) {
            if (selectedCountries.contains(country.getM49Code())){
                html += "<option value='" + country.getM49Code() + "' selected>" + country.getName() + "</option>";
            } else {
                html += "<option value='" + country.getM49Code() + "'>" + country.getName() + "</option>";
            }
            //html += "<option value='" + country.getM49Code() + "'>" + country.getName() + "</option>";
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
            if (startYear != null && allYears[i] == Integer.parseInt(startYear)){
                html += "<option value='" + allYears[i] + "' selected>" + allYears[i] + "</option>";
            } else {
                html += "<option value='" + allYears[i] + "'>" + allYears[i] + "</option>";
            }
        }

        //get end year
        html += """
                </select>
                <label for="endYear">Choose end year:</label>
                    <select name="endYear" id="endYear">
        """;
        
        for (int i = 0; i < allYears.length - 1; i++) {
            if (endYear != null && allYears[i] == Integer.parseInt(endYear)) {
                html += "<option value='" + allYears[i] + "'selected>" + allYears[i] + "</option>";
            } 
            else {
                html += "<option value='" + allYears[i] + "'>" + allYears[i] + "</option>";
            }
        }

        //submit button
        html += """
                    </select>
                <b>Show all data:</b>
                <input type="checkbox" id="showAllData" name="showAllData" value="showAllData"
            """;
            if (allDataOrNot != null) {
                    html += "checked>";
                }
                else {
                    html += ">";
                }
        
        html += """
                <br></br>

                <b>Show fields:</b>
                <input type="checkbox" id="commodity" name="showColumns" value="cpcCode" """; 
                
                if (selectedColumns.contains("cpcCode")) {
                    html += "checked>";
                }
                else {
                    html += ">";
                }

                html += """
                <label> Commodity</label>

                <input type="checkbox" id="activity" name="showColumns" value="activity" """;
                
                if (selectedColumns.contains("activity")) {
                    html += "checked>";
                }
                else {
                    html += ">";
                }

                html += """
                <label> Activity</label>

                <input type="checkbox" id="stage" name="showColumns" value="foodSupplyStage" """;
                
                if (selectedColumns.contains("foodSupplyStage")) {
                    html += "checked>";
                }
                else {
                    html += ">";
                }
                
                html += """
                <label> Stage</label>

                <input type="checkbox" id="cause" name="showColumns" value="cause" """;
                
                if (selectedColumns.contains("cause")) {
                    html += "checked>";
                }
                else {
                    html += ">";
                }

                html += """
                <label> Cause</label><br></br>

                <label for="sortBy">Sort by:</label>
                    <select name="sortBy" id="sortBy">
                """;
                    
                if (sortOption != null && sortOption.equals("loss1")) {
                    html += "<option value='loss1' selected>Start year loss</option>";
                }
                else {
                    html += "<option value='loss1'>Start year loss</option>";
                }
                if (sortOption != null && sortOption.equals("loss2")) {
                    html += "<option value='loss2' selected>End year loss</option>";
                }
                else {
                    html += "<option value='loss2'>End year loss</option>";
                }

                html += """
                    </select> 
                    <label for="sortDir">Order by:</label>
                        <select name="sortDir" id="sortDir">
                """;

                if (sortDirection != null && sortDirection.equals("asc")) {
                    html += "<option value='asc' selected>Ascending</option>";
                }
                else {
                    html += "<option value='asc'>Ascending</option>";
                }
                if (sortDirection != null && sortDirection.equals("desc")) {
                    html += "<option value='desc' selected>Descending</option>";
                }
                else {
                    html += "<option value='desc'>Descending</option>";
                }

                html += """
                    </select>

                <br></br>

                <input type="submit" value="Submit">

            </form>
                """;
        
            
        
        // //table
        html += """
            <div style="padding-left:25px; padding-right:25px; height: 500px; overflow:auto;"> """;

        if (allDataOrNot == null) {
            html += getHtmlForDifferenceTable(selectedColumns); 
        }
        else {
            html += getHtmlForYearRangeTable(selectedColumns);
        }
                
        html += "</div></div>";

        html += """
                <div class="div6">
                        <div class='footer' id='page-2A'>
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

    private String getHtmlForDifferenceTable(ArrayList<String> selectedColumns) {
        //table
        String html = "";
        html += """       
            <table id="2A-table">
                <tr>   
                <th>Country</th>
                <th>Start Year</th>
                <th>Loss (%)</th>
                <th>End Year</th>
                <th>Loss (%)</th>
                <th>Difference (%)</th>
        """;

        if (selectedColumns.contains("cpcCode")) {
            html += "<th>Commodity</th>";
        }
        if (selectedColumns.contains("activity")) {
            html += "<th>Activity</th>";
        }
        if (selectedColumns.contains("foodSupplyStage")) {
            html += "<th>Stage</th>";
        }
        if (selectedColumns.contains("cause")) {
            html += "<th>Cause</th>";
        }
        html += "</tr>";   

        for (CommoditiesAllDetails table : selectedData) {
            html += "<tr><td>" + table.getCountry() + "</td>" +
            "<td>" + table.getYear1() + "</td>" + 
            "<td>" + table.getLoss1() + "</td>" + 
            "<td>" + table.getYear2() + "</td>" + 
            "<td>" + table.getLoss2() + "</td>" + 
            "<td>" + table.getDifference() + "</td>";

            //if 'commodity' is selected
            if (selectedColumns.contains("cpcCode")) {
                html += "<td>" + table.getCommodity() + "</td>";
            }
            //if 'activity' is selected
            if (selectedColumns.contains("activity")) {
                html += "<td>" + table.getActivity() + "</td>";
            }
            //if 'stage' is selected
            if (selectedColumns.contains("foodSupplyStage")) {
                html += "<td>" + table.getStage() + "</td>";
            }
            //if 'stage' is selected 
            if (selectedColumns.contains("cause")) {
                html += "<td>" + table.getCause() + "</td>";
            }
            
            html += "</tr>";
        }
        html += "</table>";

        return html;
    }
    private String getHtmlForYearRangeTable(ArrayList<String> selectedColumns) {
        //table
        String html = "";
        html += """       
            <table id="2A-table-year-range">
                <tr>   
                <th>Country</th>
                <th>Year</th>
                <th>Loss (%)</th>
        """;

        if (selectedColumns.contains("cpcCode")) {
            html += "<th>Commodity</th>";
        }
        if (selectedColumns.contains("activity")) {
            html += "<th>Activity</th>";
        }
        if (selectedColumns.contains("foodSupplyStage")) {
            html += "<th>Stage</th>";
        }
        if (selectedColumns.contains("cause")) {
            html += "<th>Cause</th>";
        }
        html += "</tr>";        
                
        html += """ 
                
        """;

        for (LossData table : allDataWithinRange) {
            html += "<tr><td>" + table.getCountry() + "</td>" +
            "<td>" + table.getYear() + "</td>" + 
            "<td>" + table.getLoss() + "</td>";

            //if 'commodity' is selected
            if (selectedColumns.contains("cpcCode")) {
                html += "<td>" + table.getCommodity() + "</td>";
            }
            //if 'activity' is selected
            if (selectedColumns.contains("activity")) {
                html += "<td>" + table.getActivity() + "</td>";
            }
            //if 'stage' is selected
            if (selectedColumns.contains("foodSupplyStage")) {
                html += "<td>" + table.getStage() + "</td>";
            }
            //if 'stage' is selected 
            if (selectedColumns.contains("cause")) {
                html += "<td>" + table.getCause() + "</td>";
            }
            
            html += "</tr>";
        }
        html += "</table>";

        return html;
    }
    }

