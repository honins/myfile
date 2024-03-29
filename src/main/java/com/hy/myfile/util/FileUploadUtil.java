package com.hy.myfile.util;

import com.hy.myfile.config.WebConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by hy on 2019/10/16 13:18
 */
public class FileUploadUtil {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = WebConfig.getBasePath();

    /**
     * 默认文件类型jpg
     */
    public static final String IMAGE_JPG_EXTENSION = ".jpg";

    private static int counter = 0;

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtil.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    /**
     * 根据文件路径上传
     *
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String uploadPicture(MultipartFile file) throws IOException {
        String baseDir = WebConfig.getBasePath();
        try {
            return upload(baseDir, file, FileUploadUtil.IMAGE_JPG_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static String uploadWithPath(String path, MultipartFile file) throws IOException {
        String baseDir = WebConfig.getBasePath();
        try {
            return uploadWithPath(baseDir, path, file);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private static String uploadWithPath(String baseDir, String path, MultipartFile file) throws IOException {

        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtil.DEFAULT_FILE_NAME_LENGTH) {
            // 此处可进行异常处理throws
            System.out.println("文件名长度超出限定长度");
        }

        assertAllowed(file);

        String fileName = path;

        File desc = getAbsoluteFile(baseDir + fileName);
        file.transferTo(desc);
        return fileName;
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String uploadText(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, ".txt");
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir   相对应用的基目录
     * @param file      上传的文件
     * @param extension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static final String  upload(String baseDir, MultipartFile file, String extension) throws IOException{

        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtil.DEFAULT_FILE_NAME_LENGTH) {
            // 此处可进行异常处理throws
            System.out.println("文件名长度超出限定长度");
        }

        assertAllowed(file);

        String fileName = extractFilename(file, extension);

        File desc = getAbsoluteFile(baseDir + fileName);
        file.transferTo(desc);
        return fileName;
    }


    public static final String extractFilename(MultipartFile file, String extension) {
        String filename = file.getOriginalFilename();
        filename = MyDateUtil.getDatePath() + "/" + encodingFilename(filename) + extension;
        return filename;
    }

    private static final File getAbsoluteFile(String filename) throws IOException {
        File desc = new File(File.separator + filename);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String filename) {
        filename = filename.replace("_", " ");
        filename = Md5Util.hash(filename + System.nanoTime() + counter++);
        return filename;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     */
    public static final void assertAllowed(MultipartFile file) {//throws FileSizeLimitExceededException {
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {
            // 此处可进行异常处理throws
            System.out.println("文件大小超出最大限定大小");
        }
    }

    /**
     * 获得完整的url
     * @param path 文件路径
     * @return
     */
    public static String getUrl(String path){
        return WebConfig.getBaseHost()+File.separator+path;
    }

}
