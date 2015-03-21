package caup.dataloader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Richard on 2015/03/21 .
 */
@Controller
@RequestMapping("/index")
public class MainController {
    @RequestMapping(method = RequestMethod.GET)
    public String mainPageDisplay(ModelMap model){
        model.addAttribute("msg", "Main Page");
        return "MainPage";
    }
}
