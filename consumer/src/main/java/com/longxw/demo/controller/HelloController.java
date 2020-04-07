package com.longxw.demo.controller;

import com.longxw.fdfs.FastDFSClient;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author longxw
 * @since 2019/12/10
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class HelloController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request){
        String id = "group1/M00/00/00/wKgQv15nBBWAUjyBAAAHd27bCgY442.txt";
        try {
            byte[] b = FastDFSClient.download(id);
            log.info(new String(b, Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
