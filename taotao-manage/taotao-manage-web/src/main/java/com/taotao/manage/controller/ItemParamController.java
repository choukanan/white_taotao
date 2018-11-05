package com.taotao.manage.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController {
    
    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品类目id查询模板
     * @param itemCatId
     * @return
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemParam> queryItemParamByItemParamId(@PathVariable("itemCatId") Long itemCatId){
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        try {
             return ResponseEntity.ok(this.itemParamService.queryOne(itemParam));
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 根据商品规格参数据模板增加
     * @param itemCatId
     * @return
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ItemParam> queryItemParamByItemParamId(@PathVariable("itemCatId") Long itemCatId,
            @RequestParam("paramData")String paramData){
        ItemParam itemParam = new ItemParam();
        itemParam.setId(null);
        itemParam.setItemCatId(itemCatId);
        itemParam.setParamData(paramData);
        itemParam.setCreated(new Date());
        itemParam.setUpdated(itemParam.getCreated());
        this.itemParamService.save(itemParam);
        try {
            
            return ResponseEntity.status(HttpStatus.CREATED).body(itemParam);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
   /**
    * 查询模板列表
    * @param page
    * @param rows
    * @return
    */
   @RequestMapping(method=RequestMethod.GET)
   @ResponseBody
   public ResponseEntity<EasyUIResult> queryItemParamList(@RequestParam("page") Integer page,@RequestParam("rows") Integer rows){
       PageInfo<ItemParam> pageInfo = this.itemParamService.queryItemParamList(page, rows);
       try {
        return ResponseEntity.status(HttpStatus.OK).body(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
    } catch (Exception e) {
        e.printStackTrace();
    }
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
   }
}
