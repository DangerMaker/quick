package com.hundsun.zjfae.activity.product.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayBaoInfo implements Parcelable {


    private List<HashMap> playList;//红包信息
    private HashMap<String, Object> playMap;//卡券支付信息


    public String getKqAddRatebj() {
        if (null == kqAddRatebj) {
            return "";
        }
        return kqAddRatebj;
    }

    public void setKqAddRatebj(String kqAddRatebj) {
        this.kqAddRatebj = kqAddRatebj;
    }

    private String kqAddRatebj;//加息券加息金额


    public List<HashMap> getPlayList() {
        return playList;
    }

    public void setPlayList(List<HashMap> playList) {
        this.playList = playList;
    }

    public HashMap<String, Object> getPlayMap() {
        return playMap;
    }

    public void setPlayMap(HashMap<String, Object> playMap) {
        this.playMap = playMap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kqAddRatebj);
        dest.writeList(this.playList);
        dest.writeSerializable(this.playMap);
    }

    public PlayBaoInfo() {
    }

    protected PlayBaoInfo(Parcel in) {
        this.kqAddRatebj = in.readString();
        this.playList = new ArrayList<HashMap>();
        in.readList(this.playList, HashMap.class.getClassLoader());
        this.playMap = (HashMap<String, Object>) in.readSerializable();
    }

    public static final Creator<PlayBaoInfo> CREATOR = new Creator<PlayBaoInfo>() {
        @Override
        public PlayBaoInfo createFromParcel(Parcel source) {
            return new PlayBaoInfo(source);
        }

        @Override
        public PlayBaoInfo[] newArray(int size) {
            return new PlayBaoInfo[size];
        }
    };

    /**
     * 计算红包抵用金额
     * */
    public static String allBaoAmount(List<HashMap> playList) {
        String amount = "0.00";
        if (playList != null && !playList.isEmpty()) {
            for (HashMap hashMap : playList) {
                String value = (String) hashMap.get("value");
                amount = MoneyUtil.moneyAdd(amount, value);

            }
        }
        return amount;
    }




    public static boolean moneyComp(String valueStr, String compValueStr) {


        return MoneyUtil.moneyComp(valueStr, compValueStr);
    }


    public static boolean moneyCompare(String valueStr, String compValueStr) {

        return MoneyUtil.moneyCompare(valueStr, compValueStr);
    }


    public String addBaoListMoney(List<HashMap> playList) {
        String amount = "0.00";
        if (playList != null && !playList.isEmpty()) {
            for (HashMap hashMap : playList) {
                String value = (String) hashMap.get("value");
                amount = MoneyUtil.moneyAdd(amount, value);
            }
        }
        return MoneyUtil.formatMoney(amount);
    }



    public static String addMoney(String valueStr, String addStr){

        return MoneyUtil.moneyAdd(valueStr, addStr);
    }


    /**
     * 计算红包卡券抵用后最终支付的金额
     * */
    public static String allAmount(HashMap<String, Object> playMap, List<HashMap> playList, String total_payAmount) {
        String amount = "0.00";
        if (playMap != null && !playMap.isEmpty()) {
            String type = (String) playMap.get("type");
            if (!type.endsWith("A")) {
                String value = (String) playMap.get("value");
                amount = MoneyUtil.moneyAdd(amount, value);
            }
        }

        if (playList != null && !playList.isEmpty()) {
            for (HashMap hashMap : playList) {
                String value = (String) hashMap.get("value");
                amount = MoneyUtil.moneyAdd(amount, value);
            }
        }
        String amountPlay = MoneyUtil.moneySub(total_payAmount, amount);
        CCLog.e("amountPlay", amountPlay);

        boolean isAmountPlay = moneyComp(amountPlay, "0.00");

        if (isAmountPlay) {

            return amountPlay;
        } else {
            return "0.00";
        }

        //return  MoneyUtil.formatMoney(String.valueOf(amountPlay));
    }



    public static String all_quan_bao_Amount(PlayBaoInfo playBaoInfo){
        String amount = "0.00";

        if (playBaoInfo == null){

            return amount;
        }

        else {
            HashMap<String, Object> playMap = playBaoInfo.getPlayMap();
            List<HashMap> playList = playBaoInfo.getPlayList();


            if (playMap != null && !playMap.isEmpty()) {
                String type = (String) playMap.get("type");
                if (!type.equals("A") ) {
                    String value = (String) playMap.get("value");
                    amount = MoneyUtil.moneyAdd(amount, value);
                }
            }

            if (playList != null && !playList.isEmpty()) {
                for (HashMap hashMap : playList) {
                    String value = (String) hashMap.get("value");
                    amount = MoneyUtil.moneyAdd(amount, value);
                }
            }


            return MoneyUtil.formatMoney(amount);
        }

    }


    public static String moneySub(String valueStr, String minusStr) {
        CCLog.e("valueStr",valueStr);
        CCLog.e("minusStr",minusStr);
        String amountPlay = MoneyUtil.moneySub(valueStr, minusStr);
        boolean isAmountPlay = MoneyUtil.moneyComp(amountPlay, "0.00");

        if (isAmountPlay) {

            return amountPlay;
        } else {
            return "0.00";
        }

    }


    // //计算总支付金额-红包-卡券
    public static String calculate(PlayBaoInfo playBaoInfo, String total_payAmount) {
        String amount = "0.00";

        //当playBaoInfo为空时，用户并未选择红包及卡券
        if (playBaoInfo != null) {
            HashMap<String, Object> playMap = playBaoInfo.getPlayMap();
            List<HashMap> playList = playBaoInfo.getPlayList();
            if (playMap != null && !playMap.isEmpty()) {
                String type = (String) playMap.get("type");
                if (!type.endsWith("A")) {
                    String value = (String) playMap.get("value");
                    amount = MoneyUtil.moneyAdd(amount, value);
                }
            }
            if (playList != null && !playList.isEmpty()) {
                for (HashMap hashMap : playList) {
                    String value = (String) hashMap.get("value");
                    amount = MoneyUtil.moneyAdd(amount, value);
                }
            }
        }

        return moneySub(total_payAmount, amount);
    }

    // //计算总支付金额-红包-卡券-保证金
    public static String calculate(PlayBaoInfo playBaoInfo, String total_payAmount, String freezeDeposit) {
        String amount = "0.00";

        //当playBaoInfo为空时，用户并未选择红包及卡券
        if (playBaoInfo != null) {
            HashMap<String, Object> playMap = playBaoInfo.getPlayMap();
            List<HashMap> playList = playBaoInfo.getPlayList();
            if (playMap != null && !playMap.isEmpty()) {
                String type = (String) playMap.get("type");
                if (!type.endsWith("A")) {
                    String value = (String) playMap.get("value");
                    amount = MoneyUtil.moneyAdd(amount, value);
                }
            }
            if (playList != null && !playList.isEmpty()) {
                for (HashMap hashMap : playList) {
                    String value = (String) hashMap.get("value");
                    amount = MoneyUtil.moneyAdd(amount, value);
                }
            }
        }
        amount = MoneyUtil.moneyAdd(amount, freezeDeposit);
        return moneySub(total_payAmount, amount);
    }
    //拼接所有kqCode红包和卡券code码 type类型已经value

    /**
     * @param playList 红包选择
     * @param playMap  卡券选择
     * @param type     三种类型 type = value 卡券或红包对应的金额,type = type 卡券或红包类型，type = id 卡券或红包id
     */
    public String allType(HashMap<String, Object> playMap, List<HashMap> playList, String type) {
        StringBuffer buffer = new StringBuffer();
        //位置固定，先红包，后卡券
        if (playList != null && !playList.isEmpty()) {
            for (HashMap hashMap : playList) {
                String value = (String) hashMap.get(type);
                buffer.append(value).append("|");
            }
        }
        if (playMap != null && !playMap.isEmpty()) {
            //"A":加息券,"L":抵用券,"F":满减券
            String value = (String) playMap.get(type);
            buffer.append(value);

            CCLog.e(buffer.toString());
        }
        return buffer.toString();
    }


    public String allValue(HashMap<String, Object> playMap, List<HashMap> playList, String type) {
        StringBuffer buffer = new StringBuffer();
        //位置固定，先红包，后卡券
        if (playList != null && !playList.isEmpty()) {
            for (HashMap hashMap : playList) {
                String value = (String) hashMap.get(type);
                DecimalFormat df = new DecimalFormat();
                double valueCode = Double.parseDouble(value);
                String percentage = df.format(valueCode * 100);
                buffer.append(percentage.replaceAll(",", "".trim())).append("|");
            }
        }
        if (playMap != null && !playMap.isEmpty()) {
            //"A":加息券,"L":抵用券,"F":满减券
            String quanType = (String) playMap.get("type");
            if (quanType.endsWith("A")) {
                String value = (String) playMap.get(type);
                buffer.append(value);
            } else {
                String value = (String) playMap.get(type);
                DecimalFormat df = new DecimalFormat();
                double valueCode = Double.parseDouble(value);
                String percentage = df.format(valueCode * 100);
                buffer.append(percentage.replaceAll(",", "".trim()));
            }


            CCLog.e(buffer.toString());
        }
        return buffer.toString();
    }

}


