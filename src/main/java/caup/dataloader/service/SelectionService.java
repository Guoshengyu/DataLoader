package caup.dataloader.service;

import caup.dataloader.dao.DimIndicator3Dao;
import caup.dataloader.entity.DimIndicator3Entity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Richard on 2015/03/21 .
 */
@Service
    public class SelectionService {

    @Autowired
    private DimIndicator3Dao dimIndicator3Dao;

    public JSONObject getIndicator(){
        return  indicatorConverToJson(dimIndicator3Dao.getIndicatorList().getData());
    }

    private JSONObject indicatorConverToJson(List<DimIndicator3Entity> dimIndicator3EntityList){
        JSONObject ret = new JSONObject();
        JSONArray indexArray = new JSONArray();
        for(DimIndicator3Entity entity: dimIndicator3EntityList) {
            JSONObject dataObject = new JSONObject();
            if(entity.getIndexName() != null)
                dataObject.put("IndexName", entity.getIndexName().trim());
            else
                dataObject.put("IndexName", "");
            if(entity.getUnit() != null)
                dataObject.put("Unit", entity.getUnit().trim());
            else
                dataObject.put("Unit", "");
            indexArray.put(dataObject);
        }
        ret.put("ceshi", "测试");
        ret.put("IndexList", indexArray);

        return ret;
    }
}
