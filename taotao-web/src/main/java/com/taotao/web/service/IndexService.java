package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.Content;

@Service
public class IndexService {
    @Autowired
    private ApiService apiService;
    
    @Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;
    
    @Value("${INDEXAD1_TAOTAO_URL}")
    private String INDEXAD1_TAOTAO_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String REDIS_INDEXAD1="TAOTAO_WEB_INDEX_INDEXAD1";
    private static final Integer REDIS_INDEXAD1_TIME=60*60*24;
    
    @Autowired
    private RedisService redisService;

    public String getIndexAd1() {
        try {
            
            String jsonData = this.redisService.get(REDIS_INDEXAD1);
            if (StringUtils.isNoneBlank(jsonData)) {
                return jsonData;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        String url = MANAGE_TAOTAO_URL+INDEXAD1_TAOTAO_URL;
        
        try {
            String jsonData = apiService.doGet(url);
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
            @SuppressWarnings("unchecked")
            List<Content> contents = (List<Content>)easyUIResult.getRows();
            
            //封装前台需要的jsop数据
            List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
            for (Content content : contents) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("srcB", content.getPic());
                map.put("height", 240);
                map.put("alt", content.getTitle());
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 550);
                map.put("href", content.getUrl());
                map.put("heightB", 240);
                result.add(map);
            }
            this.redisService.set(REDIS_INDEXAD1, MAPPER.writeValueAsString(result), REDIS_INDEXAD1_TIME);
            
            return MAPPER.writeValueAsString(result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
