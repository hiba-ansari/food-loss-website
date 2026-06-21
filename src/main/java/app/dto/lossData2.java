package app.dto;

public class lossData2 {
    private String commodity;
    private int year;
    private double loss;
    private String country;
    private String activity;
    private String stage;
    private String cause;

    public lossData2(String commodity, int year, double loss, String country, String activity, 
                        String stage, String cause) {
        this.country = country;
        this.year = year;
        this.loss = loss;
        this.commodity = commodity;
        this.activity = activity;
        this.stage = stage;
        this.cause = cause;

        if (country == null) {
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
        return "LossData [commodity=" + commodity + ", year=" + year + ", loss=" + loss + ", country=" + country
                + ", activity=" + activity + ", stage=" + stage + ", cause=" + cause + "]";
    }

}
