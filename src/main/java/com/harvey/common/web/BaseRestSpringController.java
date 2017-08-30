package com.harvey.common.web;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import com.harvey.common.utils.ConvertRegisterHelper;
import com.harvey.common.utils.PageRequestFactory;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 标准的rest方法列表
 * <pre>
 * /userinfo                => index()
 * /userinfo/new            => _new()  注意: 不使用/userinfo/add => add()的原因是ad会被一些浏览器当做广告URL拦截
 * /userinfo/{id}           => show()
 * /userinfo/{id}/edit      => edit()
 * /userinfo        POST    => create()
 * /userinfo/{id}   PUT     => update()
 * /userinfo/{id}   DELETE  => delete()
 * /userinfo        DELETE  => batchDelete()
 * </pre>
 *  @author badqiu
 */
public class BaseRestSpringController{
    protected static final String CREATED_SUCCESS = "创建成功";
    protected static final String UPDATE_SUCCESS = "更新成功";
    protected static final String DELETE_SUCCESS = "删除成功";

    protected static final String RESULT_STRING = "result";
    protected static final String TOTAL = "total";
    protected static final String RESULT_ACTION = "redirect:/result";

    static {
        //注册converters
        ConvertRegisterHelper.registerConverters();
    }

    public static void copyProperties(Object target,Object source) {
        BeanUtils.copyProperties(target, source);
    }

    public static <T> T copyProperties(Class<T> destClass,Object orig) {
        return BeanUtils.copyProperties(destClass, orig);
    }

    public static ModelMap toModelMap(Page page, PageRequest pageRequest) {
        return toModelMap("",page, pageRequest);
    }

    public static ModelMap toModelMap(String tableId,Page page,PageRequest pageRequest) {
        ModelMap model = new ModelMap();
        saveIntoModelMap(tableId,page,pageRequest,model);
        return model;
    }

    /**
     * 用于一个页面有多个extremeTable是使用
     * @param tableId 等于extremeTable的tableId属性
     */
    public static void saveIntoModelMap(String tableId,Page page,PageRequest pageRequest,ModelMap model){
        Assert.notNull(tableId,"tableId must be not null");
        Assert.notNull(page,"page must be not null");

        model.addAttribute(tableId+"page", page);
        model.addAttribute(tableId+"totalRows", Integer.valueOf(page.getTotalCount()));
        model.addAttribute(tableId+"pageRequest", pageRequest);
        model.addAttribute(tableId+"query", pageRequest);
    }

    public static PageRequest bindPageRequest(HttpServletRequest request,PageRequest pageRequest,String defaultSortColumns){
        return PageRequestFactory.bindPageRequest(pageRequest,request, defaultSortColumns);
    }

    public static <T> T getOrCreateRequestAttribute(HttpServletRequest request, String key,Class<T> clazz) {
        Object value = request.getAttribute(key);
        if(value == null) {
            try {
                value = clazz.newInstance();
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }
            request.setAttribute(key, value);
        }
        return (T)value;
    }

    protected Map<String,String> getParams(HttpServletRequest request){
        Map<String,String> params = new HashMap();
        //获得POST 过来参数设置到新的params中
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = request.getParameterValues(name);
            StringBuffer valueStr =new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                valueStr.append((i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",");
            }
            params.put(name, valueStr.toString());
        }
        return params;
    }

    public void setSuccess(ModelMap model){
        model.put("success","true");
    }

    public void setFailure(ModelMap model){
        model.put("success","false");
    }
}
