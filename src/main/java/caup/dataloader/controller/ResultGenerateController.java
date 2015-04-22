package caup.dataloader.controller;

import caup.dataloader.file.io.DataWriter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Richard on 2015/03/31 .
 */
@Controller
@RequestMapping(value = "/generateResult")
public class ResultGenerateController {

    @RequestMapping(value = "/writeResult", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String generateResult( @RequestBody String selectedResult, @RequestParam("fileName") String fileName, HttpServletRequest request,
                                  HttpServletResponse response){

        System.out.println(selectedResult);


        DataWriter dataWriter = new DataWriter();
        String inputFileNamePath = request.getSession().getServletContext().getRealPath("/upload/") + File.separator + fileName;
       // String _outputFileName = StringUtils.downloadFileNameProcess(orgName);
        String outputFileName = dataWriter.writeResultEXCELNew(selectedResult, inputFileNamePath);


         return "{\"name\":\"" + outputFileName + "\"}";
       // return selectedResult.toString();
    }



}
