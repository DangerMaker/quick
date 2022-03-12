package com.hundsun.zjfae.common.user;

import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ChangeCard {

    @Id(assignable = true)
    public long id;

    public String model;

    public String imagePath;

    public String dynamicKey;

    public String dynamicValue;

    private static final int position = 1;




    //添加/更新数据
    public static void putData(ChangeCard card){
        card.id = card.id +position;
        Box<ChangeCard> box = ObjectBox.getBoxStore().boxFor(ChangeCard.class);
        box.put(card);
    }


    //获取UnBindCard
    public static ChangeCard getChangeCardData(ChangeCard card){

        long index = card.id + position;
        Box<ChangeCard> box = ObjectBox.getBoxStore().boxFor(ChangeCard.class);

        ChangeCard changeCard = box.get(index);
        if (changeCard == null){

            return new ChangeCard();
        }

        return changeCard;
    }

    public static List<ChangeCard> getChangeCardAllData(){

        Box<ChangeCard> box = ObjectBox.getBoxStore().boxFor(ChangeCard.class);
        return box.getAll();
    }



    //删除某一个数据
    public static void remove(ChangeCard changeCard){
        Box<ChangeCard> box = ObjectBox.getBoxStore().boxFor(ChangeCard.class);
        box.remove(changeCard);
    }

    //删除全部数据
    public static void removeAll(){
        Box<ChangeCard> box = ObjectBox.getBoxStore().boxFor(ChangeCard.class);
        box.removeAll();
    }


    public static  <T> void remove( T t){
        Box<T> box = (Box<T>) ObjectBox.getBoxStore().boxFor(t.getClass());
        box.removeAll();
    }


}
