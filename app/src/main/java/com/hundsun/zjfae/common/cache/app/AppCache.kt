package com.hundsun.zjfae.common.cache.app

import com.hundsun.zjfae.common.utils.SharedPreferenceAccesser

object AppCache {

    private const val PRODUCT_LIST_TITLE = "productListTitle"

    const val TRADE_AREA_IS_SHOW = "trade_area_is_show"

    const val TRADE_ICON_URL = "trade_icon_url"
    const val TRADE_ICON_CLICK_URL = "trade_icon_click_url"


    fun saveProductTitle(value : String){
         SharedPreferenceAccesser.saveStringData(PRODUCT_LIST_TITLE,value)
    }

    fun getProductTitle() : String{

       val title =  SharedPreferenceAccesser.getStringData(PRODUCT_LIST_TITLE)

        title?.apply {

            return this

        }
        return "购买"
    }
}