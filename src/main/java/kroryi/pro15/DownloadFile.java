package kroryi.pro15;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@WebServlet("/DownloadFile.do")
public class DownloadFile extends HttpServlet {
    private static final String CHARSET = "UTF-8";
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doHandle(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doHandle(request, response);
    }

    public void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding(CHARSET);

        ServletContext sc = request.getServletContext();
        Properties properties = new Properties();
        properties.load(new FileReader(sc.getRealPath(sc.getInitParameter("contextConfigLocation"))));
        Path currentDirPath = Paths.get((String) properties.get("upload.directory"));

        String filePath = currentDirPath+"/"+request.getParameter("fileName");
        System.out.println(filePath);
        File downloadFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        String relativePath = getServletContext().getRealPath("");
        ServletContext context = getServletContext();
        String mimeType = context.getMimeType(filePath);
        if(mimeType == null){
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type:" + mimeType);

        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        String headerkey = "Context-Disposition";

        String encodedFileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");

        String headerValue = String.format("attachment; filename=\"%s\"", encodedFileName);
        response.setHeader(headerkey, headerValue);
        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while((bytesRead = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();

    }
}
