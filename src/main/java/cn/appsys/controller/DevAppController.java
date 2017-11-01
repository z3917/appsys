package cn.appsys.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.appsys.dao.AppVersionMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.AppCategoryService;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppVersionService;
import cn.appsys.service.DataDictionaryService;
import cn.appsys.service.DevUserService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("dev/flatform/app")
public class DevAppController {
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
		return "developer/appinfolist";
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
	 * 验证APKName是否已存在
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午9:46:35
	 * @author：Eternal
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value="/apkexist.json")
	@ResponseBody
	public Object apkexist(@RequestParam("APKName") String APKName){
		logger.debug("categorylevellist===========验证APKName是否已存在");
		logger.debug("APKName==========="+APKName);
		JSONObject array=new JSONObject();
		if (null!=APKName&&!"".equals(APKName)) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("APKName", APKName);
			if (appInfoService.findAppInfoCount(map)!=0) {
				array.put("APKName", "exist");
			}else{
				array.put("APKName", "noexist");
			}
		}else {
			array.put("APKName", "empty");
		}
		return array;
	}
	/**
	 * 进入app保存页面
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午9:14:20
	 * @author：Eternal
	 * @param appInfo
	 * @return
	 */
	@RequestMapping(value="/appinfoadd")
	public String appinfoadd(@ModelAttribute AppInfo appInfo){
		return "developer/appinfoadd";
	}
	/**
	 * 新增app信息
	 * 
	 *
	 * 创建时间： 2017年10月30日 上午9:14:36
	 * @author：Eternal
	 * @param appInfo
	 * @return
	 */
	@RequestMapping(value="/appinfoaddsave")
	public String appinfoaddsave(	AppInfo appInfo,
									@RequestParam("a_logoPicPath") MultipartFile attach,
									HttpServletRequest request,
									HttpSession session,
									Model model){
		//相对路径
		String logoPicPath =  null;
		//绝对路径
		String logoLocPath =  null;
		//判断文件是否存在---存在保存
		if (!attach.isEmpty()) {
			//定义上传文件的路径
			String path=request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			//获取原文件名
			String oldFileName=attach.getOriginalFilename();
			//获取文件的后缀
			String suffix=FilenameUtils.getExtension(oldFileName);
			//定义上传文件的最高大小
			int filesize=500000;
			if (attach.getSize()>filesize) {
				model.addAttribute("fileUploadError",Constants.FILEUPLOAD_ERROR_4);
				return "developer/appinfoadd";
			}else if (suffix.equalsIgnoreCase("jpg")||
					suffix.equalsIgnoreCase("png")||
					suffix.equalsIgnoreCase("jpeg")||
					suffix.equalsIgnoreCase("pneg")) {
				//上传LOGO图片命名:apk名称.apk
				String fileName=appInfo.getAPKName()+".jpg";
				File file=new File(path,fileName);
				//如果文件不存在就创建
				if (!file.exists()) {
					file.mkdirs();
				}
				try {
					attach.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
					return "developer/appinfoadd";
				}
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
				 logger.info("logoPicPath===========" + logoPicPath);
				 logger.info("logoLocPath===========" + logoLocPath);
			}else{
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		
		Integer result=appInfoService.addAppInfo(appInfo);
		if (result==1) {
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appinfoadd";
	}
	/**
	 * 进入app修改页面
	 * 
	 * 修改保存时文件上传错误
	 *
	 * 创建时间： 2017年10月30日 下午12:51:21
	 * @author：Eternal
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appinfomodify")
	public String appinfomodify(@RequestParam("id") String id,Model model,@RequestParam(value="error",required= false)String fileUploadError){
		Map<String, Object> map=new HashMap<String, Object>();
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}else if(null != fileUploadError && fileUploadError.equals("error4")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_4;
		}
		map.put("id", id);
		map.put("pageIndex", 0);
		map.put("size", 5);
		AppInfo appInfo=appInfoService.findAppInfoList(map).get(0);
		logger.debug(appInfo.toString());
		model.addAttribute("appInfo", appInfo);
		model.addAttribute("fileUploadError",fileUploadError);
		return "developer/appinfomodify";
	}
	/**
	 * 删除文件
	 * 
	 *
	 * 创建时间： 2017年10月30日 下午4:33:10
	 * @author：Eternal
	 * @param id
	 * @param flag
	 * @return
	 */
	@RequestMapping(value="/delfile.json")
	@ResponseBody
	public Object delfile(@RequestParam(value="id",required=false) String id,@RequestParam(value="flag",required=false) String flag){
		JSONObject result=new JSONObject();
		if (null!=id&&!"".equals(id)&&null!=flag&&!"".equals(flag)) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("pageIndex", 0);
			map.put("size", 5);
			String filePath=null;
			if (flag.equals("logo")) {
				map.put("id", id);
				filePath=appInfoService.findAppInfoList(map).get(0).getLogoLocPath();
				File file=new File(filePath);
				if (file.exists()) {
					if(appInfoService.deleteAppLogo(Integer.parseInt(id))==1){//更新表
						if (file.delete()) {
							result.put("result", "success");
						 }
					}
				}
			}else if (flag.equals("apk")) {
				map.put("vid", id);
				filePath=appVersionService.findAppVersionList(map).get(0).getApkLocPath();
				System.out.println("*******************************"+filePath);
				File file=new File(filePath);
				if (file.exists()) {
					if(appVersionService.deleteApk(Integer.parseInt(id))==1){//更新表
						if (file.delete()) {
							result.put("result", "success");
						 }
					}
				}
			}
		
		}else{
			result.put("result", "failed");
		}
		return result;
	}
	/**
	 * 保存修改
	 * 
	 *
	 * 创建时间： 2017年10月30日 下午4:33:41
	 * @author：Eternal
	 * @param appInfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
	public String modifySave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
							@RequestParam(value="attach",required= false) MultipartFile attach){		
		String logoPicPath =  null;
		String logoLocPath =  null;
		String APKName = appInfo.getAPKName();
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){//上传大小不得超过 50k
            	 return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
						 +"&error=error4";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
				 String fileName = APKName + ".jpg";//上传LOGO图片命名:apk名称.apk
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
							+"&error=error2";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
            }else{
            	return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
						 +"&error=error3";
            }
		}
		appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		try {
			if(appInfoService.modify(appInfo)==1){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}
	/**
	 * 进入新增版本页面
	 * 
	 * 先查询该App的所有版本
	 * 
	 *
	 * 创建时间： 2017年10月31日 上午8:59:50
	 * @author：Eternal
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("appversionadd")
	public String appversionadd(Model model,
								@RequestParam("id") String id,
								@ModelAttribute AppVersion appVersion,
								@RequestParam(value="error",required= false)String fileUploadError){
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
		}
		Map<String, Object> map=new  HashMap<String, Object>();
		map.put("aid", id);
		appVersion.setAppId(Integer.parseInt(id));
		List<AppVersion> appVersionList=appVersionService.findAppVersionList(map);
		model.addAttribute(appVersionList);
		model.addAttribute("fileUploadError", fileUploadError);
		return "developer/appversionadd";
	}
	/**
	 * 新增版本保存
	 * 
	 *
	 * 创建时间： 2017年10月31日 下午1:50:14
	 * @author：Eternal
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping("addversionsave")
	public String addversionsave(AppVersion appVersion,
								HttpSession session,
								HttpServletRequest request,
								@RequestParam(value="a_downloadLink",required=false) MultipartFile attach){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", appVersion.getAppId());
		map.put("pageIndex", 0);
		map.put("size", 5);
		String downloadLink=null;
		String apkFileName=null;
		String apkLocPath=null;
		System.out.println(attach.getOriginalFilename());
		if (!attach.isEmpty()) {
			String path=session.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName=attach.getOriginalFilename();
			String suffix=FilenameUtils.getExtension(oldFileName);
			if (suffix.equalsIgnoreCase("apk")) {
				String APKName=appInfoService.findAppInfoList(map).get(0).getAPKName();
				logger.debug("=============="+APKName);
				if (null!=APKName&&!"".equals(APKName)) {
					apkFileName=APKName+"-"+appVersion.getVersionNo()+".apk";
					File file=new File(path,apkFileName);
					if(!file.exists()){
						file.mkdirs();
					 }
					try {
						attach.transferTo(file);
					} catch (Exception e) {
						e.printStackTrace();
						return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error2";
					}
					apkLocPath=path+File.separator+apkFileName;
					downloadLink=request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
					
				}else {
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error1";	
				}
			}else {
				return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setDownloadLink(downloadLink);
		if(appVersionService.addappVersion(appVersion)==1){
			return "redirect:/dev/flatform/app/list";
		}
		return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error1";
	}
	/**
	 * 进入修改版本页面
	 * 
	 *
	 * 创建时间： 2017年10月31日 下午4:50:14
	 * @author：Eternal
	 * @param model
	 * @param vid
	 * @param aid
	 * @return
	 */
	@RequestMapping("/appversionmodify")
	public String appversionmodify(Model model,
									@RequestParam("vid")String vid,
									@RequestParam("aid")String aid,
									@RequestParam(value="error",required=false)String error ){
		Map<String, Object> map=new  HashMap<String, Object>();
		map.put("aid", aid);
		List<AppVersion> appVersionList=appVersionService.findAppVersionList(map);
		map.put("vid", vid);
		AppVersion appVersion=appVersionService.findAppVersionList(map).get(0);
		if (error!=null&&error.equals("error1")) {
			model.addAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_1);
		}else if (error!=null&&error.equals("error2")) {
			model.addAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
		}else if (error!=null&&error.equals("error3")) {
			model.addAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
		}else if (error!=null&&error.equals("error4")) {
			model.addAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute("appVersion", appVersion);
		return "developer/appversionmodify";
		
	}
	/**
	 * 修改版本信息
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午8:50:32
	 * @author：Eternal
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping("appversionmodifysave")
	public String appversionmodifysave(AppVersion appVersion,
										HttpSession session,
										HttpServletRequest request,	
										@RequestParam(value="attach",required=false)MultipartFile attach){
		String apkFileName=null;
		String apkLocPath=null;
		String downloadLink=null;
		String APKName=null;
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", appVersion.getAppId());
		map.put("pageIndex", 0);
		map.put("size", 5);
		if (!attach.isEmpty()) {
			String path=session.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName=attach.getOriginalFilename();
			String suffix=FilenameUtils.getExtension(oldFileName);
			Integer size=500000000;
			if (attach.getSize()>size) {
				return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error4";
			}else {
				if (suffix.equalsIgnoreCase("apk")) {
					APKName=appInfoService.findAppInfoList(map).get(0).getAPKName();
					if (null!=APKName&&!"".equals(APKName)) {
						apkFileName=APKName+"-"+appVersion.getVersionNo()+".apk";
						File file=new File(path,apkFileName);
						if (!file.exists()) {
							file.mkdirs();
						}
						try {
							attach.transferTo(file);
						} catch (Exception e) {
							e.printStackTrace();
							return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error2";
						}
						apkLocPath=path+File.separator+apkFileName;
						downloadLink=request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
					}else {
						return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error1";
					}
				}else {
					return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error3";
				}
			}
		}
		appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setApkFileName(apkFileName);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setDownloadLink(downloadLink);
		if (appVersionService.updateAppVersion(appVersion)==1) {
			return "redirect:/dev/flatform/app/list";
		}
		return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()+"&aid="+appVersion.getAppId()+"&error=error2";
	}
	/**
	 * 查看App信息 
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午9:17:48
	 * @author：Eternal
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("appview/{id}")
	public String appview(@PathVariable("id")String id,Model model){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("aid", id);
		AppInfo appInfo=appInfoService.findAppInfoList(map).get(0);
		model.addAttribute(appInfo);
		List<AppVersion> appVersionList=appVersionService.findAppVersionList(map);
		model.addAttribute(appVersionList);
		return "developer/appinfoview";
	}
	/**
	 * 删除App及版本信息
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午9:58:08
	 * @author：Eternal
	 * @param id
	 * @return
	 */
	@RequestMapping("delapp.json")
	@ResponseBody
	public Object delapp(@RequestParam("id") String id){
		JSONObject data=new JSONObject();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		if (appInfoService.findAppInfoCount(map)>0) {
			if (appInfoService.deleteApp(id)) {
				data.put("delResult", "true");
			}else {
				data.put("delResult", "false");
			}
		}else {
			data.put("delResult", "notexist");
		}
		return data;
	}
	/**
	 * 上下架
	 * 
	 *
	 * 创建时间： 2017年11月1日 上午10:58:15
	 * @author：Eternal
	 * @param appId
	 * @param session
	 * @return
	 */
	@RequestMapping("{appId}/sale.json")
	@ResponseBody
	public Object sale(@PathVariable String appId,HttpSession session){
		JSONObject result = new JSONObject();
		Integer appIdInteger = 0;
		try{
			appIdInteger = Integer.parseInt(appId);
		}catch(Exception e){
			appIdInteger = 0;
		}
		result.put("errorCode", "0");
		result.put("appId", appId);
		if(appIdInteger>0){
			try {
				DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
				AppInfo appInfo = new AppInfo();
				appInfo.setId(appIdInteger);
				appInfo.setModifyBy(devUser.getId());
				if(appInfoService.updateSaleStatusByAppId(appInfo)){
					result.put("resultMsg", "success");
				}else{
					result.put("resultMsg", "failed");
				}		
			} catch (Exception e) {
				e.printStackTrace();
				result.put("errorCode", "exception000001");
			}
		}else{
			//errorCode:0为正常
			result.put("errorCode", "param000001");
		}
		return result;
	}
}

