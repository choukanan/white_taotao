package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    /**
     * 新添商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("执行新添商品操作，item={},desc={}", item, desc);
            }

            this.itemService.saveItemAndDescAndItemParam(item, desc,itemParams);
            if (LOGGER.isDebugEnabled()) {
                // 201
                LOGGER.debug("执行新添商品操作，item={},desc={}", item, desc);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            LOGGER.error("新添商品失败，item=", item.getId());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 商品列表
     * 
     * @param item
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryItemCatByPage(Item item, @RequestParam("page") Integer page,
            @RequestParam("rows") Integer rows) {
        PageInfo<Item> list = this.itemService.queryPageListByWhere(item, page, rows);
        try {
            if (list != null) {
                // 200
                return ResponseEntity.status(HttpStatus.OK).body(
                        new EasyUIResult(list.getTotal(), list.getList()));
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
     * 更新商品
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updateItemByItemId(Item item, @RequestParam("desc") String desc) {
        try {
            this.itemService.updateItemAndDesc(item, desc);
            // 200
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 通过商品id查询商品数据
     */
    //http://manage.taotao.com/rest/item/1
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Item> queryItemById(@PathVariable("itemId") Long id){
        try {
            Item item = this.itemService.queryById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
}
