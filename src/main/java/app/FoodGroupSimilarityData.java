package app;

public class FoodGroupSimilarityData {
    private String groupName;
    private double similarityFactorValue;
    private double similarityScore;
    public FoodGroupSimilarityData(String groupName, double similarityFactorValue, double similarityScore) {
        this.groupName = groupName;
        this.similarityFactorValue = similarityFactorValue;
        this.similarityScore = similarityScore;
    }
    public String getGroupName() {
        return groupName;
    }
    public double getSimilarityFactorValue() {
        return similarityFactorValue;
    }
    public double getSimilarityScore() {
        return similarityScore;
    }

}
