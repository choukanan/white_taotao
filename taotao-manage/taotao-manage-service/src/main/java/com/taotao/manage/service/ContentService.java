package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content>{
    @Autowired
    private ContentMapper contentMapper;

    public PageInfo<Content> queryPageListByWhere(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Content.class);
        example.createCriteria().andEqualTo("categoryId", categoryId);
        example.setOrderByClause("updated DESC");
        List<Content> list = this.contentMapper.selectByExample(example);
        return new PageInfo<Content>(list);
    }

}
