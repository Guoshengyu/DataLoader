package caup.dataloader.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Richard on 2015/04/01 .
 */
@Controller
@RequestMapping(value = "/file")
public class FileIOController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartHttpServletRequest request, HttpServletResponse response){

        Iterator<String> iter = request.getFileNames();

        MultipartFile mpf = request.getFile(iter.next());
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH mm ss");
        String realPath = request.getSession().getServletContext().getRealPath("/upload/");
        String oldFileName = mpf.getOriginalFilename();
        String newFileName = oldFileName.substring(0, oldFileName.indexOf(".")) + "-" + format.format(currentTime) + oldFileName.substring(oldFileName.indexOf("."));
        try {
            mpf.transferTo(new File(realPath + "/" + newFileName));
          //  FileUtils.copyInputStreamToFile(mpf.getInputStream(), new File(realPath, newFileName));
            System.out.println("fjdsf");
        }catch (Exception e){
            e.printStackTrace();
        }
        return realPath + newFileName;
    }
}
