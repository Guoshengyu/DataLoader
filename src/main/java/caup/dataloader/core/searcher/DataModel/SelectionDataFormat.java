package caup.dataloader.core.searcher.DataModel;

import java.util.Map;

/**
 * Created by Richard on 2015/03/23 .
 */
public class SelectionDataFormat {
    String region;
    String databaseIndex;
    String databaseUnit;
    Map<String, String> yearbookIndexUnitMap;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDatabaseIndex() {
        return databaseIndex;
    }

    public void setDatabaseIndex(String databaseIndex) {
        this.databaseIndex = databaseIndex;
    }

    public String getDatabaseUnit() {
        return databaseUnit;
    }

    public void setDatabaseUnit(String databaseUnit) {
        this.databaseUnit = databaseUnit;
    }

    public Map<String, String> getYearbookIndexUnitMap() {
        return yearbookIndexUnitMap;
    }

    public void setYearbookIndexUnitMap(Map<String, String> yearbookIndexList) {
        this.yearbookIndexUnitMap = yearbookIndexList;
    }


}
