package kroryi.pro15;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletFileUpload;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

@WebServlet("/upload.do")
public class FileUpload extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CHARSET = "UTF-8";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doHandle(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doHandle(request, response);
    }

    private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding(CHARSET);
        Charset charset = Charset.forName(CHARSET);
        ////////////////
        ServletContext sc = request.getServletContext();
        Properties properties = new Properties();
        properties.load(new FileReader(sc.getRealPath(sc.getInitParameter("contextConfigLocation"))));

        Path currentDirPath = Paths.get((String) properties.get("upload.directory"));
        ////////////////
        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        final JakartaServletFileUpload fileUpload = new JakartaServletFileUpload(factory);
        final List items;
        try {
            items = fileUpload.parseRequest(request);
            for (Object item : items) {
                FileItem fileItem = (FileItem) item;
                if (fileItem.isFormField()) {
                    System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(charset));
                } else {
                    System.out.println("파라미터명:" + fileItem.getFieldName());
                    System.out.println("파일명:" + fileItem.getName());
                    System.out.println("파일크기:" + fileItem.getSize() + "bytes");

                    if (fileItem.getSize() > 0) {
                        int idx = fileItem.getName().lastIndexOf("/");
                        if (idx == -1) {
                            idx = fileItem.getName().lastIndexOf("/");
                        }
                        String fileName = fileItem.getName().substring(idx + 1);
/////////////////////////////
                        long currentTime = System.currentTimeMillis();
                        File uploadFile = new File(currentDirPath + "/" + currentTime+"_" +fileName);
////////////////////////////
                        Path path = Path.of(uploadFile.getPath());
                        fileItem.write(path);
                    } // end if
                } // end if
            } // end for

            // 게시판 내용 작성자, 제목, 내용, -> DB등록 아랫부분에 넣어여야 합니다.
            // 디비등록시 파일명 필드에 파일명 만 넣어시고 RealPath "/home/work/IdeaProject/pro15/upload" 이것은 디비에 넣지 마세요.

        } catch (FileUploadException e) {
            throw new ServletException(e);
        }


    }

}