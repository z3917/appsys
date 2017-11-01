package cn.appsys.dao;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

public interface BackendUserMapper {
	BackendUser findLoginUser(@Param("userCode")String userCode);
}

