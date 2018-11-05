package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;
    
    @Autowired
    private ItemParamItemMapper itemParamItemMapper;

    public void saveItemAndDesc(Item item, String desc) {
       
        //使用同一个事务处理
        item.setId(null);//强制id为null
        item.setCreated(new Date());
        item.setUpdated(item.getCreated());
        this.itemMapper.insertSelective(item);
        
        
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(itemDesc.getCreated());
        this.itemDescMapper.insertSelective(itemDesc);
        
    }

    
    /**
     * 分页查询
     * @param param
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Item> queryPageListByWhere(Item param,Integer page,Integer rows){
        PageHelper.startPage(page, rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        List<Item> list = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(list);
        return pageInfo;
    }


    public void updateItemAndDesc(Item item, String desc) {
       item.setUpdated(new Date());
       this.itemMapper.updateByPrimaryKeySelective(item);
       ItemDesc itemDesc = new ItemDesc();
       itemDesc.setItemId(item.getId());
       itemDesc.setUpdated(item.getUpdated());
       itemDesc.setItemDesc(desc);
       this.itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        
    }
/*
 * 
 * 4.3.	提交商品时实现规格参数的添加功能
 */

    public void saveItemAndDescAndItemParam(Item item, String desc, String itemParams) {
        //使用同一个事务处理
        item.setId(null);//强制id为null
        item.setCreated(new Date());
        item.setUpdated(item.getCreated());
        this.itemMapper.insertSelective(item);
        
        
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(itemDesc.getCreated());
        this.itemDescMapper.insertSelective(itemDesc);
        
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setId(null);
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(itemParamItem.getCreated());
        
        this.itemParamItemMapper.insertSelective(itemParamItem);
        
    }
    
}
