package com.google.servlet;

import com.google.mongodb.MongoDbUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

/**
 * Create By
 *
 * @author hemingxi
 * At
 * @date 2018/10/2 21:18
 */
@MultipartConfig(maxFileSize = 1000 * 1024 * 1024,
                    maxRequestSize = 1000 * 1024 * 1024)
@WebServlet(name = "UploadFIleServlet", urlPatterns = {"/UploadFIleServlet"})
public class UploadFIleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");


        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            String filename = part.getSubmittedFileName();
            if (filename == null) {
                continue;
            }
            String fid = MongoDbUtil.uploadFileToGridFS(part.getSubmittedFileName(), part.getInputStream());
            System.out.println("---------------上传完毕，文件id： "+fid);
        }
    }

    public UploadFIleServlet() {
        super();
    }

    @Override
    public void init() {
        System.out.println("servlet init -------------------- now");
    }

    /**
     *根据请求头解析出文件名
     *请求头的格式：火狐和google浏览器下：form-data; name="file"; filename="snmp4j--api.zip"
     *IE浏览器下：form-data; name="file"; filename="E:\snmp4j--api.zip"
     *@param header 请求头
     *@return 文件名
     */
    public String getFileName(String header) {
        /**
         * String[] tempArr1 = header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
         * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
         * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
         */
        String[] tempArr1 = header.split(";");
        /**
         *火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
         *IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
         */
        String[] tempArr2 = tempArr1[2].split("=");
        //获取文件名，兼容各种浏览器的写法
        String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
        return fileName;
    }

}
