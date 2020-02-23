package com.hoolai.choosewife;

import com.hoolai.choosewife.entity.RoleInfo;
import com.hoolai.choosewife.util.AllowAnonymous;
import com.hoolai.choosewife.util.HasPermission;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.upload.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@WebServlet("/chooseWife")
public class MyServlet extends BaseServlet {

    private static Log log = LogFactory.getLog(MyServlet.class);

    @AllowAnonymous
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        if (user.equals(ConfigServlet.getValue("userName")) && pass.equals(ConfigServlet.getValue("password"))) {
            HttpSession session = request.getSession();
            session.setAttribute("userName", user);
            session.setAttribute("password", pass);
            return uploadImg(request, response);
        }
        request.setAttribute("login", "用户名或密码错误！");
        return "/index.jsp";
    }

    @AllowAnonymous
    public void findRoleInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 响应参数格式设置
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        int id = Integer.parseInt(request.getParameter("id"));
        if (id <= 0) {
            throw new RuntimeException("参数错误！");
        }

        List<RoleInfo> list = new ArrayList<RoleInfo>();

        Map<String, Object> params = new HashMap<>();
        params.put("type", 100);
        params.put("id", id);
        params.put("timestamp", System.currentTimeMillis());

//        JSONObject object = JSON.parseObject(Http.get("http://meogo api" + "getRoleInfo", params));
//        RoleInfo roleInfo = JSONObject.toJavaObject(object, RoleInfo.class);

        RoleInfo roleInfo1 = new RoleInfo(1, "李云龙", 999, 89.35f, 7.21);
        list.add(roleInfo1);

        response.getWriter().println(JSONArray.fromCollection(list));
    }

    @HasPermission("AdminUser.Upload")
    public String upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                String value = item.getString();
                request.setAttribute(fieldName, value);
            } else {
                String fileName = item.getName();
                int index = fileName.lastIndexOf("\\");
                fileName = fileName.substring(index + 1);
                request.setAttribute("realFileName", fileName);

                File file = new File(ConfigServlet.getValue("filePath") + File.separator + fileName);
                try {
                    item.write(file);
                    log.info(fileName + ",文件上传成功！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return uploadImg(request, response);
    }

    @HasPermission("AdminUser.DeleteImg")
    public String deleteImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fileName = request.getParameter("fileName");
        File file = new File(ConfigServlet.getValue("filePath") + File.separator + fileName);
        if (file.exists()) {
            file.delete();
            log.info(fileName + ",文件已经被成功删除");
        }

        return uploadImg(request, response);
    }

    @HasPermission("AdminUser.Get")
    public String uploadImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> fileNames = getFileList();
        request.setAttribute("fileNames", fileNames);
        request.setAttribute("fileNameSize", fileNames.size());
        return "/upload.jsp";
    }

    /*
     * 读取指定路径下的文件名和目录名
     */
    private List<String> getFileList() {
        File file = new File(ConfigServlet.getValue("filePath"));
        File[] fileList = file.listFiles();
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                if (!String.valueOf(fileList[i].getName().charAt(0)).equals(".")) {
                    fileNames.add(fileList[i].getName());
                }
            }
        }
        return fileNames;
    }

}
