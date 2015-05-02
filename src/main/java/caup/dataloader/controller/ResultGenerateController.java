package caup.dataloader.controller;

import caup.dataloader.searcher.model.SelectionResultFormat;
import caup.dataloader.datafile.processer.DataWriter;
import caup.dataloader.learner.ResultSetWriter;
import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2015/03/31 .
 */
@Controller
@RequestMapping(value = "/generateResult")
public class ResultGenerateController {

    @RequestMapping(value = "/writeResult", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String generateResult( @RequestBody String selectedResult, @RequestParam("fileName") String fileName, @RequestParam("region") String region, HttpServletRequest request,
                                  HttpServletResponse response){

        try {
            region = new String(region.getBytes("ISO-8859-1"), "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        System.out.println(selectedResult);


        DataWriter dataWriter = new DataWriter();
        String inputFileNamePath = request.getSession().getServletContext().getRealPath("/upload/") + File.separator + fileName;
        String outputFileName = dataWriter.writeResultEXCELNew(selectedResult, inputFileNamePath);
         String historyFilePath =  request.getSession().getServletContext().getRealPath("/datafile/") + File.separator + "Result_Set.xlsx";
       //Test: Save the selected result
        ResultSetWriter resultSetWriter = new ResultSetWriter();
        resultSetWriter.writeResultSet(historyFilePath, region, getResultFormat(selectedResult));
      //  ResultSetReader reader = new ResultSetReader();
      //  List<ResultSetElement> result = reader.getHistoryResultSet(historyFilePath, region);
        //~Test
        return "{\"name\":\"" + outputFileName + "\"}";
       // return selectedResult.toString();
    }

    private List<SelectionResultFormat> getResultFormat(String jsonStr) {
        List<SelectionResultFormat> ret = new ArrayList<SelectionResultFormat>();
        JSONArray jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i != jsonArray.length(); ++i) {
            ret.add(new SelectionResultFormat(jsonArray.getJSONObject(i).getString("DBIndex"), jsonArray.getJSONObject(i).getString("DBUnit"), jsonArray.getJSONObject(i).getString("YBIndex"), jsonArray.getJSONObject(i).getString("YBUnit")));
        }
        return ret;
    }

}
