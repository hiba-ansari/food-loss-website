package app;

import java.util.ArrayList;

import app.dto.CommoditiesAllDetails;
import app.dto.commDetails;
import app.dto.CommodityLoss;
import app.dto.Country;
import app.dto.lossData2;
import app.dto.Commodity;
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

public class PageST2B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2B.html";
    JDBCConnection jdbcConnection = new JDBCConnection();
    ArrayList<Country> countryNames = jdbcConnection.getAllCountries();
    ArrayList<CommodityLoss> maxLoss = new ArrayList<>();
    ArrayList<CommoditiesAllDetails> selectedData = new ArrayList();
    ArrayList<Commodity> foodNames=jdbcConnection.getAllCommodity();
    ArrayList<lossData2> allDataWithinRange = new ArrayList<>();

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";
        String selectedFoodGroup;
        ArrayList<String> selectedFoodGroups = new ArrayList<>();
        if (!context.queryParams("foodgrp").isEmpty()) {
            if (context.queryParams("foodgrp").size() == 1) {
                selectedFoodGroup = context.queryParam("foodgrp");
                selectedFoodGroups.add(selectedFoodGroup);
                System.out.println("foodgrp ===============> " + context.queryParam("foodgrp"));
            }
            else if (context.queryParams("foodgrp").size() > 1) {
                selectedFoodGroups = (ArrayList<String>) context.queryParams("foodgrp");
                System.out.println("foodgrps ===============> " + context.queryParams("foodgrp"));
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


        
        if (!selectedFoodGroups.isEmpty() && 
        startYear != null && endYear != null && 
        Integer.parseInt(endYear) > Integer.parseInt(startYear) &&
        allDataOrNot == null) {
    selectedData = jdbcConnection.getLossDifferenceForTwoYears(selectedFoodGroups, startYear, endYear, selectedColumns, sortOption, sortDirection);
}
if (!selectedFoodGroups.isEmpty() && 
        startYear != null && endYear != null && 
        Integer.parseInt(endYear) > Integer.parseInt(startYear) &&
        allDataOrNot != null) {
    allDataWithinRange = jdbcConnection.getLossForYearRange2(selectedFoodGroups, startYear, endYear, selectedColumns, sortOption, sortDirection);
}


        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 2.2</title>";

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
                        <h1>View by Food Group</h1>
                    </div>
                    <div id="text-under-heading">
                        <h3>Use this page to sort by Food Group.</h3>
                    </div>
                    <div id="instructions-button">
                        <button>HOW TO USE</button>
                    </div>
                </div>
            
                

                <div class="div5"> 
                    <div class="subheadings">
                        <h2>TABLE</h2>
                    </div>

               
        """;

         //get foodgroup
         html += """
            <form name="yearRangeForm" id="yearRangeForm" action="/page2B.html">
                <label for="foodgrp">Choose a Food Group: <br> </label>
                    <select name="foodgrp" id="foodgrp" multiple>
        """;

        for (Commodity commodity : foodNames  ) {
            html += "<option value='" + commodity.getCpcCode() + "'>" + commodity.getName() + "</option>"; //FIXME: ADD CHECKBOXES
        }

        //get start year
        html += """
                    </select>
                <label for="startYear"> <br> Choose start year:</label>
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
                <label for="endYear">  Choose end year:</label>
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
                <input type="checkbox" id="country" name="showColumns" value="M49Code"  """; 
                 if (selectedColumns.contains("M49Code")) {
                    html += "checked>";
                }
                else {
                    html += ">";
                }

                html += """
                <label> Country</label>

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
                <label> Cause</label><br>

                <label for="sortBy">Sort by:</label>
                    <select name="sortBy" id="sortBy">
                        <option value='loss1'>Start year loss</option>
                        <option value='loss2'>End year loss</option>
                    </select>

                <label for="sortDir">Order by:</label>
                    <select name="sortDir" id="sortDir">
                        <option value='asc'>Ascending</option>
                        <option value='desc'>Descending</option>
                    </select>

                <br></br>

                <input type="submit" value="Submit">
                <input type="reset" value="Clear All">
            </form>
                """;

        //table
        html += """
            <div style="display:grid; padding-left:25px; padding-right:25px; height:auto; width:auto; 
            overflow:auto;"> """;

        if (allDataOrNot == null) {
            html += getHtmlForDifferenceTable(selectedColumns); 
        }
        else {
            html += getHtmlForYearRangeTable(selectedColumns);
        }
                
        html += "</div></div>";

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

    private String getHtmlForDifferenceTable(ArrayList<String> selectedColumns) {
        //table
        String html = "";
        html += """       
            <table id="2A-table">
                <tr>   
                <th>Commodity</th>
                <th>Start Year</th>
                <th>Loss (%)</th>
                <th>End Year</th>
                <th>Loss (%)</th>
                <th>Difference (%)</th>
        """;

        if (selectedColumns.contains("M49Code")) {
            html += "<th>Country</th>";
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

        for (CommoditiesAllDetails table : selectedData) {
            html += "<tr><td>" + table.getCommodity() + "</td>" +
            "<td>" + table.getYear1() + "</td>" + 
            "<td>" + table.getLoss1() + "</td>" + 
            "<td>" + table.getYear2() + "</td>" + 
            "<td>" + table.getLoss2() + "</td>" + 
            "<td>" + table.getDifference() + "</td>";

            //if 'commodity' is selected
            if (selectedColumns.contains("cpcCode")) {
                html += "<td>" + table.getCountry() + "</td>";
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
            <table id="2B-table-year-range">
                <tr>   
                <th>Commodity</th>
                <th>Year</th>
                <th>Loss (%)</th>
        """;

        if (selectedColumns.contains("M49Code")) {
            html += "<th>Country</th>";
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

        for (lossData2 table : allDataWithinRange) {
            html += "<tr><td>" + table.getCommodity() + "</td>" +
            "<td>" + table.getYear() + "</td>" + 
            "<td>" + table.getLoss() + "</td>";

            //if 'country' is selected
            if (selectedColumns.contains("M49Code")) {
                html += "<td>" + table.getCountry() + "</td>";
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




      