package app;

import java.util.ArrayList;

import app.dto.CommoditiesAllDetails;
import app.dto.CommodityLoss;
import app.dto.Country;
import app.dto.LossData;
import app.dto.lossData2;
import app.dto.CommodityData;
import app.dto.CommodityData;
import app.dto.CountryData;
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
     * 
     * @return
     *         Returns an ArrayList of Country objects
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
            String query = "SELECT * FROM Country ORDER BY countryName";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String m49Code = results.getString("m49code");
                String name = results.getString("countryName");

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
            String query = "SELECT DISTINCT descriptor FROM CPC"; // SQL query

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String foodName = results.getString("descriptor"); // individual data value

                // Add the country object to the array
                foodGroups.add(foodName);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just print the error
            System.err.println(e.getMessage());
            // e.printStackTrace();
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
                // e.printStackTrace();
            }
        }

        // Finally we return all of the countries
        return foodGroups;
    }

    public ArrayList<Commodity> getAllCommodity() {
        // Create the ArrayList of Country objects to return
        ArrayList<Commodity> commodities = new ArrayList<Commodity>();

        // // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // // The Query
            String query = """
                SELECT DISTINCT 
                    cpc.subclass, cpc.descriptor 
                FROM 
                    H_CPC cpc 
                WHERE 
                    cpc.subclass is not null
                    and cpc.class is not null
                ORDER BY descriptor asc;
            
            """;

            // // Get Result
            ResultSet results = statement.executeQuery(query);

            // // Process all of the results
            while (results.next()) {
                // // Lookup the columns we need
                String name = results.getString("descriptor");
                String cpc = results.getString("subclass");

                // // Create a Country Object
                Commodity commodity = new Commodity(cpc, name);

                // // Add the Country object to the array
                commodities.add(commodity);
            }

            // // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // // Finally we return all of the countries
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
                double lossPercent = results.getDouble("maxLoss");
                String commodity = results.getString("descriptor");

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
        // System.out.println(maxLoss.size());
        return maxLoss;
    }

    public ArrayList<CommoditiesAllDetails> getLossDifferenceForTwoYears(ArrayList<String> countries,
            String startYear, String endYear,
            ArrayList<String> chosenColumns, String sortOption,
            String sortDirection) {
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
                        -- tbl1.regionName,
                        ROUND(avg(tbl1.lossPercent),2) as loss1,
                        tbl2.year as year2,
                        ROUND(avg(tbl2.lossPercent),2) as loss2,
                        ROUND(avg(tbl2.lossPercent) - avg(tbl1.lossPercent),2) as difference,
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
            // if (columnsAsString.isEmpty()) {
            // query += ",";
            // }

            query += " " + """
                    from LossWasteOccurrence as tbl1, LossWasteOccurrence as tbl2, Country c, CPC comm
                    where
                        tbl1.m49Code = tbl2.m49Code
                        and tbl1.cpcCode = comm.subclass
                        and tbl1.m49Code = c.m49Code
                        -- and tbl1.regionName = tbl2.regionName
                    """;

            if (columnsAsString.contains("cpcCode")) {
                query += " and tbl1.cpcCode = tbl2.cpcCode";
            }
            if (columnsAsString.contains("activity")) {
                query += " and tbl1.activity = tbl2.activity";
            }
            if (columnsAsString.contains("foodSupplyStage")) {
                query += " and tbl1.foodSupplyStage = tbl2.foodSupplyStage";
            }
            if (columnsAsString.contains("cause")) {
                query += " and tbl1.cause = tbl2.cause";
            }

            query += " and tbl1.year = " + Integer.parseInt(startYear) +
                    " and tbl2.year = " + Integer.parseInt(endYear) +
                    " and tbl1.m49Code in (" + countriesAsString + ") ";

            if (!chosenColumns.isEmpty()) {
                query += "group by " + String.join(",", columnsAsArray) + " ";
            }

            if (sortOption != null && sortDirection != null) { // if there is a sort option
                query += "order by " + sortOption + " " + sortDirection;
            }

            query += ";";

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
                String regionName = null; // results.getString("regionName");
                double difference = results.getDouble("difference");
                String commodity = null;
                String activity = null;
                String stage = null;
                String cause = null;

                if (chosenColumns.contains("cpcCode")) {
                    commodity = results.getString("commodityName");
                }
                if (chosenColumns.contains("activity")) {
                    activity = results.getString("activity");
                }
                if (chosenColumns.contains("foodSupplyStage")) {
                    stage = results.getString("stage");
                }
                if (chosenColumns.contains("cause")) {
                    cause = results.getString("cause");
                }

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
        // System.out.println(maxLoss.size());
        return wantedDetails;
    }


    public ArrayList<CommoditiesAllDetails> getLossDifferenceForTwoYears2(ArrayList<String> commodities,
    String startYear, String endYear,
    ArrayList<String> chosenColumns, String sortOption,
    String sortDirection) {
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

    String commoditiesAsString = String.join(",", commodities);

    String[] columnsAsArray = new String[chosenColumns.size()];
    columnsAsArray = chosenColumns.toArray(columnsAsArray);
    for (int i = 0; i < columnsAsArray.length; ++i) {
        columnsAsArray[i] = "tbl1." + columnsAsArray[i];
    }
    String columnsAsString = String.join(",", chosenColumns);
    // The Query
    String query = """
                select  comm.descriptor as commodityName, comm.subclass,
                tbl1.year as year1,
                -- tbl1.regionName,
                ROUND(avg(tbl1.lossPercent),2) as loss1,
                tbl2.year as year2,
                ROUND(avg(tbl2.lossPercent),2) as loss2,
                ROUND(avg(tbl2.lossPercent) - avg(tbl1.lossPercent),2) as difference,
                tbl1.M49Code
                WHERE comm.subclass=tbl1.M49Code
            """;

    if (columnsAsString.contains("M49Code")) {
        query += ",c.countryName as countryName";
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
    // if (columnsAsString.isEmpty()) {
    // query += ",";
    // }

    query += " " + """
            from LossWasteOccurrence as tbl1, LossWasteOccurrence as tbl2, Country c, CPC comm
            where
                tbl1.m49Code = tbl2.m49Code
                and tbl1.cpcCode = comm.subclass
                and tbl1.m49Code = c.m49Code
                -- and tbl1.regionName = tbl2.regionName
            """;

    if (columnsAsString.contains("M49Code")) {
        query += " and tbl1.M49Code = tbl2.M49Code";
    }
    if (columnsAsString.contains("activity")) {
        query += " and tbl1.activity = tbl2.activity";
    }
    if (columnsAsString.contains("foodSupplyStage")) {
        query += " and tbl1.foodSupplyStage = tbl2.foodSupplyStage";
    }
    if (columnsAsString.contains("cause")) {
        query += " and tbl1.cause = tbl2.cause";
    }

    query += " and tbl1.year = " + Integer.parseInt(startYear) +
            " and tbl2.year = " + Integer.parseInt(endYear) +
            " and tbl1.cpcCode in (" + commoditiesAsString + ") ";

    if (!chosenColumns.isEmpty()) {
        query += "group by " + String.join(",", columnsAsArray) + " ";
    }

    if (sortOption != null && sortDirection != null) { // if there is a sort option
        query += "order by " + sortOption + " " + sortDirection;
    }

    query += ";";

    System.out.println(query);
    // Get Result
    ResultSet results = statement.executeQuery(query);

    // Process all of the results
    while (results.next()) {
        // Lookup the columns we need
        String commodity = results.getString("commodityName");
        int m49Code = results.getInt("m49Code");
        String cpcCode = results.getString("cpcCode");
        int year1 = results.getInt("year1");
        double loss1 = results.getDouble("loss1");
        int year2 = results.getInt("year2");
        double loss2 = results.getDouble("loss2");
        String regionName = null; // results.getString("regionName");
        double difference = results.getDouble("difference");
        String country = null;
        String activity = null;
        String stage = null;
        String cause = null;

        if (chosenColumns.contains("M49Code")) {
            commodity = results.getString("countryName");
        }
        if (chosenColumns.contains("activity")) {
            activity = results.getString("activity");
        }
        if (chosenColumns.contains("foodSupplyStage")) {
            stage = results.getString("stage");
        }
        if (chosenColumns.contains("cause")) {
            cause = results.getString("cause");
        }

        // Create a Country Object
        CommoditiesAllDetails receivedDetails = new CommoditiesAllDetails(commodity, m49Code, cpcCode,
                year1, loss1, year2, loss2, regionName, difference,
                country, activity, stage, cause);

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
// System.out.println(maxLoss.size());
return wantedDetails;
}

    public ArrayList<LossData> getLossForYearRange(ArrayList<String> countries, String startYear,
            String endYear, ArrayList<String> chosenColumns,
            String sortOption, String sortDirection) {
        // Create the ArrayList of Country objects to return
        ArrayList<LossData> wantedDetails = new ArrayList<>();

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
                        select  c.countryName, tbl1.year as year1, tbl1.lossPercent
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
            // if (columnsAsString.isEmpty()) {
            // query += ",";
            // }

            query += " " + """
                    from LossWasteOccurrence as tbl1, Country c, CPC comm
                    where
                        year between """ + " " + Integer.parseInt(startYear) + " and " + Integer.parseInt(endYear) + """
                        and tbl1.m49Code = c.m49Code
                    """;

            if (columnsAsString.contains("cpcCode")) {
                query += " and tbl1.cpcCode = comm.subclass";
            }

            query += " and tbl1.m49Code in (" + countriesAsString + ") ";

            if (sortOption != null && sortDirection != null) { // if there is a sort option
                query += "order by year " + sortDirection;
            }

            query += ";";

            System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String country = results.getString("countryName");
                int year = results.getInt("year1");
                double loss1 = results.getDouble("lossPercent");
                String commodity = null;
                String activity = null;
                String stage = null;
                String cause = null;

                if (chosenColumns.contains("cpcCode")) {
                    commodity = results.getString("commodityName");
                }
                if (chosenColumns.contains("activity")) {
                    activity = results.getString("activity");
                }
                if (chosenColumns.contains("foodSupplyStage")) {
                    stage = results.getString("stage");
                }
                if (chosenColumns.contains("cause")) {
                    cause = results.getString("cause");
                }

                // Create a Country Object
                LossData receivedDetails = new LossData(country, year, loss1,
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
        // System.out.println(maxLoss.size());
        return wantedDetails;
    }


    public ArrayList<lossData2> getLossForYearRange2(ArrayList<String> commodities, String startYear,
    String endYear, ArrayList<String> chosenColumns,
    String sortOption, String sortDirection) {
// Create the ArrayList of Country objects to return
ArrayList<lossData2> wantedDetails = new ArrayList<>();

// Setup the variable for the JDBC connection
Connection connection = null;

try {
    // Connect to JDBC data base
    connection = DriverManager.getConnection(DATABASE);

    // Prepare a new SQL Query & Set a timeout
    Statement statement = connection.createStatement();
    statement.setQueryTimeout(30);

    String commoditiesAsString = String.join(",", commodities);

    String[] columnsAsArray = new String[chosenColumns.size()];
    columnsAsArray = chosenColumns.toArray(columnsAsArray);
    for (int i = 0; i < columnsAsArray.length; ++i) {
        columnsAsArray[i] = "tbl1." + columnsAsArray[i];
    }
    String columnsAsString = String.join(",", chosenColumns);
    // The Query
    String query = """
                select  comm.descriptor, tbl1.year as year1, tbl1.lossPercent
            """;

    if (columnsAsString.contains("M49Code")) {
        query += ",c.countryName as countryName";
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
    // if (columnsAsString.isEmpty()) {
    // query += ",";
    // }

    query += " " + """
            from LossWasteOccurrence as tbl1, Country c, CPC comm
            where
                year between """ + " " + Integer.parseInt(startYear) + " and " + Integer.parseInt(endYear) + """
                and tbl1.cpcCode = comm.subclass
            """;

    if (columnsAsString.contains("M49Code")) {
        query += " and tbl1.m49Code = c.m49Code";
    }

    query += " and tbl1.cpcCode in (" + commoditiesAsString + ") ";

    if (sortOption != null && sortDirection != null) { // if there is a sort option
        query += "order by year " + sortDirection;
    }

    query += ";";

    System.out.println(query);
    // Get Result
    ResultSet results = statement.executeQuery(query);

    // Process all of the results
    while (results.next()) {
        // Lookup the columns we need
        String commodity = results.getString("commodityName");
        int year = results.getInt("year1");
        double loss1 = results.getDouble("lossPercent");
        String country = null;
        String activity = null;
        String stage = null;
        String cause = null;

        if (chosenColumns.contains("M49Code")) {
            commodity = results.getString("countryName");
        }
        if (chosenColumns.contains("activity")) {
            activity = results.getString("activity");
        }
        if (chosenColumns.contains("foodSupplyStage")) {
            stage = results.getString("stage");
        }
        if (chosenColumns.contains("cause")) {
            cause = results.getString("cause");
        }

        // Create a Commodity Object
        lossData2 receivedDetails = new lossData2(commodity, year, loss1,
                country, activity, stage, cause);

        System.out.println(receivedDetails);
        // Add the Commodity object to the array
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
// System.out.println(maxLoss.size());
return wantedDetails;
}

    public ArrayList<CommodityData> getCommodityDataBySimilarityFactor(String cpcCode, String similarityFactor) {
        ArrayList<CommodityData> similarGroupData = new ArrayList<>();
        String query = "";
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (similarityFactor.equals("maxLoss")) {
                // The Query
                query += """
                    select cpc.descriptor as commodityName, grp.descriptor as groupName, cpc.groupNumber, max(lwo.lossPercent) as maxLoss
                    from H_CPC cpc, H_CPC grp, LossWasteOccurrence lwo
                    where 
                        substr(lwo.cpcCode, 1,3) = substr('""" + cpcCode + "', 1,3)" +
                """
                        and cpc.subclass = '""" + cpcCode + "'" + 
                """
                        and cpc.groupNumber = grp.groupNumber
                        and grp.class = ''
                        and grp.subclass = '';
                """;

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String commodityName = results.getString("commodityName");
                    String groupName = results.getString("groupName");
                    String groupNumber = results.getString("groupNumber");
                    //String similarityFactor = "maxLoss";
                    double maxLoss = results.getDouble(similarityFactor);

                    // Create a Country Object
                    CommodityData data = new CommodityData(commodityName, groupName, groupNumber, similarityFactor, maxLoss);

                    // Add the Country object to the array
                    similarGroupData.add(data);
                }
            }
            else if (similarityFactor.equals("minLoss")) {
                query += """
                    select cpc.descriptor as commodityName, grp.descriptor as groupName, cpc.groupNumber, min(lwo.lossPercent) as minLoss
                    from H_CPC cpc, H_CPC grp, LossWasteOccurrence lwo
                    where 
                        substr(lwo.cpcCode, 1,3) = substr('""" + cpcCode + "', 1,3)" +
                """
                        and cpc.subclass = '""" + cpcCode + "'" + 
                """
                        and cpc.groupNumber = grp.groupNumber
                        and grp.class = ''
                        and grp.subclass = '';
            
                """;
                System.out.println(query);

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String commodityName = results.getString("commodityName");
                    String groupName = results.getString("groupName");
                    String groupNumber = results.getString("groupNumber");
                    //String similarityFactor = "minLoss";
                    double minLoss = results.getDouble(similarityFactor);

                    // Create a Country Object
                    CommodityData data = new CommodityData(commodityName, groupName, groupNumber, similarityFactor, minLoss);

                    // Add the Country object to the array
                    similarGroupData.add(data);
                }
            }
            else if (similarityFactor.equals("ratio")) {
                query += """
                    select cpc.descriptor as commodityName, grp.descriptor as groupName, cpc.groupNumber, ratios.LossWasteRatio as ratio
                    from
                        H_CPC cpc,
                        H_CPC grp,
                        (select substr(tbl1.cpcCode, 1, 3) as groupNumber,
                                avg(tbl1.lossPercent) as LossPercent,
                                avg(tbl2.lossPercent) as WastePercent,
                                avg(tbl1.lossPercent)/avg(tbl2.lossPercent) as LossWasteRatio
                        from LossWasteOccurrence tbl1, LossWasteOccurrence tbl2
                        where tbl1.activity in ('Harvest', 'Farm', 'Storage', 'Processing', 'Post-harvest', 'Pre-harvest', 'Export', 'Market', 'Collector', 'Stacking', 'Packing', 'Grading')
                        and tbl2.activity in ('Retail', 'Wholesale', 'Trader', 'Whole supply chain', 'Transport', 'Households', 'Food Services', 'Distribution')
                        and substr(tbl1.cpcCode, 1, 3) = substr(tbl2.cpcCode, 1, 3)
                        group by substr(tbl1.cpcCode, 1, 3)) ratios
                        where
                            cpc.groupNumber = grp.groupNumber
                            and grp.class = ''
                            and grp.subclass = ''
                            and cpc.subclass = '""" + cpcCode + "' and ratios.groupNumber = substr('" + cpcCode + "', 1,3)";
                    
                System.out.println(query);

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String commodityName = results.getString("commodityName");
                    String groupName = results.getString("groupName");
                    String groupNumber = results.getString("groupNumber");
                    //String similarityFactor = "ratio";
                    double ratio = results.getDouble(similarityFactor);


                    CommodityData data = new CommodityData(commodityName, groupName, groupNumber, similarityFactor, ratio);

                    // Add the Country object to the array
                    similarGroupData.add(data);
                }
            }
            
            System.out.println(query);
            

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
            
            return similarGroupData;
            
    }

    public ArrayList<CountryData> getCountryDataBySimilarityFactor(String M49Code, String similarityFactor) {
        ArrayList<CountryData> similarRegionData = new ArrayList<>();
        String query = "";
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (similarityFactor.equals("both")) {
                // The Query
                query += """
                    select cpc.descriptor as commodityName, grp.descriptor as groupName, cpc.groupNumber, max(lwo.lossPercent) as maxLoss
                    from H_CPC cpc, H_CPC grp, LossWasteOccurrence lwo
                    where 
                        substr(lwo.cpcCode, 1,3) = substr('""" + M49Code + "', 1,3)" +
                """
                        and cpc.subclass = '""" + M49Code + "'" + 
                """
                        and cpc.groupNumber = grp.groupNumber
                        and grp.class = ''
                        and grp.subclass = '';
                """;

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String countryName = results.getString("countryName");
                    String regionName = results.getString("regionName");
                    String groupNumber = results.getString("groupNumber");
                    //String similarityFactor = "maxLoss";
                    double both = results.getDouble(similarityFactor);

                    // Create a Country Object
                    CountryData data = new CountryData(countryName, regionName, groupNumber, similarityFactor, both);

                    // Add the Country object to the array
                    similarRegionData.add(data);
                }
            }
            else if (similarityFactor.equals("percentage")) {
                query += """
                  SELECT H_Country.countryName, AVG(LossWasteOccurrence.lossPercent) AS avg_loss_percent
FROM LossWasteOccurrence
JOIN H_Country ON LossWasteOccurrence.m49Code = H_Country.m49Code
GROUP BY H_Country.countryName
ORDER BY avg_loss_percent DESC; ;
            
                """;
                System.out.println(query);

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String countryName = results.getString("countryName");
                    String M49code = results.getString("M49Code");
                    String groupNumber = results.getString("groupNumber");
                    //String similarityFactor = "minLoss";
                    double percentage = results.getDouble(similarityFactor);

                    // Create a Country Object
                    CountryData data = new CountryData(countryName, M49code, groupNumber, similarityFactor, percentage);

                    // Add the Country object to the array
                    similarRegionData.add(data);
                }
            }
            else if (similarityFactor.equals("products")) {
                query += """
                    select cpc.descriptor as commodityName, grp.descriptor as groupName, cpc.groupNumber, ratios.LossWasteRatio as ratio
                    from
                        H_CPC cpc,
                        H_CPC grp,
                        (select substr(tbl1.cpcCode, 1, 3) as groupNumber,
                                avg(tbl1.lossPercent) as LossPercent,
                                avg(tbl2.lossPercent) as WastePercent,
                                avg(tbl1.lossPercent)/avg(tbl2.lossPercent) as LossWasteRatio
                        from LossWasteOccurrence tbl1, LossWasteOccurrence tbl2
                        where tbl1.activity in ('Harvest', 'Farm', 'Storage', 'Processing', 'Post-harvest', 'Pre-harvest', 'Export', 'Market', 'Collector', 'Stacking', 'Packing', 'Grading')
                        and tbl2.activity in ('Retail', 'Wholesale', 'Trader', 'Whole supply chain', 'Transport', 'Households', 'Food Services', 'Distribution')
                        and substr(tbl1.cpcCode, 1, 3) = substr(tbl2.cpcCode, 1, 3)
                        group by substr(tbl1.cpcCode, 1, 3)) ratios
                        where
                            cpc.groupNumber = grp.groupNumber
                            and grp.class = ''
                            and grp.subclass = ''
                            and cpc.subclass = '""" + M49Code + "' and ratios.groupNumber = substr('" +M49Code + "', 1,3)";
                    
                System.out.println(query);

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String countryName = results.getString("countryName");
                    String groupName = results.getString("groupName");
                    String groupNumber = results.getString("groupNumber");
                    //String similarityFactor = "ratio";
                    double ratio = results.getDouble(similarityFactor);


                    CountryData data = new CountryData(countryName, groupName, groupNumber, similarityFactor, ratio);

                    // Add the Country object to the array
                    similarRegionData.add(data);
                }
            }
            
            System.out.println(query);
            

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
            
            return similarRegionData;
            
    }


    public ArrayList<FoodGroupSimilarityData> getFoodGroupDataBySimilarityFactor (CommodityData selectedCommodityData, 
                                                            String similarityFactor, int limitGroup) {
        ArrayList<FoodGroupSimilarityData> foodGroupDataBySimilarityFactor = new ArrayList<FoodGroupSimilarityData>();
        String query = "";

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            

            if (similarityFactor.equals("maxLoss")) {
                // The Query
                query += """
                    select 
                        grp.descriptor as groupName, 
                        grp.groupNumber, 
                        max(lwo.lossPercent) as maxLoss, 
                        ROUND((max(lwo.lossPercent)/""" + selectedCommodityData.getSimilarityFactorValue() + " " + """
                    *100),2) as similarityScore""" + " " + """
                    from 
                        H_CPC grp, 
                        LossWasteOccurrence lwo
                    where
                        grp.subclass = ''
                        and grp.class = ''
                        and grp.groupNumber is not null
                        and substr(lwo.cpcCode,1,3) = grp.groupNumber
                        and grp.groupNumber <> '""" + selectedCommodityData.getGroupNumber() + """
                        ' and lwo.lossPercent <= """ + selectedCommodityData.getSimilarityFactorValue() + " " + """
                    group by 
                        substr(lwo.cpcCode,1,3)
                    order by similarityScore desc""" + " " +
                    "limit " + limitGroup;

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String groupName = results.getString("groupName");
                    double similarityFactorValue = results.getDouble(similarityFactor);
                    double score = results.getDouble("similarityScore");
    
                    // Create a Country Object
                    FoodGroupSimilarityData data = new FoodGroupSimilarityData(groupName, similarityFactorValue, score);
    
                    // Add the Country object to the array
                    foodGroupDataBySimilarityFactor.add(data);
                }
            }
            else if (similarityFactor.equals("minLoss")) {
                query += """
                    select 
                        grp.descriptor as groupName, 
                        grp.groupNumber, 
                        min(lwo.lossPercent) as minLoss, 
                        ROUND((min(lwo.lossPercent)/""" + selectedCommodityData.getSimilarityFactorValue() + " " + """
                        *100),2) as similarityScore""" + " " + """
                    from 
                        H_CPC grp, 
                        LossWasteOccurrence lwo
                    where
                        grp.subclass = ''
                        and grp.class = ''
                        and grp.groupNumber is not null
                        and substr(lwo.cpcCode,1,3) = grp.groupNumber
                        and grp.groupNumber <> '""" + selectedCommodityData.getGroupNumber() + """
                        ' and lwo.lossPercent >= """ + selectedCommodityData.getSimilarityFactorValue() + " " + """
                    group by 
                        substr(lwo.cpcCode,1,3)
                    order by similarityScore desc""" + " " +
                    "limit " + limitGroup;

                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String groupName = results.getString("groupName");
                    double similarityFactorValue = results.getDouble(similarityFactor);
                    double score = results.getDouble("similarityScore");
    
                    // Create a Country Object
                    FoodGroupSimilarityData data = new FoodGroupSimilarityData(groupName, similarityFactorValue, score);
    
                    // Add the Country object to the array
                    foodGroupDataBySimilarityFactor.add(data);
                }
            }
            else if (similarityFactor.equals("ratio")) {
                query += """
                    select 
                        grp.descriptor as groupName, grp.groupNumber, ROUND(ratios.LossWasteRatio, 2) as ratio, 
                        ROUND((ratios.LossWasteRatio/""" + selectedCommodityData.getSimilarityFactorValue() + " " + """
                        *100),2) as similarityScore
                    from 
                        H_CPC grp, 
                        H_LWO_Ratios ratios
                    where
                        grp.subclass = ''
                        and grp.class = ''
                        and grp.groupNumber is not null
                        and ratios.groupNumber = grp.groupNumber
                        and grp.groupNumber <> '""" + selectedCommodityData.getGroupNumber() + "' " + """
                        and ratios.LossWasteRatio <= """ + selectedCommodityData.getSimilarityFactorValue() + " " + """
                    order by similarityScore desc""" + " " +
                    "limit " + limitGroup;
    
                // Get Result
                ResultSet results = statement.executeQuery(query);

                // Process all of the results
                while (results.next()) {
                    // Lookup the columns we need
                    String groupName = results.getString("groupName");
                    double similarityFactorValue = results.getDouble(similarityFactor);
                    double score = results.getDouble("similarityScore");
    
                    // Create a Country Object
                    FoodGroupSimilarityData data = new FoodGroupSimilarityData(groupName, similarityFactorValue, score);
    
                    // Add the Country object to the array
                    foodGroupDataBySimilarityFactor.add(data);
                }
            }
                
            System.out.println(query);
            
            // Get Result
            ResultSet results = statement.executeQuery(query);
 
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
        return foodGroupDataBySimilarityFactor;
    }


public ArrayList<countrySimilarityData> getRegionDataBySimilarityFactor (CountryData selectedCountryData, 
String similarityFactor, int limitGroup) {
ArrayList<countrySimilarityData> RegionDataBySimilarityFactor = new ArrayList<countrySimilarityData>();
String query = "";

Connection connection = null;

try {
// Connect to JDBC data base
connection = DriverManager.getConnection(DATABASE);

// Prepare a new SQL Query & Set a timeout
Statement statement = connection.createStatement();
statement.setQueryTimeout(30);



if (similarityFactor.equals("maxLoss")) {
// The Query
query += """
select 
grp.descriptor as groupName, 
grp.groupNumber, 
max(lwo.lossPercent) as maxLoss, 
(max(lwo.lossPercent)/""" + selectedCountryData.getSimilarityFactorValue() + " " + """
) as similarityScore""" + " " + """
from 
H_CPC grp, 
LossWasteOccurrence lwo
where
grp.subclass = ''
and grp.class = ''
and grp.groupNumber is not null
and substr(lwo.cpcCode,1,3) = grp.groupNumber
and grp.groupNumber <> '""" + selectedCountryData.getGroupNumber() + """
' and lwo.lossPercent <= """ + selectedCountryData.getSimilarityFactorValue() + " " + """
group by 
substr(lwo.cpcCode,1,3)
order by similarityScore desc""" + " " +
"limit " + limitGroup;

// Get Result
ResultSet results = statement.executeQuery(query);

// Process all of the results
while (results.next()) {
// Lookup the columns we need
String groupName = results.getString("groupName");
double similarityFactorValue = results.getDouble(similarityFactor);
double score = results.getDouble("similarityScore");

// Create a Country Object
countrySimilarityData data = new countrySimilarityData(groupName, similarityFactorValue, score);

// Add the Country object to the array
RegionDataBySimilarityFactor.add(data);
}
}
else if (similarityFactor.equals("minLoss")) {
query += """
select 
grp.descriptor as groupName, 
grp.groupNumber, 
min(lwo.lossPercent) as minLoss, 
(min(lwo.lossPercent)/""" + selectedCountryData.getSimilarityFactorValue() + " " + """
) as similarityScore""" + " " + """
from 
H_CPC grp, 
LossWasteOccurrence lwo
where
grp.subclass = ''
and grp.class = ''
and grp.groupNumber is not null
and substr(lwo.cpcCode,1,3) = grp.groupNumber
and grp.groupNumber <> '""" + selectedCountryData.getGroupNumber() + """
' and lwo.lossPercent >= """ + selectedCountryData.getSimilarityFactorValue() + " " + """
group by 
substr(lwo.cpcCode,1,3)
order by similarityScore desc""" + " " +
"limit " + limitGroup;

// Get Result
ResultSet results = statement.executeQuery(query);

// Process all of the results
while (results.next()) {
// Lookup the columns we need
String groupName = results.getString("groupName");
double similarityFactorValue = results.getDouble(similarityFactor);
double score = results.getDouble("similarityScore");

// Create a Country Object
countrySimilarityData data = new countrySimilarityData(groupName, similarityFactorValue, score);

// Add the Country object to the array
RegionDataBySimilarityFactor.add(data);
}
}
else if (similarityFactor.equals("ratio")) {
query += """
select 
grp.descriptor as groupName, grp.groupNumber ,ratios.LossWasteRatio as ratio, 
(ratios.LossWasteRatio/""" + selectedCountryData.getSimilarityFactorValue() + " " + """
) as similarityScore
from 
H_CPC grp, 
(select substr(loss.cpcCode, 1, 3) as groupNumber,
avg(loss.lossPercent) as LossPercent,
avg(loss.lossPercent) as WastePercent,
avg(loss.lossPercent)/avg(waste.lossPercent) as LossWasteRatio
from LossWasteOccurrence loss, LossWasteOccurrence waste
where loss.activity in ('Harvest', 'Farm', 'Storage', 'Processing', 'Post-harvest', 'Pre-harvest', 'Export', 'Market', 'Collector', 'Stacking', 'Packing', 'Grading')
and waste.activity in ('Retail', 'Wholesale', 'Trader', 'Whole supply chain', 'Transport', 'Households', 'Food Services', 'Distribution')
and substr(loss.cpcCode, 1, 3) = substr(waste.cpcCode, 1, 3)
group by substr(loss.cpcCode, 1, 3)) ratios
where
grp.subclass = ''
and grp.class = ''
and grp.groupNumber is not null
and ratios.groupNumber = grp.groupNumber
and grp.groupNumber <> '""" + selectedCountryData.getGroupNumber() + "' " + """
and ratios.LossWasteRatio <= """ + selectedCountryData.getSimilarityFactorValue() + " " + """
order by similarityScore desc""" + " " +
"limit " + limitGroup;

// Get Result
ResultSet results = statement.executeQuery(query);

// Process all of the results
while (results.next()) {
// Lookup the columns we need
String groupName = results.getString("groupName");
double similarityFactorValue = results.getDouble(similarityFactor);
double score = results.getDouble("similarityScore");

// Create a Country Object
countrySimilarityData data = new countrySimilarityData(groupName, similarityFactorValue, score);

// Add the Country object to the array
RegionDataBySimilarityFactor.add(data);
}
}

System.out.println(query);

// Get Result
ResultSet results = statement.executeQuery(query);

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
return RegionDataBySimilarityFactor;
}

}