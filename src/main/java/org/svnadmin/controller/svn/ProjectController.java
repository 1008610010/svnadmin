package org.svnadmin.controller.svn;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.svnadmin.common.annotation.AdminAuthPassport;
import org.svnadmin.common.entity.PageBean;
import org.svnadmin.common.web.BaseController;
import org.svnadmin.entity.Pj;
import org.svnadmin.entity.Usr;
import org.svnadmin.service.CommonService;
import org.svnadmin.service.PjService;
import org.svnadmin.util.SessionUtils;

/**
 * SVN项目管理控制器
 * @author Zoro
 * @datetime 2016/1/20 19:48
 * @since 1.0.0
 */
@Controller
@RequestMapping("/")
public class ProjectController extends BaseController {

    @Autowired
    private CommonService commonService;
    @Autowired
    private PjService pjService;

    /**
     * 项目列表
     * @param session
     * @return
     */
    @RequestMapping(value = "pjList", method = RequestMethod.GET)
    public String pjList(HttpSession session, ModelMap map) {
        boolean hasAdminRight = SessionUtils.hasSuperAdminRight(session);
        List<Pj> list = null;
        if (hasAdminRight) {
            list = pjService.list();// 所有项目
        } else {
            list = pjService.list(SessionUtils.getLogedUser(session).getUsr());// 登录用户可以看到的项目
        }
        PageBean<Pj> pageBean = new PageBean<Pj>();
        pageBean.setRecordCount(list.size());
        pageBean.setDataList(list);
        map.put("pageBean", pageBean);
        return "svn/pj_list";
    }

    /**
     * 项目列表数据
     * @param session
     * @return
     */
    @RequestMapping(value = "pjList", method = RequestMethod.GET ,params = "action=data")
    @ResponseBody
    public Object pjListDataSet(HttpSession session,@RequestParam("pageNumber")int pageNumber) {
        PageBean<Usr> pageBean = new PageBean<Usr>(pageNumber,10);
        return pageBean;
    }
    
    @RequestMapping(value = "getPj", method = RequestMethod.GET)
    @ResponseBody
    public Pj getPj(HttpSession session, @RequestParam("pjId")int pjId) {
    	Pj pj = pjService.get(pjId);
        return pj;
    }

    /**
     * 创建项目
     * @param session
     * @return
     */
    @AdminAuthPassport
    @RequestMapping(value = "pjCreate", method = RequestMethod.GET)
    public String pjCreate(HttpSession session, ModelMap map) {
    	String basePath = commonService.getConfig("DB2_SVNADMIN_SVN_REPOS");
    	map.put("SVN_BASE_PATH", basePath);
    	
        return "svn/pj_create";
    }

    /**
     * 创建项目处理
     * @param session
     * @return
     */
    @AdminAuthPassport
    @RequestMapping(value = "pjCreateHandler", method = RequestMethod.POST)
    @ResponseBody
    public Object pjCreateHandler(HttpSession session,Pj entity) {
        try {
            pjService.save(entity);
            return pushMsg("创建项目成功", true , "url" , "pjList");
        }catch (Exception e){
            logger.error("创建项目提交失败",e);
            return pushMsg("创建项目失败，"+e.getMessage(), true);
        }
    }

    @AdminAuthPassport
    @RequestMapping(value = "pjDelete", method = RequestMethod.POST)
    @ResponseBody
    public Object pjDelete(HttpSession session, @RequestParam("pj")Integer pj, ModelMap map) {
    	try {
            pjService.delete(pj);
            return pushMsg("项目删除成功", true , "url" , "pjList");
        }catch (Exception e){
            logger.error("项目删除失败",e);
            return pushMsg("项目删除失败，" + e.getMessage(), true);
        }
    }

}
