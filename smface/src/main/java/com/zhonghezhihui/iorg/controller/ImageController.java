package com.zhonghezhihui.iorg.controller;

import com.zhonghezhihui.iorg.config.GlobalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/image")
public class ImageController {

    protected static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("")
    @ResponseBody
    public void getImage(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value="name", defaultValue = "") String name
    ) {
        try {
            String url= GlobalConstant.IMAGE_PROFILE + name;
            File file = new File(url);
            String l=request.getRealPath("/")+"/"+url;
            String filename = file.getName();
            logger.warn("filename = " + filename);
            String[] split = filename.split("\\.");
            String ext = split[1];
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Length", "" + file.length());
            System.out.println("image/split[1] = " + "image/" + split[1]);
            response.setContentType("image/" + ext);

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

