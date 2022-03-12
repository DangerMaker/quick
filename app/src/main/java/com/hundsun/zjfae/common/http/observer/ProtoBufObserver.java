package com.hundsun.zjfae.common.http.observer;

import com.google.gson.Gson;
import com.hundsun.zjfae.activity.accountcenter.view.BindNewPhoneView;
import com.hundsun.zjfae.activity.forget.ForgetPasswordView;
import com.hundsun.zjfae.activity.mine.view.BankCardManagementView;
import com.hundsun.zjfae.activity.product.view.SelectConditionTransferListView;
import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.fragment.finance.ProductView;
import com.hundsun.zjfae.fragment.finance.TransferView;
import com.hundsun.zjfae.fragment.home.HomeView;

import java.lang.reflect.InvocationTargetException;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;
import onight.zjfae.afront.gens.v4.TransferList;
import onight.zjfae.afront.gens.v5.ProductList;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.v2.Login;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.common.http.observer
 * @ClassName: ProtoBufObserver
 * @Description: 封装protobuf解析类，业务code码获取，区分
 * @Author: moran
 * @CreateDate: 2019/6/13 9:21
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/13 9:21
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public abstract class ProtoBufObserver<T> extends BaseObserver<T> {
    private static final String TAG = ProtoBufObserver.class.getSimpleName();

    public ProtoBufObserver() {

    }

    public ProtoBufObserver(BaseView view) {
        super(view);
    }

    public ProtoBufObserver(BaseView view, String dialogMessage) {
        super(view, dialogMessage);
    }

    @Override
    public void onNext(T t) {
        try {
            CCLog.i(TAG, "----------数据返回解析 Start----------------");
            Class<?> mClass = t.getClass();
            String returnCode = mClass.getDeclaredMethod("getReturnCode").invoke(t).toString();
            String returnMsg = mClass.getDeclaredMethod("getReturnMsg").invoke(t).toString();
            Gson gson = new Gson();
            String json = gson.toJson(t);
            CCLog.i(TAG + "returnCode", returnCode);
            CCLog.i(TAG, "\n");
            CCLog.i(TAG + "returnMsg", returnMsg);
            CCLog.i(TAG, "\n");
            CCLog.i(TAG + "Data", json);
            CCLog.i(TAG, "\n");
            CCLog.i(TAG, "\n");
            CCLog.i(TAG, "----------数据返回解析 End----------------");
            onCoreCodeError(t, returnCode, returnMsg);
            if (ConstantCode.RETURN_CODE.equals(returnCode)) {
                if (view != null) {
                    view.hideLoading();
                }
                onSuccess(t);
            } else if (ConstantCode.LOGIN_TIME_OUT.equals(returnCode)) {
                if (view != null) {
                    view.loginTimeOut(returnMsg);
                }
            } else if (ConstantCode.NO_TRANSACTION_TIME.equals(returnCode)) {
                if (view != null) {
                    view.showError(returnMsg);
                }
            }
            //登录密码错误
            else if (t instanceof Login.Ret_PBAPP_login) {
                if (view != null) {
                    view.showError(returnMsg);
                    String needValidateAuthCode = ((Login.Ret_PBAPP_login) t).getData().getNeedValidateAuthCode();
                    if (view instanceof com.hundsun.zjfae.HomeView) {
                        if(ConstantCode.IMAGE_CODE_ERROR.equals(returnCode)|| ConstantCode.IMAGE_CODE_ERROR2.equals(returnCode)){
                            ((com.hundsun.zjfae.HomeView) view).refreshImageAuthCode();
                        }else{
                            ((com.hundsun.zjfae.HomeView) view).setNeedValidateAuthCode(needValidateAuthCode);
                        }
                    }
                }
            }
            //忘记密码
            else if (ConstantCode.USER_INFO_NO_EXIST.equals(returnCode) || ConstantCode.USER_ID_CARD_ERROR.equals(returnCode) || ConstantCode.IMAGE_CODE_ERROR.equals(returnCode)|| ConstantCode.IMAGE_CODE_ERROR2.equals(returnCode)) {
                if (view instanceof ForgetPasswordView) {
                    ((ForgetPasswordView) view).refreshImageAuthCode(returnMsg);
                } else if (view instanceof BindNewPhoneView) {
                    ((BindNewPhoneView) view).refreshImageAuthCode(returnMsg);
                } else if (view instanceof com.hundsun.zjfae.HomeView) {
                    ((com.hundsun.zjfae.HomeView) view).refreshImageAuthCode();
                    view.showError(returnMsg);
                } else {
                    view.showError(returnMsg);
                }
            }
            //产品列表状态判断
            else if (ConstantCode.NO_PRODUCT_CODE_1.equals(returnCode)) {

                if (view != null) {

                    if (t instanceof TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew) {

                        TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew transferOrderListNew = (TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew) t;

                        if (view instanceof TransferView) {

                            ((TransferView) view).onInvestmentState(transferOrderListNew.getData().getIsjump(), transferOrderListNew.getData().getJumpurl(), returnMsg, transferOrderListNew.getData().getIsShare());
                        } else if (view instanceof SelectConditionTransferListView) {

                            ((SelectConditionTransferListView) view).onInvestmentState(transferOrderListNew.getData().getIsjump(), transferOrderListNew.getData().getJumpurl(), returnMsg, transferOrderListNew.getData().getIsShare());
                        } else {
                            view.showError(returnMsg);
                        }
                    } else if (t instanceof PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome) {

                        PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome productListNewHome = (PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome) t;


                        if (view instanceof HomeView) {
                            ((HomeView) view).onInvestmentState(productListNewHome.getData().getIsjump(), productListNewHome.getData().getJumpurl(), returnMsg, productListNewHome.getData().getIsShare());
                        } else {
                            view.showError(returnMsg);
                        }

                    } else if (t instanceof PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew) {

                        PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew productListNew = (PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew) t;

                        if (view instanceof ProductView) {
                            ((ProductView) view).onInvestmentState(productListNew.getData().getIsjump(), productListNew.getData().getJumpurl(), returnMsg, productListNew.getData().getIsShare());
                        } else {
                            view.showError(returnMsg);
                        }
                    }

                }


            }
            //SMID不存在
            else if (ConstantCode.SMID_NO_PRESENCE.equals(returnCode)) {
                if (view != null) {
                    view.hideLoading();
                }
            }
            //用户签署人脸协议
            else if (ConstantCode.FACE_AGREEMENT.equals(returnCode)) {
                if (view != null) {
                    if (view instanceof BankCardManagementView) {
                        ((BankCardManagementView) view).onUserIsFaceAgreement();
                    } else {
                        view.showError(returnMsg);
                    }
                }
            } else if (ConstantCode.UNKNOWN_CODE.equals(returnCode)) {
                if (view != null) {
                    view.hideLoading();

                }
            } else if (t instanceof ADPictureProtoBuf.Ret_PBAPP_ads_picture) {
                //查询banner图出错
                if (view != null) {
                    view.hideLoading();
                }
            } else {
                if (view != null) {
                    view.showError(returnMsg);
                }
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


