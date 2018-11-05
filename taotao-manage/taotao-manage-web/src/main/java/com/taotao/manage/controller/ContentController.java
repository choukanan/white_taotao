package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@RequestMapping("content")
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 加载内容列表
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryContentList(@RequestParam("categoryId") Long categoryId,
            @RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
        PageInfo<Content> pageInfo = this.contentService.queryPageListByWhere(categoryId, page, rows);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    
    /**
     * 新增内容
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveContent(Content content){
        this.contentService.save(content);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
