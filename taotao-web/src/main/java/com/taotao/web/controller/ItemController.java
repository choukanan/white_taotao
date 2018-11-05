package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.Item;
import com.taotao.web.service.ItemService;

@Controller
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @RequestMapping(value="item/{itemId}",method=RequestMethod.GET)
    public ModelAndView showInfo(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("item");
        Item item = this.itemService.getItemById(itemId);
        mv.addObject("item", item);
        return mv;
    }
    
}
