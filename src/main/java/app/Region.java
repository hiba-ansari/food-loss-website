package app;

public class Region {
    private String regionName; //PARTIAL KEY

    public Region (String name) {
        regionName = name;
    }

    public String getRegionName() {
        return regionName;
     }
}
