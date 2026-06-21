package app.dto;

public class LossData {
    private String country;
    private int year;
    private double loss;
    private String commodity;
    private String activity;
    private String stage;
    private String cause;

    public LossData(String country, int year, double loss, String commodity, String activity, 
                        String stage, String cause) {
        this.country = country;
        this.year = year;
        this.loss = loss;
        this.commodity = commodity;
        this.activity = activity;
        this.stage = stage;
        this.cause = cause;

        if (commodity == null) {
            this.commodity = "Not available";
         }
         if (activity == null) {
            this.activity = "Not available";
         }
         if (stage == null) {
            this.stage = "Not available";
         }
         if (cause == null) {
            this.cause = "Not available";
         }
    }

    public String getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }

    public double getLoss() {
        return loss;
    }

    public String getCommodity() {
        return commodity;
    }

    public String getActivity() {
        return activity;
    }

    public String getStage() {
        return stage;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return "LossData [country=" + country + ", year=" + year + ", loss=" + loss + ", commodity=" + commodity
                + ", activity=" + activity + ", stage=" + stage + ", cause=" + cause + "]";
    }

}