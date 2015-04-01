package caup.dataloader.controller;

import caup.dataloader.transformer.reader.DataModel.InputDataFormat;
import caup.dataloader.transformer.reader.DataModel.SelectionDataFormat;
import caup.dataloader.transformer.reader.DataModel.SelectionResultFormat;
import caup.dataloader.transformer.reader.DataReader;
import caup.dataloader.transformer.reader.DataWriter;
import caup.dataloader.util.StringUtils;
import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 2015/03/31 .
 */
@Controller
@RequestMapping(value = "/generateResult")
public class ResultGenerateController {

    @RequestMapping(value = "/writeResult", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    //public String generateResult(@RequestBody List<SelectionResultFormat> selectedResult){
    public String generateResult( @RequestBody String selectedResult, @RequestParam("fileName") String fileName, HttpServletRequest request,
                                  HttpServletResponse response){

        System.out.println(selectedResult);


        DataWriter dataWriter = new DataWriter();
        String outputFileName = dataWriter.writeResultEXCEL(selectedResult, request.getSession().getServletContext().getRealPath("/upload/") + File.separator + fileName);


        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return outputFileName;
       // return selectedResult.toString();
    }



}
