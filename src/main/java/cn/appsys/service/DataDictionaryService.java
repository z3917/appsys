package cn.appsys.service;

import java.util.List;
import java.util.Map;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	List<DataDictionary> finDataDictionaryList(Map<String, Object> map);
}

