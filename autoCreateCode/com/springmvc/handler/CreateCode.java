package com.springmvc.handler;

import com.springmvc.codeCreate.CodeHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/30.
 */
@Controller
@RequestMapping("/createCode")
public class CreateCode {

    CodeHelper codeHelper = new CodeHelper();
    @RequestMapping("/index")
    public String index(Map<String,Object> map){
        List<String> tables = codeHelper.getAllTableName();
        map.put("tables",tables);
        return "index";
    }

    @RequestMapping("/create")
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tableName = request.getParameter("tableName");
        String savePath = request.getParameter("savePth");
        //tableName = StringCommon.initialsUpperCase(tableName);
        codeHelper.createBean(tableName,savePath);
        codeHelper.createDao(tableName,savePath);
        codeHelper.createService(tableName,savePath);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.flush();
        printWriter.println("<script>");
        printWriter.println("alert('生成成功！');window.location='/createCode/index'");
        printWriter.println("</script>");
    }
}
