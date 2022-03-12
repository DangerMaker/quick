package com.hundsun.zjfae.common.user;


import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.common.user
 * @ClassName:      CacheConfiguration
 * @Description:    缓存配置
 * @Author:         moran
 * @CreateDate:     2019/8/8 15:26
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/8/8 15:26
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
@Entity
public class CacheConfiguration {


    @Id
    long cid;

    public String type;

    public String content;

    public String resTime = "0";




    public static void putData(CacheConfiguration configuration){
        Box<CacheConfiguration> configurationBox = ObjectBox.getBoxStore().boxFor(CacheConfiguration.class);

        List<CacheConfiguration> configurationList = configurationBox.query().equal(CacheConfiguration_.type,configuration.type).build().find();


        if (configurationList.isEmpty()){
            configurationBox.put(configuration);
        }
        else {
            for (CacheConfiguration cacheConfiguration : configurationList){
                cacheConfiguration.type = configuration.type;
                cacheConfiguration.content= configuration.content;
                cacheConfiguration.resTime = configuration.resTime;
                configurationBox.put(cacheConfiguration);
            }
        }



    }


    public static List<CacheConfiguration> getCacheConfig(String type){
        Box<CacheConfiguration> configurationBox = ObjectBox.getBoxStore().boxFor(CacheConfiguration.class);
        List<CacheConfiguration> configurationList = configurationBox.query().equal(CacheConfiguration_.type,type).build().find();
        return configurationList;
    }


    public static void deleteData(){
        Box<CacheConfiguration> configurationBox = ObjectBox.getBoxStore().boxFor(CacheConfiguration.class);
        configurationBox.removeAll();
    }






}
