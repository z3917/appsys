package cn.appsys.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.BackendUserMapper;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.BackendUserService;
@Service
public class BackendUserServiceImpl implements BackendUserService{
	@Resource
	private BackendUserMapper backendUserMapper;
	
	@Override
	public BackendUser findLoginUser(String userCode) {
		return backendUserMapper.findLoginUser(userCode);
	}

}

