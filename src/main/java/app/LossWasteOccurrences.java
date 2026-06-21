package app;

public class LossWasteOccurrences {
    public String FOOD_ID; //PRIMARY KEY
    private String region;
    private String activity;
    private String cause;
    private String supplyStage;

    public LossWasteOccurrences (String region, String activity, String cause, String stage) {
        this.region = region;
        this.activity = activity;
        this.cause = cause;
        supplyStage = stage;
    }
}
