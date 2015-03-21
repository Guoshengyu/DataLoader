package caup.dataloader.controller;

import caup.dataloader.service.SelectionService;
import caup.dataloader.entity.DimIndicator3Entity;
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

    @RequestMapping(value = "/getIndicator")
    @ResponseBody
    public String getIndicatorList(){
        List<DimIndicator3Entity> result =  selectionService.getIndicator();
        System.out.println(result);
        return result.toString();
    }
}