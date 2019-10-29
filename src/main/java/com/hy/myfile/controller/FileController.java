package com.hy.myfile.controller;

import com.hy.myfile.common.CommonResult;
import com.hy.myfile.config.WebConfig;
import com.hy.myfile.util.FileUploadUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by hy on 2019/10/16 13:39
 * @author hy
 */
@RestController
public class FileController {

    Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 保存文件
     *
     * @param file 文件
     * @return 结果
     */
    @PostMapping("/uploadFile")
    public Object uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        String fileName = null;
        String url = null;
        try {
            if (!file.isEmpty()) {
                fileName = FileUploadUtil.uploadText(WebConfig.getBasePath(), file);
                url = FileUploadUtil.getUrl(fileName);
                logger.info("uploadFile fileName:{}",fileName);
                // TODO: 此处可以根据项目需求的业务进行操作，例如将fileName保存到数据库
            }
            return CommonResult.success(url);
        } catch (Exception e) {
            return CommonResult.failed("failed");
        }
    }

    /**
     * 保存图片
     *
     * @param file 头像文件
     * @return 结果
     */
    @PostMapping("/uploadPic")
    public Object uploadPic(
            @RequestParam("picFile") MultipartFile file
    ) {
        String fileName = null;
        String url = null;
        try {
            if (!file.isEmpty()) {
                fileName = FileUploadUtil.uploadPicture(file);
                url = FileUploadUtil.getUrl(fileName);
                logger.info("uploadPic url:{}",url);
                // TODO: 此处可以根据项目需求的业务进行操作，例如此处操作的是头像，可以将头像url保存到数据库，用户登录后，获取相应的url获取头像图片。
            }
            return CommonResult.success(url);
        } catch (Exception e) {
            return CommonResult.failed("failed");
        }
    }


    /**
     * 保存图片
     *
     * @param file 头像文件
     * @return 结果
     */
    @PostMapping("/uploadFileWithPath")
    public Object uploadPicWithPath(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path
    ) {
        String fileName = null;
        String url = null;
        try {
            logger.info("saveing... {}", path);
            if (!file.isEmpty()) {
                fileName = FileUploadUtil.uploadWithPath(path, file);
                url = FileUploadUtil.getUrl(fileName);
                logger.info("uploadFileWithPath url:{}",url);
                // TODO: 此处可以根据项目需求的业务进行操作，例如此处操作的是头像，可以将头像url保存到数据库，用户登录后，获取相应的url获取头像图片。
            }
            return CommonResult.success(url);
        } catch (Exception e) {
            return CommonResult.failed("failed");
        }
    }

    /**
     * 查看图片
     *
     * @param url 图片url
     * @param response 请求响应
     */
    @RequestMapping(value = "/readPic",method = RequestMethod.GET)
    @ResponseBody
    public void readPic(
            @RequestParam String url, HttpServletResponse response
    ) {
        File file = new File(url);
        // 后缀名
        String suffixName = url.substring(url.lastIndexOf("."));
        //判断文件是否存在如果不存在就返回默认图片或者进行异常处理
        if (!(file.exists() && file.canRead())) {
//            file = new File("xxx/xxx.jpg");
            logger.error("文件不存在 {}",url);
            return;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            inputStream.close();
            response.setContentType("image/png;charset=utf-8");
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件，以String返回
     * @param url
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/readFileToString",method = RequestMethod.GET)
    @ResponseBody
    public String readFileToString(
            @RequestParam String url, HttpServletRequest request, HttpServletResponse response
    ) {

        // 通过url创建文件
        File file = new File(url);
        // 后缀名
        String suffixName = url.substring(url.lastIndexOf("."));

        //判断文件是否存在如果不存在就进行异常处理
        if (!(file.exists() && file.canRead())) {
            logger.error("文件不存在 {}",url);
        }

        FileInputStream inputStream = null;
        String content = "";
        try {
            inputStream = new FileInputStream(file);
            inputStream.close();
            // 使用FileUtils将File内容以UTF-8的编码写到String里
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 下载文件
     *
     * @param url 文件url
     * @param response 请求响应
     */
    @RequestMapping(value = "/downloadFile",method =RequestMethod.GET)
    @ResponseBody
    public void downloadFile(
            @RequestParam String url, HttpServletResponse response
    ) {
        File file = new File(url);
        // 后缀名
        String suffixName = url.substring(url.lastIndexOf("."));
        //判断文件是否存在如果不存在就进行异常处理
        if (!(file.exists() && file.canRead())) {
            logger.error("文件不存在 {}",url);
        }
        FileInputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            int length = inputStream.read(data);
            inputStream.close();
            response.setContentType("application/force-download");
            //通过设置头信息给文件命名，也即是，在前端，文件流被接受完还原成原文件的时候会以你传递的文件名来命名
            response.addHeader("Content-Disposition",String.format("attachment; filename=\"%s\"", file.getName()));
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
