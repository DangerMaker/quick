package com.hundsun.zjfae.activity.myinvitation.bean;

import java.util.List;

/**
 * @Description:推荐明细实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/28 15:19
 */
public class MyInvitationListBean {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"Nks0c2FiY3B4NlRVb25lOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYs","appVersion":"1.6.49","ReqTime":"1538185125503","SMID":"WUo3cDEwLmQ3N2Y2NjU5N2U1YWFTY085T2R3OjAwMDAxMDcxMjYvYXBwF","isShare":"1","pbname":"PBIFE_friendsrecommend_recommendDetailsInfo","realipzjs":"115.236.162.162","userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"pageInfo":{"pageSize":"10","pageIndex":"1","pageCount":"5","totalCount":"42"},"recommendDetailsList":[{"id":"1131","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115131","frfundAccount":"0000108417","frname":"GR000115131","frmobile":"180****5014","isBindCard":"0","isInvest":"0","cardAward":"0","investAward":"0","gmtRecommend":"2018-09-26 15:56:27","gmtCreate":"2018-09-26 15:56:27","gmtModify":"2018-09-26 15:56:27"},{"id":"1130","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115126","frfundAccount":"0000108412","frname":"凡*","frmobile":"180****5013","isBindCard":"1","isInvest":"1","gmtBinding":"2018-09-26 13:50:52","gmtInvest":"2018-09-26 14:02:25","gmtRecommend":"2018-09-26 13:49:59","gmtCreate":"2018-09-26 13:49:59","gmtModify":"2018-09-26 14:17:56"},{"id":"1129","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115125","frfundAccount":"0000108411","frname":"耳*","frmobile":"180****5012","isBindCard":"1","isInvest":"1","gmtBinding":"2018-09-26 14:03:51","gmtInvest":"2018-09-26 14:06:02","gmtRecommend":"2018-09-26 11:50:46","gmtCreate":"2018-09-26 11:50:46","gmtModify":"2018-09-26 14:17:56"},{"id":"1128","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115124","frfundAccount":"0000108410","frname":"GR000115124","frmobile":"180****5011","gmtRecommend":"2018-09-26 11:42:00","gmtCreate":"2018-09-26 11:42:00"},{"id":"1127","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115123","frfundAccount":"0000108409","frname":"GR000115123","frmobile":"180****5010","gmtRecommend":"2018-09-26 11:35:07","gmtCreate":"2018-09-26 11:35:07"},{"id":"1126","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115122","frfundAccount":"0000108408","frname":"GR000115122","frmobile":"180****5009","gmtRecommend":"2018-09-26 11:17:30","gmtCreate":"2018-09-26 11:17:30"},{"id":"1124","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115119","frfundAccount":"0000108405","frname":"路*","frmobile":"180****5007","isBindCard":"1","isInvest":"0","cardAward":"0","investAward":"0","gmtBinding":"2018-09-26 09:55:27","gmtRecommend":"2018-09-26 09:54:12","gmtCreate":"2018-09-26 09:54:12","gmtModify":"2018-09-26 09:55:27"},{"id":"1123","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115110","frfundAccount":"0000108396","frname":"GR000115110","frmobile":"180****0999","isBindCard":"0","isInvest":"0","cardAward":"0","investAward":"0","gmtRecommend":"2018-09-25 17:47:46","gmtCreate":"2018-09-25 17:47:46","gmtModify":"2018-09-25 17:47:46"},{"id":"1122","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115107","frfundAccount":"0000108393","frname":"第*","frmobile":"180****5006","isBindCard":"1","isInvest":"1","cardAward":"0","investAward":"0","gmtBinding":"2018-09-25 18:24:25","gmtInvest":"2018-09-25 18:25:59","gmtRecommend":"2018-09-25 17:01:34","gmtCreate":"2018-09-25 17:01:34","gmtModify":"2018-09-25 18:30:41"},{"id":"1121","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115106","frfundAccount":"0000108392","frname":"史*夫","frmobile":"180****5005 ","isBindCard":"1","isInvest":"1","cardAward":"0","investAward":"0","gmtBinding":"2018-09-25 18:01:38","gmtInvest":"2018-09-25 18:21:34","gmtRecommend":"2018-09-25 16:52:43","gmtCreate":"2018-09-25 16:52:43","gmtModify":"2018-09-25 18:30:41"}]}}
     */

    private String fh;
    private EhBean eh;
    private BodyBean body;

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

    public static class EhBean {
        /**
         * p : and
         * MFID : Nks0c2FiY3B4NlRVb25lOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYs
         * appVersion : 1.6.49
         * ReqTime : 1538185125503
         * SMID : WUo3cDEwLmQ3N2Y2NjU5N2U1YWFTY085T2R3OjAwMDAxMDcxMjYvYXBwF
         * isShare : 1
         * pbname : PBIFE_friendsrecommend_recommendDetailsInfo
         * realipzjs : 115.236.162.162
         * userid : 18012340000
         * platform : android
         */

        private String p;
        private String MFID;
        private String appVersion;
        private String ReqTime;
        private String SMID;
        private String isShare;
        private String pbname;
        private String realipzjs;
        private String userid;
        private String platform;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getMFID() {
            return MFID;
        }

        public void setMFID(String MFID) {
            this.MFID = MFID;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
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
    }

    public static class BodyBean {
        /**
         * returnCode : 0000
         * returnMsg : 操作通过
         * data : {"pageInfo":{"pageSize":"10","pageIndex":"1","pageCount":"5","totalCount":"42"},"recommendDetailsList":[{"id":"1131","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115131","frfundAccount":"0000108417","frname":"GR000115131","frmobile":"180****5014","isBindCard":"0","isInvest":"0","cardAward":"0","investAward":"0","gmtRecommend":"2018-09-26 15:56:27","gmtCreate":"2018-09-26 15:56:27","gmtModify":"2018-09-26 15:56:27"},{"id":"1130","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115126","frfundAccount":"0000108412","frname":"凡*","frmobile":"180****5013","isBindCard":"1","isInvest":"1","gmtBinding":"2018-09-26 13:50:52","gmtInvest":"2018-09-26 14:02:25","gmtRecommend":"2018-09-26 13:49:59","gmtCreate":"2018-09-26 13:49:59","gmtModify":"2018-09-26 14:17:56"},{"id":"1129","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115125","frfundAccount":"0000108411","frname":"耳*","frmobile":"180****5012","isBindCard":"1","isInvest":"1","gmtBinding":"2018-09-26 14:03:51","gmtInvest":"2018-09-26 14:06:02","gmtRecommend":"2018-09-26 11:50:46","gmtCreate":"2018-09-26 11:50:46","gmtModify":"2018-09-26 14:17:56"},{"id":"1128","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115124","frfundAccount":"0000108410","frname":"GR000115124","frmobile":"180****5011","gmtRecommend":"2018-09-26 11:42:00","gmtCreate":"2018-09-26 11:42:00"},{"id":"1127","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115123","frfundAccount":"0000108409","frname":"GR000115123","frmobile":"180****5010","gmtRecommend":"2018-09-26 11:35:07","gmtCreate":"2018-09-26 11:35:07"},{"id":"1126","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115122","frfundAccount":"0000108408","frname":"GR000115122","frmobile":"180****5009","gmtRecommend":"2018-09-26 11:17:30","gmtCreate":"2018-09-26 11:17:30"},{"id":"1124","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115119","frfundAccount":"0000108405","frname":"路*","frmobile":"180****5007","isBindCard":"1","isInvest":"0","cardAward":"0","investAward":"0","gmtBinding":"2018-09-26 09:55:27","gmtRecommend":"2018-09-26 09:54:12","gmtCreate":"2018-09-26 09:54:12","gmtModify":"2018-09-26 09:55:27"},{"id":"1123","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115110","frfundAccount":"0000108396","frname":"GR000115110","frmobile":"180****0999","isBindCard":"0","isInvest":"0","cardAward":"0","investAward":"0","gmtRecommend":"2018-09-25 17:47:46","gmtCreate":"2018-09-25 17:47:46","gmtModify":"2018-09-25 17:47:46"},{"id":"1122","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115107","frfundAccount":"0000108393","frname":"第*","frmobile":"180****5006","isBindCard":"1","isInvest":"1","cardAward":"0","investAward":"0","gmtBinding":"2018-09-25 18:24:25","gmtInvest":"2018-09-25 18:25:59","gmtRecommend":"2018-09-25 17:01:34","gmtCreate":"2018-09-25 17:01:34","gmtModify":"2018-09-25 18:30:41"},{"id":"1121","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115106","frfundAccount":"0000108392","frname":"史*夫","frmobile":"180****5005 ","isBindCard":"1","isInvest":"1","cardAward":"0","investAward":"0","gmtBinding":"2018-09-25 18:01:38","gmtInvest":"2018-09-25 18:21:34","gmtRecommend":"2018-09-25 16:52:43","gmtCreate":"2018-09-25 16:52:43","gmtModify":"2018-09-25 18:30:41"}]}
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

        public static class DataBean {
            /**
             * pageInfo : {"pageSize":"10","pageIndex":"1","pageCount":"5","totalCount":"42"}
             * recommendDetailsList : [{"id":"1131","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115131","frfundAccount":"0000108417","frname":"GR000115131","frmobile":"180****5014","isBindCard":"0","isInvest":"0","cardAward":"0","investAward":"0","gmtRecommend":"2018-09-26 15:56:27","gmtCreate":"2018-09-26 15:56:27","gmtModify":"2018-09-26 15:56:27"},{"id":"1130","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115126","frfundAccount":"0000108412","frname":"凡*","frmobile":"180****5013","isBindCard":"1","isInvest":"1","gmtBinding":"2018-09-26 13:50:52","gmtInvest":"2018-09-26 14:02:25","gmtRecommend":"2018-09-26 13:49:59","gmtCreate":"2018-09-26 13:49:59","gmtModify":"2018-09-26 14:17:56"},{"id":"1129","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115125","frfundAccount":"0000108411","frname":"耳*","frmobile":"180****5012","isBindCard":"1","isInvest":"1","gmtBinding":"2018-09-26 14:03:51","gmtInvest":"2018-09-26 14:06:02","gmtRecommend":"2018-09-26 11:50:46","gmtCreate":"2018-09-26 11:50:46","gmtModify":"2018-09-26 14:17:56"},{"id":"1128","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115124","frfundAccount":"0000108410","frname":"GR000115124","frmobile":"180****5011","gmtRecommend":"2018-09-26 11:42:00","gmtCreate":"2018-09-26 11:42:00"},{"id":"1127","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115123","frfundAccount":"0000108409","frname":"GR000115123","frmobile":"180****5010","gmtRecommend":"2018-09-26 11:35:07","gmtCreate":"2018-09-26 11:35:07"},{"id":"1126","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115122","frfundAccount":"0000108408","frname":"GR000115122","frmobile":"180****5009","gmtRecommend":"2018-09-26 11:17:30","gmtCreate":"2018-09-26 11:17:30"},{"id":"1124","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115119","frfundAccount":"0000108405","frname":"路*","frmobile":"180****5007","isBindCard":"1","isInvest":"0","cardAward":"0","investAward":"0","gmtBinding":"2018-09-26 09:55:27","gmtRecommend":"2018-09-26 09:54:12","gmtCreate":"2018-09-26 09:54:12","gmtModify":"2018-09-26 09:55:27"},{"id":"1123","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115110","frfundAccount":"0000108396","frname":"GR000115110","frmobile":"180****0999","isBindCard":"0","isInvest":"0","cardAward":"0","investAward":"0","gmtRecommend":"2018-09-25 17:47:46","gmtCreate":"2018-09-25 17:47:46","gmtModify":"2018-09-25 17:47:46"},{"id":"1122","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115107","frfundAccount":"0000108393","frname":"第*","frmobile":"180****5006","isBindCard":"1","isInvest":"1","cardAward":"0","investAward":"0","gmtBinding":"2018-09-25 18:24:25","gmtInvest":"2018-09-25 18:25:59","gmtRecommend":"2018-09-25 17:01:34","gmtCreate":"2018-09-25 17:01:34","gmtModify":"2018-09-25 18:30:41"},{"id":"1121","reaccount":"GR000113875","refundAccount":"0000107126","remobile":"18012340000","fraccount":"GR000115106","frfundAccount":"0000108392","frname":"史*夫","frmobile":"180****5005 ","isBindCard":"1","isInvest":"1","cardAward":"0","investAward":"0","gmtBinding":"2018-09-25 18:01:38","gmtInvest":"2018-09-25 18:21:34","gmtRecommend":"2018-09-25 16:52:43","gmtCreate":"2018-09-25 16:52:43","gmtModify":"2018-09-25 18:30:41"}]
             */

            private PageInfoBean pageInfo;
            private List<RecommendDetailsListBean> recommendDetailsList;

            public PageInfoBean getPageInfo() {
                return pageInfo;
            }

            public void setPageInfo(PageInfoBean pageInfo) {
                this.pageInfo = pageInfo;
            }

            public List<RecommendDetailsListBean> getRecommendDetailsList() {
                return recommendDetailsList;
            }

            public void setRecommendDetailsList(List<RecommendDetailsListBean> recommendDetailsList) {
                this.recommendDetailsList = recommendDetailsList;
            }

            public static class PageInfoBean {
                /**
                 * pageSize : 10
                 * pageIndex : 1
                 * pageCount : 5
                 * totalCount : 42
                 */

                private String pageSize;
                private String pageIndex;
                private String pageCount;
                private String totalCount;

                public String getPageSize() {
                    return pageSize;
                }

                public void setPageSize(String pageSize) {
                    this.pageSize = pageSize;
                }

                public String getPageIndex() {
                    return pageIndex;
                }

                public void setPageIndex(String pageIndex) {
                    this.pageIndex = pageIndex;
                }

                public String getPageCount() {
                    return pageCount;
                }

                public void setPageCount(String pageCount) {
                    this.pageCount = pageCount;
                }

                public String getTotalCount() {
                    return totalCount;
                }

                public void setTotalCount(String totalCount) {
                    this.totalCount = totalCount;
                }
            }

            public static class RecommendDetailsListBean {
                /**
                 * id : 1131
                 * reaccount : GR000113875
                 * refundAccount : 0000107126
                 * remobile : 18012340000
                 * fraccount : GR000115131
                 * frfundAccount : 0000108417
                 * frname : GR000115131
                 * frmobile : 180****5014
                 * isBindCard : 0
                 * isInvest : 0
                 * cardAward : 0
                 * investAward : 0
                 * gmtRecommend : 2018-09-26 15:56:27
                 * gmtCreate : 2018-09-26 15:56:27
                 * gmtModify : 2018-09-26 15:56:27
                 * gmtBinding : 2018-09-26 13:50:52
                 * gmtInvest : 2018-09-26 14:02:25
                 */

                private String id;
                private String reaccount;
                private String refundAccount;
                private String remobile;
                private String fraccount;
                private String frfundAccount;
                private String frname;
                private String frmobile;
                private String isBindCard;
                private String isInvest;
                private String cardAward;
                private String investAward;
                private String gmtRecommend;
                private String gmtCreate;
                private String gmtModify;
                private String gmtBinding;
                private String gmtInvest;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getReaccount() {
                    return reaccount;
                }

                public void setReaccount(String reaccount) {
                    this.reaccount = reaccount;
                }

                public String getRefundAccount() {
                    return refundAccount;
                }

                public void setRefundAccount(String refundAccount) {
                    this.refundAccount = refundAccount;
                }

                public String getRemobile() {
                    return remobile;
                }

                public void setRemobile(String remobile) {
                    this.remobile = remobile;
                }

                public String getFraccount() {
                    return fraccount;
                }

                public void setFraccount(String fraccount) {
                    this.fraccount = fraccount;
                }

                public String getFrfundAccount() {
                    return frfundAccount;
                }

                public void setFrfundAccount(String frfundAccount) {
                    this.frfundAccount = frfundAccount;
                }

                public String getFrname() {
                    return frname;
                }

                public void setFrname(String frname) {
                    this.frname = frname;
                }

                public String getFrmobile() {
                    return frmobile;
                }

                public void setFrmobile(String frmobile) {
                    this.frmobile = frmobile;
                }

                public String getIsBindCard() {
                    return isBindCard;
                }

                public void setIsBindCard(String isBindCard) {
                    this.isBindCard = isBindCard;
                }

                public String getIsInvest() {
                    return isInvest;
                }

                public void setIsInvest(String isInvest) {
                    this.isInvest = isInvest;
                }

                public String getCardAward() {
                    return cardAward;
                }

                public void setCardAward(String cardAward) {
                    this.cardAward = cardAward;
                }

                public String getInvestAward() {
                    return investAward;
                }

                public void setInvestAward(String investAward) {
                    this.investAward = investAward;
                }

                public String getGmtRecommend() {
                    return gmtRecommend;
                }

                public void setGmtRecommend(String gmtRecommend) {
                    this.gmtRecommend = gmtRecommend;
                }

                public String getGmtCreate() {
                    return gmtCreate;
                }

                public void setGmtCreate(String gmtCreate) {
                    this.gmtCreate = gmtCreate;
                }

                public String getGmtModify() {
                    return gmtModify;
                }

                public void setGmtModify(String gmtModify) {
                    this.gmtModify = gmtModify;
                }

                public String getGmtBinding() {
                    return gmtBinding;
                }

                public void setGmtBinding(String gmtBinding) {
                    this.gmtBinding = gmtBinding;
                }

                public String getGmtInvest() {
                    return gmtInvest;
                }

                public void setGmtInvest(String gmtInvest) {
                    this.gmtInvest = gmtInvest;
                }
            }
        }
    }
}
