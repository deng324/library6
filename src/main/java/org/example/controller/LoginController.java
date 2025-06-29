package org.example.controller;

import org.example.entity.Admin;
import org.example.entity.Sysadmin;
import org.example.entity.User;
import org.example.form.LoginForm;
import org.example.result.LoginResult;
import org.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    public String login(LoginForm loginForm, Model model, HttpServletRequest request){
        LoginResult result = this.loginService.login(loginForm);
        String url = "";
        ModelAndView modelAndView = new ModelAndView();
        if(result.getCode().equals(-1) || result.getCode().equals(-2)){
            url = "/login";
            model.addAttribute("msg", result.getMsg());
        }
        if(result.getCode().equals(0)){
            HttpSession session = request.getSession();  //将数据存入session
            switch (loginForm.getType()){
                case 1:
                    session.setAttribute("user", (User)result.getObject());
                    url = "redirect:/user/index";
                    break;
                case 2:
                    session.setAttribute("admin", (Admin)result.getObject());
                    url = "redirect:/admin/index";
                    break;
                case 3:
                    session.setAttribute("sysadmin", (Sysadmin)result.getObject());
                    url = "redirect:/sysadmin/index";
                    break;
            }

        }
        return url;
    }

}
