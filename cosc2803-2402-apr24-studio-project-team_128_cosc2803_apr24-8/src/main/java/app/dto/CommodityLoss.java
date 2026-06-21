package app.dto;

public class CommodityLoss {
    private double lossPercentage = 0.0;
    private String commodity;

    public CommodityLoss(double loss, String commodityName) {
        this.lossPercentage = loss;
        this.commodity = commodityName;
    }

    public double getLossPercentage() {
        return lossPercentage;
    }

    public String getCommodity() {
        return commodity;
    }
    
}
