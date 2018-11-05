package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.web.bean.Item;

@Service
public class ItemService {
    
    @Autowired
    private ApiService apiService;
    
    @Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;
    
    private static final ObjectMapper MAPPER= new ObjectMapper();
    
    public Item getItemById(Long itemId) {
        String url=MANAGE_TAOTAO_URL +"/rest/item/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (jsonData==null) {
                return null;
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
