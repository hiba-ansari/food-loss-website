package app.dto;

public class CountryData {
    private String countryName;
    private String groupName;
    private String groupNumber;
    private String similarityFactor;
    private double similarityFactorValue;

    public CountryData(String countryName, String groupName, String groupNumber,
            String similarityFactor, double similarityFactorValue) {
        this.countryName = countryName;
        this.groupName = groupName;
        this.groupNumber = groupNumber;
        this.similarityFactor = similarityFactor;
        this.similarityFactorValue = similarityFactorValue;
    }
    public String getCountryName() {
        return countryName;
    }
    public String getGroupName() {
        return groupName;
    }
    public String getGroupNumber() {
        return groupNumber;
    }
    public String getSimilarityFactor() {
        return similarityFactor;
    }
    public double getSimilarityFactorValue() {
        return similarityFactorValue;
    }
    
}
