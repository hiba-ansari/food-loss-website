package app;

import java.util.ArrayList;

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
                        <h1>Subtask 3.1</h1>
                    </div>
                    <div id="text-under-heading">
                        <h3>text</h3>
                    </div>
                    <div id="instructions-button">
                        <button>HOW TO USE</button>
                    </div>
                </div>
            
                <div class="div3"> 
                    <div class="mission-center">
                        <h2>div3</h2>
                        <p>text</p>
                    </div>
                </div>

                <div class="div4"> 
                    <div class="subheadings">
                        <h2>div4</h2>
                    </div>
                </div>

                <div class="div5"> 
                    <div class="subheadings">
                        <h2>div5</h2>
                    </div>
                </div>
                
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
