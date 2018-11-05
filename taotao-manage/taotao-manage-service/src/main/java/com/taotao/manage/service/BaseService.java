package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {
    
    @Autowired
    private Mapper<T> mapper;

    /**
     * 查询所有数据
     * 
     * @return
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }

    /**
     * 根据主键查询数据
     * 
     * @param id
     * @return
     */
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 条件查询
     * @param param
     * @return
     */
    public List<T> queryListByWhere(T param) {
        return this.mapper.select(param);
    }
    
    /**
     * 根据自定义的查询条件对象查询列表
     * 
     * @return
     */
    public List<T> queryByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    /**
     * 查询记录数
     * 
     * @param param
     * @return
     */
    public Integer queryCount(T param) {
        return this.mapper.selectCount(param);
    }

    /**
     * 分页查询
     * @param param
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T param,Integer page,Integer rows){
        PageHelper.startPage(page, rows);
        List<T> list = this.queryListByWhere(param);
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        return pageInfo;
    }
    
    /**
     * 查询一条记录
     * @return
     */
    public T queryOne(T t){
        return this.mapper.selectOne(t);
    }
    
    /**
     * 插入一条记录
     * @param param
     * @return
     */
    public Integer  save(T t){
        if (t.getCreated() == null) {
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        } else if (t.getUpdated() == null) {
            t.setUpdated(t.getCreated());
        }
        return this.mapper.insert(t);
    }
    /**
     * 插入指定字段内容
     * @param param
     * @return
     */
    public Integer saveSelective(T t){
        if (t.getCreated() == null) {
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        } else if (t.getUpdated() == null) {
            t.setUpdated(t.getCreated());
        }
        return this.mapper.insertSelective(t);
    }
    /**
     * 更新
     * @param param
     * @return
     */
    public Integer  update(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }
    /**
     * 更新指定的字段
     * @param param
     * @return
     */
    public Integer updateSelective(T param){
        param.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(param);
    }
    /**
     * 根据主键id删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 批量删除
     * @param clazz
     * @param list
     * @return
     */
    public Integer deleteByIds(Class<?> clazz,List<Object> list){
        Example example = new Example(clazz);
        //把list放进删除列表
        example.createCriteria().andIn("id", list);
        return this.mapper.deleteByExample(example);
    }
}
