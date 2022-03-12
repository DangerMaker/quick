package com.hundsun.zjfae.common.user;

import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

//合格投资者资料上传
@Entity
public class AssetProof  {
    @Id(assignable = true)
    public long id;

    public String model;

    public String imagePath;

    public String dynamicKey;

    public String dynamicValue;

    private static final int position = 1;

    //添加/更新数据
    public static void putData(AssetProof assetProof){
        assetProof.id = assetProof.id +position;
        Box<AssetProof> box = ObjectBox.getBoxStore().boxFor(AssetProof.class);
        box.put(assetProof);
    }


    //删除某一个数据
    public static void remove(AssetProof assetProof){
        Box<AssetProof> box = ObjectBox.getBoxStore().boxFor(AssetProof.class);
        box.remove(assetProof);
    }

    //删除全部数据
    public static void removeAll(){
        Box<AssetProof> box = ObjectBox.getBoxStore().boxFor(AssetProof.class);



        box.removeAll();
    }

    //获取AssetProof
    public static AssetProof getAssetProofData(AssetProof assetProof){

        long index = assetProof.id + position;
        Box<AssetProof> box = ObjectBox.getBoxStore().boxFor(AssetProof.class);

        AssetProof proof = box.get(index);
        if (proof == null){

            return new AssetProof();
        }

        return proof;
    }

    public static List<AssetProof> getAssetProofAllData(){

        Box<AssetProof> box = ObjectBox.getBoxStore().boxFor(AssetProof.class);
        return box.getAll();
    }

}
