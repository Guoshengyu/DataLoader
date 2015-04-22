package caup.dataloader.core.searcher.DataModel;

import java.util.Map;

/**
 * Created by Richard on 2015/03/23 .
 */
public class ExcelOutputDataFormat {
    String region;
    String DBIndex;
//    String unit;
    Map<String, Double> yValueMap;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDBIndex() {
        return DBIndex;
    }

    public void setDBIndex(String DBIndex) {
        this.DBIndex = DBIndex;
    }

    public Map<String, Double> getyValueMap() {
        return yValueMap;
    }

    public void setyValueMap(Map<String, Double> yValueMap) {
        this.yValueMap = yValueMap;
    }
}
