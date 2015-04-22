package caup.dataloader.core.searcher.DataModel;

import java.util.Map;

/**
 * Created by Richard on 2015/03/23 .
 */
public class ExcelInputDataFormat {
    String region;
    String YBIndex;
    String unit;
    Map<String, Double> yValueMap;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getYBIndex() {
        return YBIndex;
    }

    public void setYBIndex(String YBIndex) {
        this.YBIndex = YBIndex;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map<String, Double> getyValueMap() {
        return yValueMap;
    }

    public void setyValueMap(Map<String, Double> yValueMap) {
        this.yValueMap = yValueMap;
    }
}
