package caup.dataloader.service;

import caup.dataloader.dao.DimIndicator3Dao;
import caup.dataloader.entity.DimIndicator3Entity;
import caup.dataloader.util.DataWrapper;
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

    public DataWrapper<List<DimIndicator3Entity>> getIndicator(){
        return  dimIndicator3Dao.getIndicatorList();
    }


}
