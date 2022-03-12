package com.hundsun.zjfae.common.user;


import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.objectbox.Box;
import io.objectbox.Property;
import io.objectbox.query.QueryBuilder;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.common.user
 * @ClassName:      CacheData
 * @Description:     获取APP缓存数据
 * @Author:         moran
 * @CreateDate:     2019/8/12 16:18
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/8/12 16:18
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class CacheData {

    private static CacheData cacheData;


    public static CacheData getInstance() {
        if (cacheData == null){
            synchronized (CacheData.class){
                cacheData = new CacheData();
            }
        }
        return cacheData;
    }

    private CacheData(){

    }


    public <T> List<T> getListCacheData(Class<T> tClass){
        List<T> list = new ArrayList<>();
        Box<T> box = ObjectBox.getBoxStore().boxFor(tClass);
        list.addAll(box.query().build().find());
        return list;
    }


    public <T> T getCacheData(Class<T> tClass){
        Box<T> box = ObjectBox.getBoxStore().boxFor(tClass);
        return box.query().build().findFirst();
    }


    /**
     *多条件查询
     * @param tClass 查询对象
     * @param propertyObjectMap 查询条件集合 key查询条件，value具体值
     * **/
    public <T> List<T> getListEqualCacheData(Class<T > tClass, Map<Property,Object> propertyObjectMap){

        if (propertyObjectMap == null){
            throw new NullPointerException("propertyObjectMap is not null and value is not null.");
        }

        if (propertyObjectMap.isEmpty()){

            throw  new IllegalStateException("propertyObjectMap is not empty and value is not empty.");
        }
        List<T> list = new ArrayList<>();
        Box<T> box = ObjectBox.getBoxStore().boxFor(tClass);
        QueryBuilder queryBuilder = box.query();
        Iterator<Map.Entry<Property,Object>> iterator = propertyObjectMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Property,Object> entry = iterator.next();
            queryBuilder.equal(entry.getKey(),entry.getValue().toString());
        }

        list.addAll(queryBuilder.build().find());
        return list;
    }



    /**
     *多条件模糊查找
     * @param tClass 查询对象
     * @param propertyObjectMap 查询条件集合 key查询条件，value具体值
     * **/
    public <T> List<T> getListContainsCacheData(Class<T > tClass, Map<Property,Object> propertyObjectMap){

        if (propertyObjectMap == null){
            throw new NullPointerException("propertyObjectMap is not null and value is not null.");
        }

        if (propertyObjectMap.isEmpty() ){

            throw  new IllegalStateException("propertyObjectMap is not empty and value is not empty.");
        }

        List<T> list = new ArrayList<>();
        Box<T> box = ObjectBox.getBoxStore().boxFor(tClass);

        QueryBuilder queryBuilder = box.query();
        Iterator<Map.Entry<Property,Object>> iterator = propertyObjectMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Property,Object> entry = iterator.next();
            queryBuilder.contains(entry.getKey(),entry.getValue().toString());
        }

        list.addAll(queryBuilder.build().find());
        return list;
    }





    public <T> void putData(Class<T> clazz,Object value){
        Box<T> box = ObjectBox.getBoxStore().boxFor(clazz);
        box.put((T)value);

    }


    public <T> void deleteData(Class<T> clazz,Object value){
        Box<T> box = ObjectBox.getBoxStore().boxFor(clazz);
        box.remove((T) value);
    }


    public <T> void deleteAllData(Class<T> clazz){
        Box<T> box = ObjectBox.getBoxStore().boxFor(clazz);
        box.removeAll();
    }






}
