package caup.dataloader.controller;

import caup.dataloader.entity.DimIndicator3Entity;
import caup.dataloader.service.SelectionService;
import caup.dataloader.transformer.reader.CoreIndexSearcher;
import caup.dataloader.transformer.reader.DataModel.InputDataFormat;
import caup.dataloader.transformer.reader.DataModel.SelectionDataFormat;
import caup.dataloader.transformer.reader.DataReader;
import caup.dataloader.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 2015/03/27 .
 */
@Controller
@RequestMapping("/searchIndex")
public class SearchIndexController {

    @Autowired
    SelectionService selectionService;

    @RequestMapping(value = "/getResult",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getLuceneSeachResult(@RequestParam("fileName") String fileName, HttpServletRequest request){
        List<SelectionDataFormat> ret = new ArrayList<SelectionDataFormat>();
        SelectionDataFormat selectionDataFormat = null;
        List<DimIndicator3Entity> dimIndicator3EntityList = selectionService.getIndicator().getData();
        try {
            String realPath = request.getSession().getServletContext().getRealPath("/upload/");
            DataReader reader = new DataReader(realPath + File.separator +fileName, 1, 100, 2, true);
            List<InputDataFormat> inputDataFormatList = reader.getYearBookIndexListNew();

            //Put the preprocessed data and the orginal data into map
            Map<String, String> orgIndexMap = new HashMap<String, String>();
            for (InputDataFormat inputDataFormat : inputDataFormatList) {
                orgIndexMap.put(StringUtils.yearbookIndexPreprocess(inputDataFormat.getIndexName().replaceAll(" ", "")), inputDataFormat.getIndexName().replaceAll(" ", ""));
            }
            searchForAllIndex(ret, dimIndicator3EntityList, inputDataFormatList, orgIndexMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectionDataConverToJson(ret).toString();
    }

    private void searchForAllIndex(List<SelectionDataFormat> ret, List<DimIndicator3Entity> dimIndicator3EntityList, List<InputDataFormat> inputDataFormatList, Map<String, String> orgIndexMap) throws Exception {
        SelectionDataFormat selectionDataFormat;
   //     for(int i = 0; i != 30; i++) {
        for(int i = 0; i != dimIndicator3EntityList.size(); ++i){
            CoreIndexSearcher coreIndexSearcher = new CoreIndexSearcher(dimIndicator3EntityList.get(i).getIndexName().trim(), new ArrayList<String>(orgIndexMap.keySet()));
            List<String> result = coreIndexSearcher.getTopYearbookIndex();
            // System.out.println(dimIndicator3EntityList.get(i).getIndexName());
            Map<String, String> map =new HashMap<String, String>();
            for(String str: result){
                map.put(orgIndexMap.get(str), searchFromInputDataModel(inputDataFormatList,orgIndexMap.get(str)).getUnit());
            }
            selectionDataFormat = new SelectionDataFormat();
            selectionDataFormat.setDatabaseIndex(dimIndicator3EntityList.get(i).getIndexName());
            selectionDataFormat.setDatabaseUnit(dimIndicator3EntityList.get(i).getUnit());
            selectionDataFormat.setYearbookIndexUnitMap(map);

            System.out.println(dimIndicator3EntityList.get(i).getIndexName());
            for(String str: result)
                System.out.print(orgIndexMap.get(str) + " | ");
            System.out.println();
            System.out.println("------");
            ret.add(selectionDataFormat);
        }
    }

    private InputDataFormat searchFromInputDataModel(List<InputDataFormat> inputDataFormatList, String yearbookIndex){
        for(InputDataFormat item: inputDataFormatList){
            if(item.getIndexName().equals(yearbookIndex))
                return item;
        }
        return new InputDataFormat();
    }

    private JSONObject selectionDataConverToJson(List<SelectionDataFormat> selectionDataFormatList){
        JSONObject ret = new JSONObject();
        JSONArray indexArray = new JSONArray();
        for(SelectionDataFormat entity: selectionDataFormatList) {
            JSONObject dataObject = new JSONObject();
            if(entity.getRegion() != null)
                dataObject.put("Region", entity.getRegion().trim());
            else
                dataObject.put("Region", "");

            if(entity.getDatabaseIndex() != null)
                dataObject.put("DBIndex", entity.getDatabaseIndex().trim());
            else
                dataObject.put("DBIndex", "");

            if(entity.getDatabaseUnit() != null)
                dataObject.put("DBUnit", entity.getDatabaseUnit().trim());
            else
                dataObject.put("DBUnit", "");

            JSONArray ybIndexArray = new JSONArray();
           // JSONObject ybDataObject = new JSONObject();
         //   if(!entity.getYearbookIndexUnitMap().isEmpty()){
                for(String str: entity.getYearbookIndexUnitMap().keySet()) {
                    JSONObject ybDataObject = new JSONObject();
                    ybDataObject.put("ybIndex", str);
                    ybDataObject.put("ybUnit", entity.getYearbookIndexUnitMap().get(str));
                    ybIndexArray.put(ybDataObject);
                }
          //  }
            dataObject.put("ybIndexList", ybIndexArray);

            indexArray.put(dataObject);
        }
        //ret.put("Region", "NULL");
        ret.put("IndexList", indexArray);

        return ret;
    }
}
