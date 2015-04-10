package caup.dataloader.controller;

import caup.dataloader.service.SelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
