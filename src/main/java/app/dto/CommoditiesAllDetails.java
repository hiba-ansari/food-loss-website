package app.dto;

public class CommoditiesAllDetails {
    // commodity attributes from ERD
   private String country;
   private int m49Code;
   private String cpcCode;
   private int year1;
   private double loss1;
   private int year2;
   private double loss2;
   private String regionName;
   private double difference;
   private String commodity;
   private String activity;
   private String stage;
   private String cause;
   
   public CommoditiesAllDetails(String country, int m49Code, String cpcCode, int year1, double loss1, int year2,
         double loss2, String regionName, double difference, String commodity, String activity, String stage, String cause) {
      this.country = country;
      this.m49Code = m49Code;
      this.cpcCode = cpcCode;
      this.year1 = year1;
      this.loss1 = loss1;
      this.year2 = year2;
      this.loss2 = loss2;
      this.regionName = regionName;
      this.difference = difference;
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

   public int getM49Code() {
      return m49Code;
   }

   public String getCpcCode() {
      return cpcCode;
   }

   public int getYear1() {
      return year1;
   }

   public double getLoss1() {
      return loss1;
   }

   public int getYear2() {
      return year2;
   }

   public double getLoss2() {
      return loss2;
   }

   public String getRegionName() {
      return regionName;
   }

   public double getDifference() {
      return difference;
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
      return "CommoditiesAllDetails [country=" + country + ", m49Code=" + m49Code + ", cpcCode=" + cpcCode + ", year1="
            + year1 + ", loss1=" + loss1 + ", year2=" + year2 + ", loss2=" + loss2 + ", difference=" + difference
            + ", commodity=" + commodity + ", activity=" + activity + ", stage=" + stage + ", cause=" + cause + "]";
   }

   

}
