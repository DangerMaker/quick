package com.hundsun.zjfae.fragment.finance.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class QuanBean implements Parcelable {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"appVersion":"1.6.48","ZJSRANDOMID":"a5460400eae840a492d916fa23a07662","pbname":"PBIFE_trade_queryMyKqQuan","realipzjs":"122.225.201.180","ZJS_AUTH_CODE_ID":"bd9d03e7c9384ac1a1f377735ea8c20d","userid":"13400000001","platform":"android","p":"and","clientOsver":"8","MFID":"Vjh4aGFiY3BOVEtMUlRlOmFwcHVzZXJzbG9naW46LTAwMDAxMDQzOTc6","ReqTime":"1534763307797","SMID":"Y1NsejkuZDc3ZjY2NTk3ZTVhcHFLQlJ6eTowMDAwMTA0Mzk3L2FwcA8","isShare":"1","ZJS_CUSTOMER_CHECK_COOKIE":"ec0c11c5b5be471590498aae36e939f3"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"quanList":[{"quanDetailsId":"KDA01j5aL486","quanType":"A","quanTypeName":"加息券","quanValue":"0.011","quanCanStack":"0","quanValidityEnd":"2018年11月24日","quanIncreaseInterestAmount":"5000","enableIncreaseInterestAmount":"1000.00","percentValue":"1.1%","quanName":"有加息金额的加息券"},{"quanDetailsId":"KDF01p65k419","quanType":"F","quanTypeName":"满减券","quanValue":"50","quanCanStack":"0","quanValidityEnd":"2019年12月25日","quanFullReducedAmount":"1000","quanName":"测试一下"}],"quanSize":"2","baoSize":"0","totalSize":"2"}}
     */

    private String fh;
    private EhBean eh;
    private BodyBean body;

    protected QuanBean(Parcel in) {
        fh = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fh);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuanBean> CREATOR = new Creator<QuanBean>() {
        @Override
        public QuanBean createFromParcel(Parcel in) {
            return new QuanBean(in);
        }

        @Override
        public QuanBean[] newArray(int size) {
            return new QuanBean[size];
        }
    };

    public String getFh() {
        return fh;
    }

    public void setFh(String fh) {
        this.fh = fh;
    }

    public EhBean getEh() {
        return eh;
    }

    public void setEh(EhBean eh) {
        this.eh = eh;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class EhBean implements Parcelable {
        /**
         * appVersion : 1.6.48
         * ZJSRANDOMID : a5460400eae840a492d916fa23a07662
         * pbname : PBIFE_trade_queryMyKqQuan
         * realipzjs : 122.225.201.180
         * ZJS_AUTH_CODE_ID : bd9d03e7c9384ac1a1f377735ea8c20d
         * userid : 13400000001
         * platform : android
         * p : and
         * clientOsver : 8
         * MFID : Vjh4aGFiY3BOVEtMUlRlOmFwcHVzZXJzbG9naW46LTAwMDAxMDQzOTc6
         * ReqTime : 1534763307797
         * SMID : Y1NsejkuZDc3ZjY2NTk3ZTVhcHFLQlJ6eTowMDAwMTA0Mzk3L2FwcA8
         * isShare : 1
         * ZJS_CUSTOMER_CHECK_COOKIE : ec0c11c5b5be471590498aae36e939f3
         */

        private String appVersion;
        private String ZJSRANDOMID;
        private String pbname;
        private String realipzjs;
        private String ZJS_AUTH_CODE_ID;
        private String userid;
        private String platform;
        private String p;
        private String clientOsver;
        private String MFID;
        private String ReqTime;
        private String SMID;
        private String isShare;
        private String ZJS_CUSTOMER_CHECK_COOKIE;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getZJSRANDOMID() {
            return ZJSRANDOMID;
        }

        public void setZJSRANDOMID(String ZJSRANDOMID) {
            this.ZJSRANDOMID = ZJSRANDOMID;
        }

        public String getPbname() {
            return pbname;
        }

        public void setPbname(String pbname) {
            this.pbname = pbname;
        }

        public String getRealipzjs() {
            return realipzjs;
        }

        public void setRealipzjs(String realipzjs) {
            this.realipzjs = realipzjs;
        }

        public String getZJS_AUTH_CODE_ID() {
            return ZJS_AUTH_CODE_ID;
        }

        public void setZJS_AUTH_CODE_ID(String ZJS_AUTH_CODE_ID) {
            this.ZJS_AUTH_CODE_ID = ZJS_AUTH_CODE_ID;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getClientOsver() {
            return clientOsver;
        }

        public void setClientOsver(String clientOsver) {
            this.clientOsver = clientOsver;
        }

        public String getMFID() {
            return MFID;
        }

        public void setMFID(String MFID) {
            this.MFID = MFID;
        }

        public String getReqTime() {
            return ReqTime;
        }

        public void setReqTime(String ReqTime) {
            this.ReqTime = ReqTime;
        }

        public String getSMID() {
            return SMID;
        }

        public void setSMID(String SMID) {
            this.SMID = SMID;
        }

        public String getIsShare() {
            return isShare;
        }

        public void setIsShare(String isShare) {
            this.isShare = isShare;
        }

        public String getZJS_CUSTOMER_CHECK_COOKIE() {
            return ZJS_CUSTOMER_CHECK_COOKIE;
        }

        public void setZJS_CUSTOMER_CHECK_COOKIE(String ZJS_CUSTOMER_CHECK_COOKIE) {
            this.ZJS_CUSTOMER_CHECK_COOKIE = ZJS_CUSTOMER_CHECK_COOKIE;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.appVersion);
            dest.writeString(this.ZJSRANDOMID);
            dest.writeString(this.pbname);
            dest.writeString(this.realipzjs);
            dest.writeString(this.ZJS_AUTH_CODE_ID);
            dest.writeString(this.userid);
            dest.writeString(this.platform);
            dest.writeString(this.p);
            dest.writeString(this.clientOsver);
            dest.writeString(this.MFID);
            dest.writeString(this.ReqTime);
            dest.writeString(this.SMID);
            dest.writeString(this.isShare);
            dest.writeString(this.ZJS_CUSTOMER_CHECK_COOKIE);
        }

        public EhBean() {
        }

        protected EhBean(Parcel in) {
            this.appVersion = in.readString();
            this.ZJSRANDOMID = in.readString();
            this.pbname = in.readString();
            this.realipzjs = in.readString();
            this.ZJS_AUTH_CODE_ID = in.readString();
            this.userid = in.readString();
            this.platform = in.readString();
            this.p = in.readString();
            this.clientOsver = in.readString();
            this.MFID = in.readString();
            this.ReqTime = in.readString();
            this.SMID = in.readString();
            this.isShare = in.readString();
            this.ZJS_CUSTOMER_CHECK_COOKIE = in.readString();
        }

        public static final Creator<EhBean> CREATOR = new Creator<EhBean>() {
            @Override
            public EhBean createFromParcel(Parcel source) {
                return new EhBean(source);
            }

            @Override
            public EhBean[] newArray(int size) {
                return new EhBean[size];
            }
        };
    }

    public static class BodyBean implements Parcelable {
        /**
         * returnCode : 0000
         * returnMsg : 操作通过
         * data : {"quanList":[{"quanDetailsId":"KDA01j5aL486","quanType":"A","quanTypeName":"加息券","quanValue":"0.011","quanCanStack":"0","quanValidityEnd":"2018年11月24日","quanIncreaseInterestAmount":"5000","enableIncreaseInterestAmount":"1000.00","percentValue":"1.1%","quanName":"有加息金额的加息券"},{"quanDetailsId":"KDF01p65k419","quanType":"F","quanTypeName":"满减券","quanValue":"50","quanCanStack":"0","quanValidityEnd":"2019年12月25日","quanFullReducedAmount":"1000","quanName":"测试一下"}],"quanSize":"2","baoSize":"0","totalSize":"2"}
         */

        private String returnCode;
        private String returnMsg;
        private DataBean data;

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Parcelable {
            /**
             * quanList : [{"quanDetailsId":"KDA01j5aL486","quanType":"A","quanTypeName":"加息券","quanValue":"0.011","quanCanStack":"0","quanValidityEnd":"2018年11月24日","quanIncreaseInterestAmount":"5000","enableIncreaseInterestAmount":"1000.00","percentValue":"1.1%","quanName":"有加息金额的加息券"},{"quanDetailsId":"KDF01p65k419","quanType":"F","quanTypeName":"满减券","quanValue":"50","quanCanStack":"0","quanValidityEnd":"2019年12月25日","quanFullReducedAmount":"1000","quanName":"测试一下"}]
             * quanSize : 2
             * baoSize : 0
             * totalSize : 2
             */

            private String quanSize;
            private String baoSize;
            private String totalSize;
            private List<QuanListBean> quanList;

            private List<BaoListBean> baoList;

            public String getQuanSize() {
                return quanSize;
            }

            public void setQuanSize(String quanSize) {
                this.quanSize = quanSize;
            }

            public String getBaoSize() {
                return baoSize;
            }

            public void setBaoSize(String baoSize) {
                this.baoSize = baoSize;
            }

            public String getTotalSize() {
                return totalSize;
            }

            public void setTotalSize(String totalSize) {
                this.totalSize = totalSize;
            }

            public List<QuanListBean> getQuanList() {
                return quanList;
            }

            public void setQuanList(List<QuanListBean> quanList) {
                this.quanList = quanList;
            }

            public List<BaoListBean> getBaoList() {
                return baoList;
            }

            public void setBaoList(List<BaoListBean> baoList) {
                this.baoList = baoList;
            }


            public static class BaoListBean implements Parcelable {
                /**
                 * 	"quanDetailsId": "KDR01kLvp908",
                 "quanType": "R",
                 "quanTypeName": "红包",
                 "quanValue": "100",
                 "quanCanStack": "1",
                 "quanValidityEnd": "2045年12月10日",
                 "quanName": "CJW发红包"
                 * */

                private String quanDetailsId;
                private String quanType;
                private String quanTypeName;
                private String quanValue;
                private String quanCanStack;
                private String quanValidityEnd;
                private String quanName;

                public String getQuanDetailsId() {
                    return quanDetailsId;
                }

                public void setQuanDetailsId(String quanDetailsId) {
                    this.quanDetailsId = quanDetailsId;
                }

                public String getQuanType() {
                    return quanType;
                }

                public void setQuanType(String quanType) {
                    this.quanType = quanType;
                }

                public String getQuanTypeName() {
                    return quanTypeName;
                }

                public void setQuanTypeName(String quanTypeName) {
                    this.quanTypeName = quanTypeName;
                }

                public String getQuanValue() {
                    return quanValue;
                }

                public void setQuanValue(String quanValue) {
                    this.quanValue = quanValue;
                }

                public String getQuanCanStack() {
                    return quanCanStack;
                }

                public void setQuanCanStack(String quanCanStack) {
                    this.quanCanStack = quanCanStack;
                }

                public String getQuanValidityEnd() {
                    return quanValidityEnd;
                }

                public void setQuanValidityEnd(String quanValidityEnd) {
                    this.quanValidityEnd = quanValidityEnd;
                }

                public String getQuanName() {
                    return quanName;
                }

                public void setQuanName(String quanName) {
                    this.quanName = quanName;
                }


                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.quanDetailsId);
                    dest.writeString(this.quanType);
                    dest.writeString(this.quanTypeName);
                    dest.writeString(this.quanValue);
                    dest.writeString(this.quanCanStack);
                    dest.writeString(this.quanValidityEnd);
                    dest.writeString(this.quanName);
                }

                public BaoListBean() {
                }

                protected BaoListBean(Parcel in) {
                    this.quanDetailsId = in.readString();
                    this.quanType = in.readString();
                    this.quanTypeName = in.readString();
                    this.quanValue = in.readString();
                    this.quanCanStack = in.readString();
                    this.quanValidityEnd = in.readString();
                    this.quanName = in.readString();
                }

                public static final Creator<BaoListBean> CREATOR = new Creator<BaoListBean>() {
                    @Override
                    public BaoListBean createFromParcel(Parcel source) {
                        return new BaoListBean(source);
                    }

                    @Override
                    public BaoListBean[] newArray(int size) {
                        return new BaoListBean[size];
                    }
                };
            }

            public static class QuanListBean implements Parcelable {
                /**
                 * quanDetailsId : KDA01j5aL486
                 * quanType : A
                 * quanTypeName : 加息券
                 * quanValue : 0.011
                 * quanCanStack : 0
                 * quanValidityEnd : 2018年11月24日
                 * quanIncreaseInterestAmount : 5000
                 * enableIncreaseInterestAmount : 1000.00
                 * percentValue : 1.1%
                 * quanName : 有加息金额的加息券
                 * quanFullReducedAmount : 1000
                 */

                private String quanDetailsId;
                private String quanType;
                private String quanTypeName;
                private String quanValue;
                private String quanCanStack;
                private String quanValidityEnd;
                private String quanIncreaseInterestAmount;
                private String enableIncreaseInterestAmount;
                private String percentValue;
                private String quanName;
                private String quanFullReducedAmount;

                public String getQuanDetailsId() {
                    return quanDetailsId;
                }

                public void setQuanDetailsId(String quanDetailsId) {
                    this.quanDetailsId = quanDetailsId;
                }

                public String getQuanType() {
                    return quanType;
                }

                public void setQuanType(String quanType) {
                    this.quanType = quanType;
                }

                public String getQuanTypeName() {
                    return quanTypeName;
                }

                public void setQuanTypeName(String quanTypeName) {
                    this.quanTypeName = quanTypeName;
                }

                public String getQuanValue() {
                    return quanValue;
                }

                public void setQuanValue(String quanValue) {
                    this.quanValue = quanValue;
                }

                public String getQuanCanStack() {
                    return quanCanStack;
                }

                public void setQuanCanStack(String quanCanStack) {
                    this.quanCanStack = quanCanStack;
                }

                public String getQuanValidityEnd() {
                    return quanValidityEnd;
                }

                public void setQuanValidityEnd(String quanValidityEnd) {
                    this.quanValidityEnd = quanValidityEnd;
                }

                public String getQuanIncreaseInterestAmount() {
                    return quanIncreaseInterestAmount;
                }

                public void setQuanIncreaseInterestAmount(String quanIncreaseInterestAmount) {
                    this.quanIncreaseInterestAmount = quanIncreaseInterestAmount;
                }

                public String getEnableIncreaseInterestAmount() {
                    return enableIncreaseInterestAmount;
                }

                public void setEnableIncreaseInterestAmount(String enableIncreaseInterestAmount) {
                    this.enableIncreaseInterestAmount = enableIncreaseInterestAmount;
                }

                public String getPercentValue() {
                    return percentValue;
                }

                public void setPercentValue(String percentValue) {
                    this.percentValue = percentValue;
                }

                public String getQuanName() {
                    return quanName;
                }

                public void setQuanName(String quanName) {
                    this.quanName = quanName;
                }

                public String getQuanFullReducedAmount() {
                    return quanFullReducedAmount;
                }

                public void setQuanFullReducedAmount(String quanFullReducedAmount) {
                    this.quanFullReducedAmount = quanFullReducedAmount;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.quanDetailsId);
                    dest.writeString(this.quanType);
                    dest.writeString(this.quanTypeName);
                    dest.writeString(this.quanValue);
                    dest.writeString(this.quanCanStack);
                    dest.writeString(this.quanValidityEnd);
                    dest.writeString(this.quanIncreaseInterestAmount);
                    dest.writeString(this.enableIncreaseInterestAmount);
                    dest.writeString(this.percentValue);
                    dest.writeString(this.quanName);
                    dest.writeString(this.quanFullReducedAmount);
                }

                public QuanListBean() {
                }

                protected QuanListBean(Parcel in) {
                    this.quanDetailsId = in.readString();
                    this.quanType = in.readString();
                    this.quanTypeName = in.readString();
                    this.quanValue = in.readString();
                    this.quanCanStack = in.readString();
                    this.quanValidityEnd = in.readString();
                    this.quanIncreaseInterestAmount = in.readString();
                    this.enableIncreaseInterestAmount = in.readString();
                    this.percentValue = in.readString();
                    this.quanName = in.readString();
                    this.quanFullReducedAmount = in.readString();
                }

                public static final Creator<QuanListBean> CREATOR = new Creator<QuanListBean>() {
                    @Override
                    public QuanListBean createFromParcel(Parcel source) {
                        return new QuanListBean(source);
                    }

                    @Override
                    public QuanListBean[] newArray(int size) {
                        return new QuanListBean[size];
                    }
                };
            }

            public DataBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.quanSize);
                dest.writeString(this.baoSize);
                dest.writeString(this.totalSize);
                dest.writeTypedList(this.quanList);
                dest.writeTypedList(this.baoList);
            }

            protected DataBean(Parcel in) {
                this.quanSize = in.readString();
                this.baoSize = in.readString();
                this.totalSize = in.readString();
                this.quanList = in.createTypedArrayList(QuanListBean.CREATOR);
                this.baoList = in.createTypedArrayList(BaoListBean.CREATOR);
            }

            public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
                @Override
                public DataBean createFromParcel(Parcel source) {
                    return new DataBean(source);
                }

                @Override
                public DataBean[] newArray(int size) {
                    return new DataBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.returnCode);
            dest.writeString(this.returnMsg);
            dest.writeParcelable(this.data, flags);
        }

        public BodyBean() {
        }

        protected BodyBean(Parcel in) {
            this.returnCode = in.readString();
            this.returnMsg = in.readString();
            this.data = in.readParcelable(DataBean.class.getClassLoader());
        }

        public static final Creator<BodyBean> CREATOR = new Creator<BodyBean>() {
            @Override
            public BodyBean createFromParcel(Parcel source) {
                return new BodyBean(source);
            }

            @Override
            public BodyBean[] newArray(int size) {
                return new BodyBean[size];
            }
        };
    }
}
