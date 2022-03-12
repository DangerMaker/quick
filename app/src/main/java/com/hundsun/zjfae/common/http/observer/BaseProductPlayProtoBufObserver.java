package com.hundsun.zjfae.common.http.observer;


import com.google.gson.Gson;
import com.hundsun.zjfae.activity.moneymanagement.view.SellView;
import com.hundsun.zjfae.activity.product.view.ProductCodeView;
import com.hundsun.zjfae.activity.product.view.SpvProductDetailView;
import com.hundsun.zjfae.activity.product.view.TransferDetailPlayView;
import com.hundsun.zjfae.activity.product.view.TransferDetailView;
import com.hundsun.zjfae.activity.productreserve.view.ReserveListDetailView;
import com.hundsun.zjfae.activity.productreserve.view.ReserveListView;
import com.hundsun.zjfae.activity.productreserve.view.ReservePayView;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductDetailView;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductPlayView;
import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.fragment.home.HomeView;

import java.lang.reflect.InvocationTargetException;

import onight.zjfae.afront.gens.ProductOrderValidate;
import onight.zjfae.afront.gens.ProductPre;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.TransferOrderPre;
import onight.zjfae.afront.gens.v2.QueryTransferSellProfitsPB;
import onight.zjfae.afront.gens.v2.TransferOrderSec;

import static com.hundsun.zjfae.common.http.api.ConstantName.TransferOrderSec;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.common.http.observer
 * @ClassName: BaseProductPlayProtoBufObserver
 * @Description: 购买/转让/预约/特约购买Observer
 * @Author: moran
 * @CreateDate: 2019/6/12 19:46
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/12 19:46
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public abstract class BaseProductPlayProtoBufObserver<T> extends ProtoBufObserver<T> {

    private static final String TAG = BaseProductPlayProtoBufObserver.class.getSimpleName();

    public BaseProductPlayProtoBufObserver() {

    }

    public BaseProductPlayProtoBufObserver(BaseView view) {
        super(view);
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
            if (view != null) {
                if (ConstantCode.RETURN_CODE.equals(returnCode)) {
                    onSuccess(t);
                } else {
                    onReturnCode(t, returnCode, returnMsg);
                }
            } else {
                onSuccess(t);
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


    }

    /**
     * 业务code码处理
     *
     * @param t          返回请求对象
     * @param returnCode 返回code码
     * @param returnMsg  返回信息
     */
    private void onReturnCode(T t, String returnCode, String returnMsg) {

        if (ConstantCode.LOGIN_TIME_OUT.equals(returnCode)) {
            view.loginTimeOut(returnMsg);
        }
        //系统升级中
        else if (ConstantCode.SYSTEM_UPDATING.equals(returnCode)) {
            view.showError(returnMsg);
        }

        //黑名单 /已经预约
        else if (ConstantCode.BLACK_LIST_CODE.equals(returnCode)) {
            view.isFinishActivity(returnMsg);
        }

        //购买预检查第一步 / 转让预检查第一步，
        else if (t instanceof ProductPre.Ret_PBIFE_trade_subscribeProductPre || t instanceof TransferOrderPre.Ret_PBIFE_trade_transferOrderPre || t instanceof ProductOrderValidate.Ret_PBIFE_trade_productOrderValidate) {

            //65周岁公告
            if (ConstantCode.HIGH_AGE_CODE.equals(returnCode)) {
                //购买
                if (view instanceof ProductCodeView) {
                    ((ProductCodeView) view).onHighAge();
                }
                //转让
                else if (view instanceof TransferDetailView) {
                    ((TransferDetailView) view).onHighAge();
                } else if (view instanceof SpvProductDetailView) {
                    ((SpvProductDetailView) view).onHighAge();
                }
                //我的预约进入产品详情页
                else if (view instanceof ReserveProductDetailView) {
                    ((ReserveProductDetailView) view).onHighAge();
                }
                //长期或者短期预约详情
                else if (view instanceof ReserveListDetailView) {
                    ((ReserveListDetailView) view).onHighAge();
                } else {
                    onException(returnMsg);
                }
            }
        }
        //风险测评
        else if (ConstantCode.RISK_ASSESSMENT_CODE.equals(returnCode) || ConstantCode.RISK_ASSESSMENT_CODE_1.equals(returnCode) || ConstantCode.RISK_ASSESSMENT_CODE_2
                .equals(returnCode)) {
            view.hideLoading();
            //购买详情
            if (view instanceof ProductCodeView) {
                ((ProductCodeView) view).onRiskAssessment(returnMsg);
            }
            //转让详情
            else if (view instanceof TransferDetailView) {
                ((TransferDetailView) view).onRiskAssessment(returnMsg);
            }
            //spv产品详情
            else if (view instanceof SpvProductDetailView) {
                ((SpvProductDetailView) view).onRiskAssessment(returnMsg);
            }
            //长期预约短期预约详情
            else if (view instanceof ReserveListDetailView) {
                ((ReserveListDetailView) view).onRiskAssessment(returnMsg);
            }
            //长期预约短期预约列表
            else if (view instanceof ReserveListView) {
                ((ReserveListView) view).onRiskAssessment(returnMsg);
            }
            //我的预约
            else if (view instanceof ReserveProductDetailView) {
                ((ReserveProductDetailView) view).onRiskAssessment(returnMsg);
            } else {
                onException(returnMsg);
            }
        }

        //高净值会员申请
        else if (ConstantCode.SENIOR_MEMBER_CODE.equals(returnCode)) {
            if (view instanceof ProductCodeView) {
                ((ProductCodeView) view).onSeniorMember(returnMsg);
            } else if (view instanceof TransferDetailView) {
                ((TransferDetailView) view).onSeniorMember(returnMsg);
            } else if (view instanceof SpvProductDetailView) {
                ((SpvProductDetailView) view).onSeniorMember(returnMsg);
            } else if (view instanceof ReserveProductDetailView) {
                ((ReserveProductDetailView) view).onSeniorMember(returnMsg);
            } else if (view instanceof ReserveListDetailView) {
                ((ReserveListDetailView) view).onSeniorMember(returnMsg);
            }
        }

        //新客专享购买提示
        else if (ConstantCode.NOVICE_CODE.equals(returnCode)) {
            view.hideLoading();
            if (view instanceof ProductCodeView) {
                ((ProductCodeView) view).onNovicePrompt(returnMsg);
            } else if (view instanceof ReserveProductDetailView) {
                ((ReserveProductDetailView) view).onNovicePrompt(returnMsg);
            } else if (view instanceof ReserveListDetailView) {
                ((ReserveListDetailView) view).onNovicePrompt(returnMsg);
            } else {
                onException(returnMsg);
            }
        }

        //更新个人可购金额
        else if (ConstantCode.PURCHASE_AMOUNT_CODE.equals(returnCode)) {

            if (view instanceof ProductCodeView && t instanceof ProductSec.Ret_PBIFE_trade_subscribeProductSec) {

                ((ProductCodeView) view).onUpDataUserPurchaseAmount((ProductSec.Ret_PBIFE_trade_subscribeProductSec) t);

            } else if (view instanceof SpvProductDetailView && t instanceof TransferOrderSec.Ret_PBIFE_trade_transferOrderSec) {

                ((SpvProductDetailView) view).onUpDataUserPurchaseAmount((TransferOrderSec.Ret_PBIFE_trade_transferOrderSec) t);

            } else if (view instanceof ReserveProductDetailView && t instanceof ProductSec.Ret_PBIFE_trade_subscribeProductSec) {

                ((ReserveProductDetailView) view).onUpDataUserPurchaseAmount((ProductSec.Ret_PBIFE_trade_subscribeProductSec) t);
            } else {

                onException(returnMsg);
            }


        }
        //合格投资者申请
        else if (ConstantCode.QUALIFIED_MEMBER_CODE.equals(returnCode)) {
            view.hideLoading();
            if (view instanceof ProductCodeView) {
                ((ProductCodeView) view).onQualifiedMember(returnMsg);
            } else if (view instanceof TransferDetailView) {
                ((TransferDetailView) view).onQualifiedMember(returnMsg);
            } else if (view instanceof SpvProductDetailView) {
                ((SpvProductDetailView) view).onQualifiedMember(returnMsg);
            } else if (view instanceof ReserveProductDetailView) {
                ((ReserveProductDetailView) view).onQualifiedMember(returnMsg);
            } else if (view instanceof ReserveListDetailView) {
                ((ReserveListDetailView) view).onQualifiedMember(returnMsg);
            } else {
                onException(returnMsg);
            }
        }

        //购买产品不可转让提示
        else if (ConstantCode.NO_TRANSFER_CODE.equals(returnCode)) {
            view.hideLoading();
            if (view instanceof ProductCodeView) {
                ((ProductCodeView) view).onPlayNoTransfer(returnMsg);
            } else if (view instanceof SpvProductDetailView) {
                ((SpvProductDetailView) view).onPlayNoTransfer(returnMsg);
            } else if (view instanceof ReserveProductDetailView) {
                ((ReserveProductDetailView) view).onPlayNoTransfer(returnMsg);
            } else if (view instanceof ReserveListDetailView) {
                ((ReserveListDetailView) view).onPlayNoTransfer(returnMsg);
            } else {
                onException(returnMsg);
            }
        }
        //购买金额有误
        else if (ConstantCode.PAYMENT_AMOUNT_CODE.equals(returnCode)) {
            view.showError("请输入正确金额");
        }
        //挂单利率不符
        else if (ConstantCode.INCOME.equals(returnCode)) {
            if (view instanceof SellView) {
                if (t instanceof QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits) {

                    ((SellView) view).onSellProfits(((QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits) t).getData(), returnMsg);
                }

            }
        }
        //充值失败，线下充值，转让充值
        else if (ConstantCode.RECHARGE_CODE.equals(returnCode)) {
            if (view instanceof TransferDetailPlayView) {
                ((TransferDetailPlayView) view).onFundRechargeError(returnMsg);
            } else if (view instanceof ReservePayView) {
                ((ReservePayView) view).onFundRechargeError(returnMsg);
            } else if (view instanceof ReserveProductPlayView) {
                ((ReserveProductPlayView) view).onFundRechargeError(returnMsg);
            } else {
                onException(returnMsg);
            }


        }
        //引导用户绑卡
        else if (ConstantCode.GUIDE_ADD_BANK_CODE.equals(returnCode)) {
            if (view != null) {
                if (view instanceof TransferDetailPlayView) {
                    ((TransferDetailPlayView) view).onGuideAddBank(returnMsg);
                } else if (view instanceof ReservePayView) {
                    ((ReservePayView) view).onGuideAddBank(returnMsg);
                }
                //预约支付
                else if (view instanceof ReserveProductPlayView) {
                    ((ReserveProductPlayView) view).onGuideAddBank(returnMsg);
                } else {
                    onException(returnMsg);
                }
            }
        } else {
            if (view != null) {
                if (view instanceof HomeView) {
                } else {
                    view.showError(returnMsg);
                }


            }
        }
    }
}
