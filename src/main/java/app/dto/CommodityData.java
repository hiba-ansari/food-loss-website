package app.dto;

public class CommodityData {
    private String commodityName;
    private String groupName;
    private String groupNumber;
    private String similarityFactor;
    private double similarityFactorValue;
    public CommodityData(String commodityName, String groupName, String groupNumber,
            String similarityFactor, double similarityFactorValue) {
        this.commodityName = commodityName;
        this.groupName = groupName;
        this.groupNumber = groupNumber;
        this.similarityFactor = similarityFactor;
        this.similarityFactorValue = similarityFactorValue;
    }
    public String getCommodityName() {
        return commodityName;
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
