package caup.dataloader.searcher.model;

/**
 * Created by Richard on 2015/03/31 .
 */

public class SelectionResultFormat {
    String DBIndex;
    String DBUnit;
    String YBIndex;
    String YBUnit;

    public SelectionResultFormat(){ }

    public SelectionResultFormat(String _DBIndex, String _DBUnit,String _YBIndex,String _YBUnit){
        DBIndex =_DBIndex;
        DBUnit = _DBUnit;
        YBIndex = _YBIndex;
        YBUnit = _YBUnit;
    }

    public String getDBIndex() {
        return DBIndex;
    }

    public void setDBIndex(String DBIndex) {
        this.DBIndex = DBIndex;
    }

    public String getDBUnit() {
        return DBUnit;
    }

    public void setDBUnit(String DBUnit) {
        this.DBUnit = DBUnit;
    }

    public String getYBIndex() {
        return YBIndex;
    }

    public void setYBIndex(String YBIndex) {
        this.YBIndex = YBIndex;
    }

    public String getYBUnit() {
        return YBUnit;
    }

    public void setYBUnit(String YBUnit) {
        this.YBUnit = YBUnit;
    }
}
