package app;

import java.util.ArrayList;

import app.dto.Commodity;
import app.dto.CommodityLoss;
import app.dto.Country;
import app.dto.LossData;
import app.dto.CommodityData;
import app.dto.CommodityData;
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

public class PageST3B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3B.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        JDBCConnection jdbcConnection = new JDBCConnection();
        String chosenCommodity = context.queryParam("commodity"); //make empty?
        String numWantedGrps = context.queryParam("numSimilar"); //make empty?
        ArrayList<Commodity> allCommodity = jdbcConnection.getAllCommodity();
        ArrayList<CommodityData> selectedCommodityData = new ArrayList<>();
        String similarityFactor = "";
        String commodityName = "";
        String groupName = "";
        String similarityLabel = "";
        double factorValue = 0.0;
        ArrayList<FoodGroupSimilarityData> foodGrpSimilarityData = new ArrayList<>();

        if (context.queryParam("commodity") != null) {
            chosenCommodity = context.queryParam("commodity");
            System.out.println("commodity ===============> " + context.queryParam("commodity"));
        }
        if (context.queryParam("similarityFactor") != null) {
            similarityFactor = context.queryParam("similarityFactor");
            System.out.println("similarity factor ===============> " + context.queryParam("similarityFactor"));

            if (numWantedGrps != null && !numWantedGrps.isEmpty()) {
                selectedCommodityData = jdbcConnection.getCommodityDataBySimilarityFactor(chosenCommodity, similarityFactor);
                if (!selectedCommodityData.isEmpty()) {
                    foodGrpSimilarityData = jdbcConnection.getFoodGroupDataBySimilarityFactor(selectedCommodityData.get(0), similarityFactor, Integer.parseInt(numWantedGrps));
                }
            }
    
        }

        if ("maxLoss".equals(similarityFactor)) {
            similarityLabel = "Max loss";
        }
        else if ("minLoss".equals(similarityFactor)) {
            similarityLabel = "Min loss";
        }
        else if ("ratio".equals(similarityFactor)) {
            similarityLabel = "Loss : Waste ratio";
        }



        if (!selectedCommodityData.isEmpty()) {
            commodityName = selectedCommodityData.get(0).getCommodityName();
            groupName = selectedCommodityData.get(0).getGroupName();
            factorValue = selectedCommodityData.get(0).getSimilarityFactorValue();
        }

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 3.2</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + """
            <body>
            <div>
            <div class="grid-container" id="page-3B">
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
                            <h1>Explore More Foods</h1>
                        </div>
                        <div id="text-under-heading">
                            <h3>Use this page to find new food groups similar to your selection.</h3>
                        </div>
                    </div>

                    <div class="div5"> 
                        <div class="subheadings">
                            <h2>FIND SIMILAR FOOD GROUPS</h2>
            """;
        //HIBA
        //dropdown for year
        html += """
            <form name="commodityFilter" id="commodityFilter" action="/page3B.html">
                <label for="commodity">Choose commodity:</label>
                <select name="commodity" id="commodity" style="width: auto; max-width: 600px;">
        """;

        if (allCommodity.isEmpty()) {
            html += "<tr><td> Unavailable </td><td> Unavailable </td></tr>";
        }
        else if (!allCommodity.isEmpty()) {
            for (Commodity commodity : allCommodity) {
                if (commodity.getCpcCode().equals(chosenCommodity)){
                    html += "<option value='" + commodity.getCpcCode() + "' selected>" + commodity.getName() + "</option>";
                } else {
                    html += "<option value='" + commodity.getCpcCode() + "'>" + commodity.getName() + "</option>";
                }
            }
        }
        
        String isSelected = similarityFactor.equals("ratio") ? "selected" : "";

        html += """
                </select>
                <br>
                <label for="similarityFactor">Similarity:</label>
                <select name="similarityFactor" id="similarityFactor">
                    <option value='ratio' >Loss : Waste ratio</option>
                    <option value='maxLoss'>Maximum loss %</option>
                    <option value='minLoss'>Minimum loss %</option>
                </select>
                <br>
                    <label for="numSimilar">Number of similar groups:</label>
                    <input type="number" id="numSimilar" name="numSimilar" min="1" max="20">
                <br>
                <input type="submit" value="Submit">
            </form>
        """;

        //displaying selected commodity, group and similarity basis (dynamic)

        html += "<br> <p> Selected commodity: " + commodityName + 
        "<br> Selected commodity group: " + groupName + 
        "<br>" + similarityLabel + ": " + factorValue + 
        "</p>";
        
        //table
        html += """
            <div style="padding-left:25px; padding-right:25px; height: 500px; overflow:auto;">
                <table id="landing-page-table">
                    <tr>   
                    <th>Similar Food Group</th>
        """;

        html += "<th>" + similarityLabel + "</th>";

        html += """
                    <th>Similarity Score (%)</th>
                    </tr>
        """;

        if (foodGrpSimilarityData.isEmpty()) {
            html += "<tr><td> Unavailable </td><td> Unavailable </td><td> Unavailable </td></tr>";
        }
        else if (!foodGrpSimilarityData.isEmpty()) {
            for (FoodGroupSimilarityData data : foodGrpSimilarityData) {
                html += "<tr><td>" + data.getGroupName() + "</td>" +
                "<td>" + data.getSimilarityFactorValue() + "</td>" +
                "<td>" + data.getSimilarityScore() + "</td></tr>";
            }
        }
        
        // Close Content div
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
