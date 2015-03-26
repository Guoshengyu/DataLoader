package caup.dataloader.controller;

import caup.dataloader.entity.DimIndicator3Entity;
import caup.dataloader.service.SelectionService;
import caup.dataloader.transformer.reader.CoreIndexSearcher;
import caup.dataloader.transformer.reader.DataModel.InputDataFormat;
import caup.dataloader.transformer.reader.DataModel.SelectionDataFormat;
import caup.dataloader.transformer.reader.DataReader;
import caup.dataloader.transformer.reader.LuceneTest;
import caup.dataloader.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 2015/03/21 .
 */
@Controller
@RequestMapping("/index")
public class MainController {

    @Autowired
    SelectionService selectionService;

    @RequestMapping(method = RequestMethod.GET)
    public String mainPageDisplay(ModelMap model) {
        model.addAttribute("msg", "Main Page");

getLuceneSeachResult();

        return "MainPage";
    }

    public List<SelectionDataFormat> getLuceneSeachResult(){
        List<SelectionDataFormat> ret = new ArrayList<SelectionDataFormat>();
        SelectionDataFormat selectionDataFormat = null;
        List<DimIndicator3Entity> dimIndicator3EntityList = selectionService.getIndicator().getData();
        try {
            DataReader reader = new DataReader("D:\\IntelliJWorkspace\\DataLoader\\src\\main\\webapp\\datafile\\Jiaxing-shiqu-Test.xlsx", 1, 100, 2, true);
            List<InputDataFormat> inputDataFormatList = reader.getYearBookIndexList();

            //Put the preprocessed data and the orginal data into map
            Map<String, String> orgIndexMap = new HashMap<String, String>();
            for (InputDataFormat inputDataFormat : inputDataFormatList) {
                orgIndexMap.put(StringUtils.yearbookIndexPreprocess(inputDataFormat.getIndexName().replaceAll(" ", "")), inputDataFormat.getIndexName().replaceAll(" ", ""));
            }
            searchForAllIndex(ret, dimIndicator3EntityList, inputDataFormatList, orgIndexMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private void searchForAllIndex(List<SelectionDataFormat> ret, List<DimIndicator3Entity> dimIndicator3EntityList, List<InputDataFormat> inputDataFormatList, Map<String, String> orgIndexMap) throws Exception {
        SelectionDataFormat selectionDataFormat;
        for(int i = 0; i != 30; i++) {

            CoreIndexSearcher coreIndexSearcher = new CoreIndexSearcher(dimIndicator3EntityList.get(i).getIndexName(), new ArrayList<String>(orgIndexMap.keySet()));
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
}
