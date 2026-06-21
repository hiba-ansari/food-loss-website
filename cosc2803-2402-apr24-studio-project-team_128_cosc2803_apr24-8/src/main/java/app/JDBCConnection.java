package app;

import java.util.ArrayList;

import app.dto.CommoditiesAllDetails;
import app.dto.CommodityLoss;
import app.dto.Country;
import app.dto.Commodity;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 */

public class JDBCConnection {

    // Name of database file (contained in database folder)
    public static final String DATABASE = "jdbc:sqlite:database/foodloss.db";

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the Countries in the database.
     * @return
     *    Returns an ArrayList of Country objects
     */
    public ArrayList<Country> getAllCountries() {
        // Create the ArrayList of Country objects to return
        ArrayList<Country> countries = new ArrayList<Country>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM Country";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String m49Code     = results.getString("m49code");
                String name  = results.getString("countryName");

                // Create a Country Object
                Country country = new Country(m49Code, name);

                // Add the Country object to the array
                countries.add(country);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return countries;
    }

    //commodity
public ArrayList<Commodity> getAllCommodity() {
        // Create the ArrayList of Country objects to return
         ArrayList<Commodity> commodities = new ArrayList<Commodity>();

//         // Setup the variable for the JDBC connection
       Connection connection = null;

       try {
           // Connect to JDBC data base
          connection = DriverManager.getConnection(DATABASE);

          // Prepare a new SQL Query & Set a timeout
         Statement statement = connection.createStatement();
          statement.setQueryTimeout(30);

//             // The Query
            String query = "SELECT * FROM CPC";
            
//             // Get Result
            ResultSet results = statement.executeQuery(query);

//             // Process all of the results
            while (results.next()) {
//                 // Lookup the columns we need
                 String name  = results.getString("descriptor");
                 String cpc  = results.getString("subclass");

//                 // Create a Country Object
                 Commodity commodity = new Commodity(cpc,name);

//                 // Add the Country object to the array
                 commodities.add(commodity);
             }

//             // Close the statement because we are done with it
             statement.close();
         } catch (SQLException e) {
//             // If there is an error, lets just pring the error
             System.err.println(e.getMessage());
         } finally {
//             // Safety code to cleanup
             try {
                if (connection != null) {
                     connection.close();
               }
            } catch (SQLException e) {
               // connection close failed.
               System.err.println(e.getMessage());
            }
       }

//         // Finally we return all of the countries
        return commodities;
    }


public ArrayList<CommodityLoss> getMaxLossByYear(int userYear) {
    // Create the ArrayList of Country objects to return
    ArrayList<CommodityLoss> maxLoss = new ArrayList<>();

    // Setup the variable for the JDBC connection
    Connection connection = null;

    try {
        // Connect to JDBC data base
        connection = DriverManager.getConnection(DATABASE);

        // Prepare a new SQL Query & Set a timeout
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        // The Query
        String query = """
            select c.descriptor, max(lwo.lossPercent) as maxLoss 
            from LossWasteOccurrence lwo, CPC c
            where 
                lwo.cpcCode = c.subclass
                and lwo.year=""" + 
                userYear + 
                """
                group by c.descriptor
                order by maxLoss desc;
        """;
        System.out.println(query);
        // Get Result
        ResultSet results = statement.executeQuery(query);
        
        // Process all of the results
        while (results.next()) {
            // Lookup the columns we need
            double lossPercent     = results.getDouble("maxLoss");
            String commodity  = results.getString("descriptor");

            // Create a Country Object
            CommodityLoss calculatedLoss = new CommodityLoss(lossPercent, commodity);

            // Add the Country object to the array
            maxLoss.add(calculatedLoss);
        }

        // Close the statement because we are done with it
        statement.close();
    } catch (SQLException e) {
        // If there is an error, lets just pring the error
        System.err.println(e.getMessage());
    } finally {
        // Safety code to cleanup
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    // Finally we return all of the countries
    //System.out.println(maxLoss.size());
    return maxLoss;
}

public ArrayList<CommoditiesAllDetails> getLossFromYearRange(ArrayList<String> countries, 
                                                String startYear, String endYear, 
                                                ArrayList<String> chosenColumns) {
    // Create the ArrayList of Country objects to return
    ArrayList<CommoditiesAllDetails> wantedDetails = new ArrayList<>();
 
    // Setup the variable for the JDBC connection
    Connection connection = null;

    try {
        // Connect to JDBC data base
        connection = DriverManager.getConnection(DATABASE);

        // Prepare a new SQL Query & Set a timeout
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String countriesAsString = String.join(",", countries);

        String[] columnsAsArray = new String[chosenColumns.size()];
        columnsAsArray = chosenColumns.toArray(columnsAsArray);
        for (int i = 0; i < columnsAsArray.length; ++i) {
            columnsAsArray[i] = "tbl1." + columnsAsArray[i];
        }
        String columnsAsString = String.join(",", chosenColumns);
        // The Query
        String query = """
            select  c.countryName, c.m49Code, 
            tbl1.year as year1, 
            tbl1.regionName,
            avg(tbl1.lossPercent) as loss1, 
            tbl2.year as year2, 
            avg(tbl2.lossPercent) as loss2,
            avg(tbl2.lossPercent) - avg(tbl1.lossPercent) as difference,
            tbl1.cpcCode
        """;

        if (columnsAsString.contains("cpcCode")) {
            query += ",comm.descriptor as commodityName";
        }
        if (columnsAsString.contains("activity")) {
            query += ",tbl1.activity";
        }
        if (columnsAsString.contains("foodSupplyStage")) {
            query += ",tbl1.foodSupplyStage as stage";
        }
        if (columnsAsString.contains("cause")) {
            query += ",tbl1.cause";
        }
        if (columnsAsString.isEmpty()) {
            query += ",";
        }

        query += " " + """
            from LossWasteOccurrence as tbl1, LossWasteOccurrence as tbl2, Country c, CPC comm
            where  
                tbl1.m49Code = tbl2.m49Code
                and tbl1.cpcCode = comm.subclass
                and tbl1.m49Code = c.m49Code
                and tbl1.regionName = tbl2.regionName
                and tbl1.cpcCode = tbl2.cpcCode
                and tbl1.activity = tbl2.activity
                and tbl1.foodSupplyStage = tbl2.foodSupplyStage
                and tbl1.cause = tbl2.cause
            """;
        query += "and tbl1.year = " + Integer.parseInt(startYear) + 
        " and tbl1.m49Code in (" + countriesAsString + ") ";
        query += "group by " + String.join(",", columnsAsArray) + ";";

        System.out.println(query);
        // Get Result
        ResultSet results = statement.executeQuery(query);
        
        // Process all of the results
        while (results.next()) {
            // Lookup the columns we need
            String country = results.getString("countryName");
            int m49Code = results.getInt("m49Code");
            String cpcCode = results.getString("cpcCode");
            int year1 = results.getInt("year1");
            double loss1 = results.getDouble("loss1");
            int year2 = results.getInt("year2");
            double loss2 = results.getDouble("loss2");
            String regionName = results.getString("regionName");
            double difference = results.getDouble("difference");
            String commodity = results.getString("commodityName");
            String activity = results.getString("activity");
            String stage = results.getString("stage");
            String cause = results.getString("cause");

            // Create a Country Object
            CommoditiesAllDetails receivedDetails = new CommoditiesAllDetails(country, m49Code, cpcCode, 
                                                    year1, loss1, year2, loss2, regionName, difference, 
                                                    commodity, activity, stage, cause);

            System.out.println(receivedDetails);
            // Add the Country object to the array
            wantedDetails.add(receivedDetails);
        }

        // Close the statement because we are done with it
        statement.close();
    } catch (SQLException e) {
        // If there is an error, lets just pring the error
        System.err.println(e.getMessage());
    } finally {
        // Safety code to cleanup
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    // Finally we return all of the countries
    //System.out.println(maxLoss.size());
    return wantedDetails;
}

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

    public void getYearLossDiff(int year1, int year2, String[] m49Codes){

    }

}
