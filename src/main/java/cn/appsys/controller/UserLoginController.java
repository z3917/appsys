package cn.appsys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.BackendUserService;
import cn.appsys.service.DevUserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping(value = "/manager")
public class UserLoginController {
	private Logger logger = Logger.getLogger(UserLoginController.class);
	@Resource
	private BackendUserService backendUserService;
	
	@RequestMapping(value = "/login")
	public String login() {
		logger.debug("LoginController welcome AppInfoSystem backend==================");
		return "backendlogin";
	}
	/**
	 * 管理员登录
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午11:50:50
	 * @author：Eternal
	 * @param userCode
	 * @param userPassword
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="dologin",method=RequestMethod.POST)
	public String dologin(	@RequestParam(value="userCode",required=false)String userCode,
							@RequestParam(value="userPassword",required=false)String userPassword,
							HttpSession session,
							Model model){
		logger.debug("管理员账号====="+userCode);
		logger.debug("管理员密码====="+userPassword);
		BackendUser backendUser=backendUserService.findLoginUser(userCode);
		if (null!=backendUser) {
			if (backendUser.getUserPassword().equals(userPassword)) {
				if (null!=session.getAttribute(Constants.USER_SESSION)) {
					session.removeAttribute(Constants.USER_SESSION);
				}
				session.setAttribute(Constants.USER_SESSION, backendUser);
				session.setMaxInactiveInterval(600);
				return "redirect:/manager/backend/main";
			}else {
				model.addAttribute("error", "密码不正确!");	
			}
		}else {
			model.addAttribute("error", "该用户不存在!");
		}
		return "backendlogin";
	}
	/**
	 * 管理员主页面
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午11:51:02
	 * @author：Eternal
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/backend/main")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.USER_SESSION) == null){
			return "redirect:/manager/login";
		}
		return "backend/main";
	}
	/**
	 * 退出登录
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午11:51:09
	 * @author：Eternal
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		//清除session
		session.removeAttribute(Constants.USER_SESSION);
		return "backendlogin";
	}
}