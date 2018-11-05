package com.taotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//http://localhost:8081/rest/page/item-add
//http://localhost:8081/rest/page/item-list
//http://localhost:8081/rest/page/item-param-list
@RequestMapping("page")
@Controller
public class PageController {

    /**
     * 配置通用的跳转
     * @param pageName
     * @return
     */
    @RequestMapping(value="{pageName}",method=RequestMethod.GET)
    public String getPage(@PathVariable("pageName") String pageName){
        return pageName;
    }
}
