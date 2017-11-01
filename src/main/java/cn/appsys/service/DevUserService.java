package cn.appsys.service;

import java.util.Map;

import cn.appsys.pojo.DevUser;

public interface DevUserService {
	/**
	 * 开发用户登录
	 * 
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	public DevUser findLoginUser(Map<String, Object> map);
}
