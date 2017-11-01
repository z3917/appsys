package cn.appsys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.DevUserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping(value = "/dev")
public class DevLoginController {
	private Logger logger = Logger.getLogger(DevLoginController.class);

	@Resource
	private DevUserService devUserService;
	/**
	 * 进入开发者登录页面
	 * 
	 *
	 * 创建时间： 2017年10月28日 上午8:54:35
	 * @author：Eternal
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login() {
		logger.debug("LoginController welcome AppInfoSystem develpor==================");
		return "devlogin";
	}
	/**
	 * 开发者登录
	 * 
	 *
	 * 创建时间： 2017年10月28日 上午8:51:19
	 * @author：Eternal
	 * @param devCode
	 * @param devPassword
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="dologin",method=RequestMethod.POST)
	public String dologin(	@RequestParam(value="devCode",required=false)String devCode,
							@RequestParam(value="devPassword",required=false)String devPassword,
							HttpSession session,
							Model model){
		Map<String, Object> map=new HashMap<String, Object>();
		logger.debug("开发者账号====="+devCode);
		logger.debug("开发者密码====="+devPassword);
		map.put("devCode", devCode);
		DevUser devUser=devUserService.findLoginUser(map);
		if (null!=devUser) {
			if (devUser.getDevPassword().equals(devPassword)) {
				if (null!=session.getAttribute(Constants.DEV_USER_SESSION)) {
					session.removeAttribute(Constants.DEV_USER_SESSION);
				}
				session.setAttribute(Constants.DEV_USER_SESSION, devUser);
				session.setMaxInactiveInterval(600);
				return "redirect:/dev/flatform/main";
			}else {
				model.addAttribute("error", "密码不正确!");	
			}
		}else {
			model.addAttribute("error", "该用户不存在!");
		}
		return "devlogin";
	}
	/**
	 * 开发者主页面
	 * 
	 *
	 * 创建时间： 2017年10月28日 上午8:51:41
	 * @author：Eternal
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/flatform/main")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.DEV_USER_SESSION) == null){
			return "redirect:/dev/login";
		}
		return "developer/main";
	}
	/**
	 * 退出登录
	 * 
	 *
	 * 创建时间： 2017年10月28日 上午10:16:43
	 * @author：Eternal
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		//清除session
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "devlogin";
	}
}
