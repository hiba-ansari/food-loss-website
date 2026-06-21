package app;

import java.util.ArrayList;

import app.dto.Commodity;
import app.dto.CommodityData;
import app.dto.CountryData;
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

public class PageST3A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3A.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";


JDBCConnection jdbcConnection = new JDBCConnection();
        String chosenCountry = context.queryParam("country"); //make empty?
        String numWantedGrps = context.queryParam("numSimilar"); //make empty?
        ArrayList<Country> allCountry = jdbcConnection.getAllCountries();
        ArrayList<CountryData> selectedCountryData = new ArrayList<>();
        String similarityFactor = "";
        String countryName = "";
        String groupName = "";
        String similarityLabel = "";
        double factorValue = 0.0;
        ArrayList<countrySimilarityData> CountrySimilarityData = new ArrayList<>();

        if (context.queryParam("country") != null) {
            chosenCountry = context.queryParam("country");
            System.out.println("country ===============> " + context.queryParam("country"));
        }
        if (context.queryParam("similarityFactor") != null) {
            similarityFactor = context.queryParam("similarityFactor");
            System.out.println("similarity factor ===============> " + context.queryParam("similarityFactor"));

            if (numWantedGrps != null && !numWantedGrps.isEmpty()) {
                selectedCountryData = jdbcConnection.getCountryDataBySimilarityFactor(chosenCountry, similarityFactor);
                if (!selectedCountryData.isEmpty()) {
                    CountrySimilarityData = jdbcConnection.getRegionDataBySimilarityFactor(selectedCountryData.get(0), similarityFactor, Integer.parseInt(numWantedGrps));
                }
            }
        }

        if ("both".equals(similarityFactor)) {
            similarityLabel = "Both";
        }
        else if ("percentage".equals(similarityFactor)) {
            similarityLabel = "Percentage";
        }
        else if ("products".equals(similarityFactor)) {
            similarityLabel = "products";
        }

        if (!selectedCountryData.isEmpty()) {
            countryName = selectedCountryData.get(0).getCountryName();
            groupName = selectedCountryData.get(0).getGroupName();
            factorValue = selectedCountryData.get(0).getSimilarityFactorValue();
        }

        // Add some Head information
        html = html + "<head>" + 
               "<title>Subtask 3.1</title>";

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
                        <h1>Similar Locations</h1>
                    </div>
                    <div id="text-under-heading">
                        <h3>Use this page to find locations similar to your selection.</h3>
                    </div>
                    <div id="instructions-button">
                        <button>HOW TO USE</button>
                    </div>
                </div>
            
               

                

                <div class="div5"> 
                    <div class="subheadings">
                        <h2>TABLE</h2>
                    </div>
                </div>
                
               
        """;

//dropdown for year
 
 html += """
    <div class='form'>
        <form name="yearForm" id="yearForm" action="/">
            <label for="year">Choose year:</label>
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

html += """
    <form name="countryFilter" id="countryFilter" action="/page3A.html">
        <label for="country">Choose country:</label>
        <select name="country" id="country">
""";
for (Country country : allCountry) {
    html += "<option value='" + country.getM49Code() + "'>" + country.getName() + "</option>";
}

html += """
        </select>
        <br>
        <label for="similarityFactor">Similarity:</label>
        <select name="similarityFactor" id="similarityFactor">
            <option value='products'>Food products</option>
            <option value='percentage'>Food loss/waste %</option>
            <option value='both'>Both</option>
        </select>
        <br>
            <label for="numSimilar">Number of similar countries:</label>
            <input type="number" id="numSimilar" name="numSimilar" min="1" max="20">
        <br>
        <input type="submit" value="Submit">
    </form>
""";

 //displaying selected country, group and similarity basis (dynamic)

 html += "<br> <p> Selected country: " + countryName + 
 "<br> Selected countries: " + groupName + 
 "<br>" + similarityLabel + ": " + factorValue + 
 "</p>";

  //table
  html += """
    <div style="height:400px;overflow:auto;">
        <table id="landing-page-table">
            <tr>   
            <th>Similar Countries</th>
""";
html += "<th>" + similarityLabel + "</th>";

html += """
            <th>Similarity Score (%)</th>
            </tr>
""";
for (countrySimilarityData data : CountrySimilarityData) {
    html += "<tr><td>" + data.getGroupName() + "</td>" +
    "<td>" + data.getSimilarityFactorValue() + "</td>" +
    "<td>" + data.getSimilarityScore() + "</td></tr>";
}

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
