package com.hundsun.zjfae.common.user;

import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;


//解绑卡数据存储
@Entity
public class UnBindCard {

    @Id(assignable = true)
    public long id;

    public String model;

    public String imagePath;

    public String dynamicKey;

    public String dynamicValue;

    private static final int position = 1;



    //添加/更新数据
    public static void putData(UnBindCard unBindCard){
        unBindCard.id = unBindCard.id +position;
        Box<UnBindCard> box = ObjectBox.getBoxStore().boxFor(UnBindCard.class);
        box.put(unBindCard);
    }


    //获取UnBindCard
    public static UnBindCard getUnBindCardData(UnBindCard unBindCard){

        long index = unBindCard.id + position;
        Box<UnBindCard> box = ObjectBox.getBoxStore().boxFor(UnBindCard.class);

        UnBindCard bindCard = box.get(index);
        if (bindCard == null){

            return new UnBindCard();
        }

        return bindCard;
    }

    public static List<UnBindCard> getUnBindCardAllData(){

        Box<UnBindCard> box = ObjectBox.getBoxStore().boxFor(UnBindCard.class);
        return box.getAll();
    }



    //删除某一个数据
    public static void remove(UnBindCard unBindCard){
        Box<UnBindCard> box = ObjectBox.getBoxStore().boxFor(UnBindCard.class);
        box.remove(unBindCard);
    }

    //删除全部数据
    public static void removeAll(){
        Box<UnBindCard> box = ObjectBox.getBoxStore().boxFor(UnBindCard.class);
        box.removeAll();
    }



}
