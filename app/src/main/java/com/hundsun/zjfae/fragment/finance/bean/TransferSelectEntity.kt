package com.hundsun.zjfae.fragment.finance.bean

import onight.zjfae.afront.AllAzjProto
import onight.zjfae.afront.gens.v4.TransferList


/**
 * ClassName:      TransferSelectEntity
 * Description:    转让筛选起投金额和剩余期限数据封装类
 * Author:         moran
 * CreateDate:     2021/5/13 18:49
 * UpdateUser:     更新者：moran
 * UpdateDate:     2021/5/13 18:49
 * UpdateRemark:   更新说明：
 * Version:        1.0
 */
class TransferSelectEntity {

    /**
     * 起投金额和剩余期限的uuid
     * */
    var amountAndTimeUUid: String = ""

    /**
     * 起投金额筛选条件列表
     * */
    var amountTransferSortList = mutableListOf<ConditionEntity>()

    /**
     * 起投金额选择的条件标记
     * */
    var amountSelectMap: MutableMap<Int, Boolean> = mutableMapOf()

    /**
     * 起投金额选择条件名称
     * */
    var amountSelectConditionName = mutableListOf<String>()

    /**
     * 剩余期限筛选条件列表
     * */
    var timeTransferSortList = mutableListOf<ConditionEntity>()

    /**
     * 剩余期限选择的条件标记
     * */
    var timeSelectMap: MutableMap<Int, Boolean> = mutableMapOf()

    /**
     * 剩余期限选择条件名称
     * */
    var timeSelectConditionName = mutableListOf<String>()

    /**
     * 起投金额 uuids
     * */
    var amountUUids: String = ""

    /**
     * 剩余期限uuid
     * */
    var timeUUids: String = ""

    /**
     * 选中的排序数据封装类
     * */
    var selectEntity: TransferIncomeSelectEntity = TransferIncomeSelectEntity()

    /**
     * 排序选项集合
     * */
    var transferIncomeList: MutableList<TransferIncome>? = null

}

/**
 * @ClassName:      TransferIncomeSelectEntity
 * @Description:    转让最后一个筛选条件数据返显封装类
 * @Author:         moran
 * @CreateDate:     2021/5/13 18:50
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/13 18:50
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class TransferIncomeSelectEntity {
    /**
     * 选中集合的下标
     * */
    var selectPosition = -1

    /**
     * 选中集合的名字--返显
     * */
    var selectName = mutableListOf<String>()

    /**
     * 选中集合的uuid --接口请求组装
     * */
    var selectUUID = mutableListOf("")

    /**
     * 升序或降序
     * */
    var controlSort = ""
}


/**
 * @ClassName:      TransferIncomeNewSelectEntity
 * @Description:    从SelectConditionTransferListActivity界面返回时，用户筛选的新排序
 * @Author:         moran
 * @CreateDate:     2021/5/16 16:17
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/16 16:17
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class TransferIncomeNewSelectEntity {

    /**
     * 升序或降序
     * */
    var controlSort = ""

    /**
     * 选中的名字
     * */
    var selectConditionName = ""

    /**
     * uuid
     * */

    var selectUUID = ""

}


/**
 * @ClassName:      TransferIncome
 * @Description:    排序组装的新数据类
 * @Author:         moran
 * @CreateDate:     2021/5/16 16:21
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/16 16:21
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class TransferIncome {

    var controlNname = ""


    /**
     * 升序、降序 uuid集合
     * */
    var controlSortUUIDMap: Map<String, String> = mutableMapOf()


}


/**
 * @ClassName:      ConditionEntity
 * @Description:    封装条件选项
 * @Author:         moran
 * @CreateDate:     2021/5/19 17:27
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/19 17:27
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ConditionEntity {

    /**
     * 筛选条件名字
     * */
    var conditionName = ""

    /**
     * 筛选条件uuid
     * */
    var uuid = ""

    /**
     * 当前筛选条件 升序或者降序
     * */
    var controlSort = ""

}


/**
 * @ClassName:      AmountNewSelectConditionEntity
 * @Description:    剩余期限新的筛选条件
 * @Author:         moran
 * @CreateDate:     2021/5/20 09:03
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/20 09:03
 * @UpdateRemark:   更新说明：
 * Version:        1.0
 */
class AmountNewSelectConditionEntity {

    /**
     * 起投金额筛选新条件的uuid
     * */
    var amountUuid = ""

    /**
     * 起投金额选择的条件标记
     * */
    var amountSelectMap: MutableMap<Int, Boolean> = mutableMapOf()

    /**
     * 起投金额选择条件名称
     * */
    var amountSelectConditionName = mutableListOf<String>()

}


/**
 * @ClassName:      AmountNewSelectConditionEntity
 * @Description:    剩余期限新的筛选条件
 * @Author:         moran
 * @CreateDate:     2021/5/20 09:03
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/20 09:03
 * @UpdateRemark:   更新说明：
 * Version:        1.0
 */
class TimeNewSelectConditionEntity {

    /**
     * 剩余期限选择新条件的uuid
     * */
    var timeUuid = ""

    /**
     * 剩余期限选择的新条件map
     * */
    var timeSelectMap: MutableMap<Int, Boolean> = mutableMapOf()

    /**
     * 剩余期限选择的新条件名称
     * */
    var timeSelectConditionName = mutableListOf<String>()

}










