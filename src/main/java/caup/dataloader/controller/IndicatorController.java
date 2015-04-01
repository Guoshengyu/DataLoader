package caup.dataloader.controller;

import caup.dataloader.service.SelectionService;
import caup.dataloader.entity.DimIndicator3Entity;
import caup.dataloader.util.DataWrapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Richard on 2015/03/21 .
 */
@Controller
@RequestMapping(value = "/getData")
public class IndicatorController {

    @Autowired
    SelectionService selectionService;

    @RequestMapping(value = "/getIndicator",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getIndicatorList(){
        DataWrapper<List<DimIndicator3Entity>> result =  selectionService.getIndicator();
   //     System.out.println(result);
        return indicatorConverToJson(result.getData()).toString();
    }

    /*public List<DimIndicator3Entity> getIndicatorAsList(){
        return selectionService.getIndicator().getData();
    }*/
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
       // ret.put("ceshi", "测试");
        ret.put("IndexList", indexArray);

        return ret;
    }
}
