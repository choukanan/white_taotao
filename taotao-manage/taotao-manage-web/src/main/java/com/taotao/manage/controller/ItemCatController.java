package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

//http://localhost:8081/rest/item/cat
@RequestMapping("item/cat")
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    
    private static final ObjectMapper MAPPER =new ObjectMapper();

    /**
     * rest风格查询商品类目
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ItemCat>> queryAll(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        List<ItemCat> list = this.itemCatService.queryListByWhere(itemCat);
        try {
            if (list != null) {
                // 200
                return ResponseEntity.status(HttpStatus.OK).body(list);
            } else {
                // 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    

    
    /**
     * 首页菜单显示
     * @return
     */
    
    @RequestMapping(value="all",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> queryAllToTree(@RequestParam("callback") String callback) {
        ItemCatResult itemCatResult =  this.itemCatService.queryAllToTree();
        try {
            String str = MAPPER.writeValueAsString(itemCatResult);
            //200
            return ResponseEntity.ok(callback+"("+str+");");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
