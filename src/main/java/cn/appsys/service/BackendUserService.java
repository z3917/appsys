package cn.appsys.service;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

public interface BackendUserService {

	BackendUser findLoginUser(String userCode);
}

