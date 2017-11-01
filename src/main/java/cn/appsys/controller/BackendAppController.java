package cn.appsys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.AppCategoryService;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppVersionService;
import cn.appsys.service.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("/manager/backend/app")
public class BackendAppController {
	private Logger logger = Logger.getLogger(DevLoginController.class);
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;
	/**
	 * 分页查询App列表
	 * 
	 *
	 * 创建时间： 2017年10月28日 上午10:34:54
	 * @author：Eternal
	 * @param querySoftwareName 	软件名称
	 * @param queryStatus			APP状态
	 * @param queryFlatformId		所属平台
	 * @param queryCategoryLevel1	一级分类
	 * @param queryCategoryLevel2	二级分类
	 * @param queryCategoryLevel3	三级分类
	 * @param pageIndex				当前页码
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String findAppInfoList(	@RequestParam(value="querySoftwareName",required=false)String querySoftwareName,
									@RequestParam(value="queryStatus",required=false)String queryStatus,
									@RequestParam(value="queryFlatformId",required=false)String queryFlatformId,
									@RequestParam(value="queryCategoryLevel1",required=false)String queryCategoryLevel1,
									@RequestParam(value="queryCategoryLevel2",required=false)String queryCategoryLevel2,
									@RequestParam(value="queryCategoryLevel3",required=false)String queryCategoryLevel3,
									@RequestParam(value="pageIndex",required=false)String pageIndex,
									Model model
			){
		logger.debug("findAppInfoList===========分页查询");
		logger.debug("querySoftwareName==========="+querySoftwareName);
		logger.debug("queryStatus==========="+queryStatus);
		logger.debug("queryFlatformId==========="+queryFlatformId);
		logger.debug("queryCategoryLevel1==========="+queryCategoryLevel1);
		logger.debug("queryCategoryLevel2==========="+queryCategoryLevel2);
		logger.debug("queryCategoryLevel3==========="+queryCategoryLevel3);
		logger.debug("pageIndex==========="+pageIndex);
		Map<String, Object> map=new HashMap<String, Object>();
		//当前页码
		Integer index=1;
		if (null!=pageIndex) {
			index=Integer.parseInt(pageIndex);
		}
		if (null!=querySoftwareName&&!"".equals(querySoftwareName)) {
			map.put("querySoftwareName", querySoftwareName);
		}
		if (null!=queryStatus&&!"".equals(queryStatus)) {
			map.put("queryStatus", Integer.parseInt(queryStatus));
		}
		if (null!=queryFlatformId&&!"".equals(queryFlatformId)) {
			map.put("queryFlatformId", Integer.parseInt(queryFlatformId));
		}
		if (null!=queryCategoryLevel1&&!"".equals(queryCategoryLevel1)) {
			map.put("queryCategoryLevel1", Integer.parseInt(queryCategoryLevel1));
		}
		if (null!=queryCategoryLevel2&&!"".equals(queryCategoryLevel2)) {
			map.put("queryCategoryLevel2", Integer.parseInt(queryCategoryLevel2));
		}
		if (null!=queryCategoryLevel3&&!"".equals(queryCategoryLevel3)) {
			map.put("queryCategoryLevel3", Integer.parseInt(queryCategoryLevel3));
		}
		map.put("pageIndex", (index-1)*Constants.pageSize);
		map.put("size", Constants.pageSize);
		map.put("queryStatus", 1);
		
		PageSupport pages=new PageSupport();
		pages.setCurrentPageNo(index);
		pages.setPageSize(Constants.pageSize);
		pages.setTotalCount(appInfoService.findAppInfoCount(map));
		//app状态
		List<DataDictionary> statusList=this.findDataDictionary("APP_STATUS"); 
		//所属平台
		List<DataDictionary> flatFormList=this.findDataDictionary("APP_FLATFORM");
		//一级分类
		List<AppCategory> categoryLevel1List=this.findAppCategoryList(null);
		//二级分类
		if (null!=queryCategoryLevel1&&!"".equals(queryCategoryLevel1)) {
			final Integer CategoryLevel1=Integer.parseInt(queryCategoryLevel1);
			List<AppCategory> categoryLevel2List=this.findAppCategoryList(CategoryLevel1);
			model.addAttribute("categoryLevel2List",categoryLevel2List);
		}
		//三级分类
		if (null!=queryCategoryLevel2&&!"".equals(queryCategoryLevel2)) {
			final Integer CategoryLevel2=Integer.parseInt(queryCategoryLevel2);
			List<AppCategory> categoryLevel3List=this.findAppCategoryList(CategoryLevel2);
			model.addAttribute("categoryLevel3List",categoryLevel3List);
		}
		//app分页查询
		List<AppInfo> appInfoList=appInfoService.findAppInfoList(map);
		
		model.addAttribute("pages",pages);
		model.addAttribute("appInfoList",appInfoList);
		model.addAttribute("statusList",statusList);
		model.addAttribute("flatFormList",flatFormList);
		model.addAttribute("categoryLevel1List",categoryLevel1List);
		model.addAttribute("querySoftwareName",querySoftwareName);
		model.addAttribute("queryStatus",queryStatus);
		model.addAttribute("queryFlatformId",queryFlatformId);
		model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
		return "backend/applist";
	}
	/**
	 * 下拉框联动
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午8:56:35
	 * @author：Eternal
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="/categorylevellist.json")
	@ResponseBody
	public Object categorylevellist(@RequestParam("pid") String pid){
		logger.debug("categorylevellist===========级联查询");
		logger.debug("pid==========="+pid);
		JSONArray array=new JSONArray();
		List<AppCategory> categorylevellist=this.findAppCategoryList(pid);
		for (AppCategory appCategory : categorylevellist) {
			array.add(appCategory);
		}
		return array;
	}
	/**
	 * 根据Id查询分类
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午9:27:38
	 * @author：Eternal
	 * @param typeCode
	 * @return
	 */
	public List<AppCategory> findAppCategoryList(Object pid){
		Map<String,Object> map=new  HashMap<String, Object>();
		if (null!=pid&&!"".equals(pid)) {
			map.put("parentId", pid);
		}else {
			map.put("parentId", null);
		}
		List<AppCategory> categorylevellist=appCategoryService.findAppCategoryList(map);
		return categorylevellist;
	}
	/**
	 * 根据Id查询字典
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午9:27:38
	 * @author：Eternal
	 * @param typeCode
	 * @return
	 */
	public List<DataDictionary> findDataDictionary(String typeCode){
		Map<String,Object> map=new  HashMap<String, Object>();
		map.put("typeCode", typeCode);
		List<DataDictionary> dataDictionaryList=dataDictionaryService.finDataDictionaryList(map);
		return dataDictionaryList;
	}
	/**
	 * 所属平台
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午9:22:36
	 * @author：Eternal
	 * @param tcode
	 * @return
	 */
	@RequestMapping(value="/datadictionarylist.json")
	@ResponseBody
	public Object datadictionarylist(@RequestParam("tcode") String tcode){
		logger.debug("categorylevellist===========加载所属平台");
		logger.debug("tcode==========="+tcode);
		JSONArray array=new JSONArray();
		List<DataDictionary> dataDictionaryList=this.findDataDictionary(tcode);
		for (DataDictionary dataDictionary : dataDictionaryList) {
			array.add(dataDictionary);
		}
		return array;
	}
	/**
	 * 进入审核页面
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午11:50:21
	 * @author：Eternal
	 * @param aid
	 * @param vid
	 * @param model
	 * @return
	 */
	@RequestMapping("check")
	public String check(@RequestParam(value="aid",required=false)String aid,
								@RequestParam(value="vid",required=false)String vid,
								Model model ){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", aid);
		map.put("aid", aid);
		map.put("vid", vid);
		AppInfo appInfo=appInfoService.findAppInfoList(map).get(0);
		AppVersion appVersion=appVersionService.findAppVersionList(map).get(0);
		model.addAttribute(appInfo);
		model.addAttribute(appVersion);
		return "backend/appcheck";
	}
	/**
	 * 审核结果
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午11:53:46
	 * @author：Eternal
	 * @param appInfo
	 * @return
	 */
	@RequestMapping(value="/checksave",method=RequestMethod.POST)
	public String checkSave(AppInfo appInfo){
		try {
			if(appInfoService.updateSatus(appInfo.getStatus(),appInfo.getId())==1){
				return "redirect:/manager/backend/app/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backend/appcheck";
	}
}

