package caup.dataloader.controller;

import caup.dataloader.transformer.reader.DataModel.InputDataFormat;
import caup.dataloader.transformer.reader.DataReader;
import caup.dataloader.transformer.reader.LuceneTest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Richard on 2015/03/21 .
 */
@Controller
@RequestMapping("/index")
public class MainController {
    @RequestMapping(method = RequestMethod.GET)
    public String mainPageDisplay(ModelMap model){
        model.addAttribute("msg", "Main Page");

        try {


      //  LuceneTest test = new LuceneTest();
      //  test.test();

        DataReader reader = new DataReader("D:\\IntelliJWorkspace\\DataLoader\\src\\main\\webapp\\datafile\\Jiaxing-shiqu-Test.xlsx", 1, 100, 2, true);
        List<InputDataFormat> inputDataFormat = reader.getYearBookIndexList();
            System.out.println(inputDataFormat);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "MainPage";
    }
}
