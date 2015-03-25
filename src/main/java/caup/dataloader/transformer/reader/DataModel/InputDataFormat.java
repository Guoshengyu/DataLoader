package caup.dataloader.transformer.reader.DataModel;

import java.util.Map;

/**
 * Created by Richard on 2015/03/23 .
 */
public class InputDataFormat {
    String region;
    String indexName;
    String unit;
    Map<String, Double> yValueMap;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
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
