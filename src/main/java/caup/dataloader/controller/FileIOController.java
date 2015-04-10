package caup.dataloader.controller;

import caup.dataloader.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    public String upload(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> iter = request.getFileNames();

        MultipartFile mpf = request.getFile(iter.next());
        String realPath = request.getSession().getServletContext().getRealPath("/upload/");
        String oldFileName = mpf.getOriginalFilename();
        String newFileName = StringUtils.uploadFileNameProcess(oldFileName);
        try {
            mpf.transferTo(new File(realPath + File.separator + newFileName));
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
        return newFileName;
    }

    @RequestMapping(value = "/download")
    @ResponseBody
    public void download(@RequestParam("fileName") String fileName, HttpServletRequest request,
                         HttpServletResponse response) throws Exception {

          String realPath = request.getSession().getServletContext().getRealPath("/upload/");
        String newFileName = StringUtils.downloadFileNameProcess(fileName);

        File attachFile = new File(realPath + File.separator + newFileName);
        InputStream is = new FileInputStream(attachFile);

        // set file as attached data and copy file data to response output stream
        response.setHeader("Content-Disposition", "attachment; filename=" + newFileName);
        FileCopyUtils.copy(is, response.getOutputStream());

        // delete file on server file system
        //attachFile.delete();

        // close stream and return to view
        response.flushBuffer();

    }
}
