package com.hoolai.choosewife;

import com.hoolai.choosewife.util.AllowAnonymous;
import com.hoolai.choosewife.util.HasPermission;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");//处理响应编码
        String methodName = request.getParameter("method");
        if (methodName == null || methodName.trim().isEmpty()) {
            throw new RuntimeException("您没有传递method参数! 无法确定您要调用的方法！");
        }
        Method method = null;
        try {
            method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("调用的方法:" + methodName + "不存在");
        }

        try {
            AllowAnonymous allowAnonymous = method.getAnnotation(AllowAnonymous.class);
            if (allowAnonymous == null) {
                HttpSession session = request.getSession();
                if (!check(session)) {
                    request.setAttribute("login", "请登录！");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }
                HasPermission hasPermission = method.getAnnotation(HasPermission.class);
                if (hasPermission == null) {
                    request.setAttribute("login", "请登录！");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }
            }

            String result = (String) method.invoke(this, request, response);
            System.out.println("result:" + result);
            /**
             * 处理从继承这个类的类中返回的字符串（重定向和转发）
             *     return "r:/index.jsp"; 和 return "f:/index.jsp";
             *      返回的是字符串，需要解读字符串
             */
            if (result == null || result.trim().isEmpty()) {
                return;
            }
            if (result.contains(":")) {
                /*
                 * 先获取冒号位置，然后截取前缀（操作，是重定向还是转发）和后缀（路径）
                 */
                int index = result.indexOf(":");
                String operate = result.substring(0, index);
                String path = result.substring(index + 1);
                /*
                 * 进行处理，如果是r重定向，如果是f则转发
                 */
                if (operate.equalsIgnoreCase("r")) {
                    response.sendRedirect(request.getContextPath() + path);
                } else if (operate.equalsIgnoreCase("f")) {
                    request.getRequestDispatcher(path).forward(request, response);
                } else {
                    throw new RuntimeException("您指定的操作" + operate +
                            "不支持，请正确填写：r和f");
                }
            } else {
                request.getRequestDispatcher(result).forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("您要调用的方法" + methodName + ",它内部抛出了异常");
            e.printStackTrace();
        }
    }

    private boolean check(HttpSession session) {
        String username = (String) session.getAttribute("userName");
        String password = (String) session.getAttribute("password");
        if ((username != null && username.equals(ConfigServlet.getValue("userName"))) ||
                (password != null && password.equals(ConfigServlet.getValue("password")))) {
            return true;
        }
        return false;
    }


}
