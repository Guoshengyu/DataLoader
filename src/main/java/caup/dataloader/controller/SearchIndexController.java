package caup.dataloader.controller;

import caup.dataloader.entity.DimIndicator3Entity;
import caup.dataloader.learner.ResultSetReader;
import caup.dataloader.learner.model.ResultSetElement;
import caup.dataloader.searcher.IndexDirectoryGenerator;
import caup.dataloader.service.SelectionService;
import caup.dataloader.searcher.CoreIndexSearcher;
import caup.dataloader.datafile.processer.model.ExcelInputDataFormat;
import caup.dataloader.searcher.model.SelectionDataFormat;
import caup.dataloader.datafile.processer.DataReader;
import caup.dataloader.unit.transformation.SearchResultSorter;
import caup.dataloader.unit.transformation.UnitDictonary;
import caup.dataloader.util.StringUtils;
import org.apache.lucene.store.Directory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Richard on 2015/03/27 .
 */
@Controller
@RequestMapping("/searchIndex")
public class SearchIndexController {

    @Autowired
    SelectionService selectionService;

    String dataDirPath;
    String uploadDirPath;

    @RequestMapping(value = "/getResult",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getLuceneSearchResult(@RequestParam("fileName") String fileName, @RequestParam("region") String region, HttpServletRequest request){
        List<SelectionDataFormat> ret = new ArrayList<SelectionDataFormat>();
        SelectionDataFormat selectionDataFormat = null;
        List<DimIndicator3Entity> dimIndicator3EntityList = selectionService.getIndicator().getData();
        try {
            region = new String(region.getBytes("ISO-8859-1"), "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        try {
            uploadDirPath = request.getSession().getServletContext().getRealPath("/upload/");
            dataDirPath = request.getSession().getServletContext().getRealPath("/datafile/");
            UnitDictonary.Initialize(dataDirPath +File.separator + "Unit_Dictionary.xlsx");
            DataReader reader = new DataReader(uploadDirPath + File.separator +fileName, 1, 100, 2, true);
            List<ExcelInputDataFormat> excelInputDataFormatList = reader.getYearBookIndexListNew();

            //Put the preprocessed data and the orginal data into map
            Map<String, String> orgIndexMap = new HashMap<String, String>();
            for (ExcelInputDataFormat excelInputDataFormat : excelInputDataFormatList) {
                orgIndexMap.put(StringUtils.yearbookIndexPreprocess(excelInputDataFormat.getYBIndex().replaceAll(" ", "")), excelInputDataFormat.getYBIndex().replaceAll(" ", ""));
            }
            ret = searchForAllIndex(region, dimIndicator3EntityList, excelInputDataFormatList, orgIndexMap);

        }
        catch (IllegalStateException e){
            e.printStackTrace();
            System.out.println("1");
            return "fail";
        }
        catch (Throwable e) {
            e.printStackTrace();
            System.out.println("2");
            return "fail";
        }

        return selectionDataConverToJson(ret).toString();
    }

    private List<SelectionDataFormat> searchForAllIndex(String region, List<DimIndicator3Entity> dimIndicator3EntityList, List<ExcelInputDataFormat> excelInputDataFormatList, Map<String, String> orgIndexMap) throws Exception {
        List<SelectionDataFormat> ret = new ArrayList<SelectionDataFormat>();
        SelectionDataFormat selectionDataFormat;
        String DBUnit;
        Directory indexDirectory = IndexDirectoryGenerator.createIndexDirectory(new ArrayList<String>(orgIndexMap.keySet()));

        String historyFilePath = dataDirPath + File.separator + "Result_Set.xlsx";
        ResultSetReader resultSetReader = new ResultSetReader();
        List<ResultSetElement> historyResult = resultSetReader.getHistoryResultSet(historyFilePath, region);
        //     for(int i = 0; i != 30; i++) {
        for(int i = 0; i != dimIndicator3EntityList.size(); ++i){
            String DBIndex = dimIndicator3EntityList.get(i).getIndexName().trim();
            if(dimIndicator3EntityList.get(i).getUnit() != null)
                DBUnit = dimIndicator3EntityList.get(i).getUnit().trim();
            else
                DBUnit = "NULL";
            CoreIndexSearcher coreIndexSearcher = new CoreIndexSearcher(indexDirectory, region, DBIndex, historyResult);
            List<String> result = coreIndexSearcher.getTopYearbookIndex();
            // System.out.println(dimIndicator3EntityList.get(i).getYBIndex());
            Map<String, String> map =new LinkedHashMap<String, String>();
            for(String str: result){
                map.put(orgIndexMap.get(str), searchFromInputDataModel(excelInputDataFormatList,orgIndexMap.get(str)).getUnit());
            }
            SearchResultSorter sorter = new SearchResultSorter();
            map = sorter.sortSelectionFormatByUnit(map, DBUnit);
            selectionDataFormat = new SelectionDataFormat();
            selectionDataFormat.setDatabaseIndex(dimIndicator3EntityList.get(i).getIndexName());
            selectionDataFormat.setDatabaseUnit(dimIndicator3EntityList.get(i).getUnit());
            selectionDataFormat.setYearbookIndexUnitMap(map);

            System.out.println(dimIndicator3EntityList.get(i).getIndexName());
            for(String str: map.keySet())
                System.out.print(str + "," + map.get(str) + " | ");
            System.out.println();
            System.out.println("------");
            ret.add(selectionDataFormat);
        }
        return ret;
    }

    private ExcelInputDataFormat searchFromInputDataModel(List<ExcelInputDataFormat> excelInputDataFormatList, String yearbookIndex){
        for(ExcelInputDataFormat item: excelInputDataFormatList){
            if(item.getYBIndex().equals(yearbookIndex))
                return item;
        }
        return new ExcelInputDataFormat();
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
