package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据parentId查询分类
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ContentCategory>> queryContentCategoryList(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(contentCategory);

        try {
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增分类 
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ContentCategory> saveContentCategory(@RequestParam("parentId") Long parentId,
            @RequestParam("name") String name) {
        
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setId(null);
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setIsParent(false);
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        this.contentCategoryService.save(contentCategory);
        
        //强调，判断父节点的isparent是否为true,如果不是，我们要更新为true
        ContentCategory parentContentCategory = this.contentCategoryService.queryById(parentId);
        if (!parentContentCategory.getIsParent()) {
            parentContentCategory.setIsParent(true);
            parentContentCategory.setUpdated(new Date());
            this.contentCategoryService.updateSelective(parentContentCategory);
        }
        
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     *  重命名
     */
    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory){
        contentCategory.setUpdated(new Date());
        this.contentCategoryService.updateSelective(contentCategory);
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 批量删除
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContentCategoryByIds(ContentCategory contentCategory){
        List<Object> deleteIds = new ArrayList<Object>();
        deleteIds.add(contentCategory.getId());
        //递归查询所有子节点
        findAllSubNode(deleteIds,contentCategory.getId());
        
        //执行批量删除
        this.contentCategoryService.deleteByIds(ContentCategory.class, deleteIds);
        
        //没有子节点时，要设置isparent为false
        ContentCategory param = new ContentCategory();
        param.setParentId(contentCategory.getParentId());
        List<ContentCategory> contentCategorys = this.contentCategoryService.queryListByWhere(param);
        if (contentCategorys.isEmpty()) {
            ContentCategory parent = new ContentCategory();
            parent.setId(contentCategory.getParentId());
            parent.setIsParent(false);
            parent.setUpdated(new Date());
            this.contentCategoryService.updateSelective(parent);
        }
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 递归查询所有子节点
     * @param deleteIds
     * @param id
     */
    private void findAllSubNode(List<Object> deleteIds, Long id) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(id);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(contentCategory);
        for (ContentCategory contentCategory2 : list) {
            deleteIds.add(contentCategory2.getId());
            //开始递归删除
            if (contentCategory2.getIsParent()) {
                findAllSubNode(deleteIds, contentCategory2.getId());
            }
        }
    }
    
    
    
}
