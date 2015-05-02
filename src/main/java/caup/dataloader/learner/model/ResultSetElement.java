package caup.dataloader.learner.model;

import java.util.List;

/**
 * Created by Richard on 2015/05/02 .
 */
public class ResultSetElement {
    String DBIndex;
    List<String> YBIndexSelectionResultList;

    public String getDBIndex() {
        return DBIndex;
    }

    public void setDBIndex(String DBIndex) {
        this.DBIndex = DBIndex;
    }

    public List<String> getYBIndexSelectionResultList() {
        return YBIndexSelectionResultList;
    }

    public void setYBIndexSelectionResultList(List<String> YBIndexSelectionResultList) {
        this.YBIndexSelectionResultList = YBIndexSelectionResultList;
    }
}
