package cn.appsys.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.DevUserMapper;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.DevUserService;

@Service
public class DevUserServiceImpl implements DevUserService {
	@Resource
	private DevUserMapper devUsermapper;

	@Override
	public DevUser findLoginUser(Map<String, Object> map) {
		return devUsermapper.findLoginUser(map);
	}

}
