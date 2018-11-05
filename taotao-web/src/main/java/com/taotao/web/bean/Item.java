package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.taotao.manage.pojo.Item{

    public String[] getImages(){
        if (null == super.getImage()) {
            return null;
        }
        return StringUtils.split(super.getImage(), ",");
    }
}
