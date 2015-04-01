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

    @RequestMapping()
    public String mainPageDisplay(ModelMap model) {
        model.addAttribute("msg", "Main Page");

  //  getLuceneSeachResult();

        return "MainPage";
    }


}
