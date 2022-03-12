package com.hundsun.zjfae.activity.mymessage.bean;

import java.util.List;

/**
 * @Description:我的消息实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/28 15:19
 */
public class MyMessageBean {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"NnROcmFiY3B4NlRVa25lOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYC","appVersion":"1.6.49","ReqTime":"1538187607626","SMID":"WXJicjEwLmQ3N2Y2NjU5N2U1YWFTY085VmR3OjAwMDAxMDcxMjYvYXBwp","isShare":"1","pbname":"PBIFE_messagemanage_listMessage","realipzjs":"115.236.162.162","userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"pageInfo":{"pageSize":"2147483647","pageIndex":"1","pageCount":"1","totalCount":"102"},"messageListList":[{"id":"54849","sender":"浙金中心消息推送","msgType":"0","msgStatus":"0","readStatus":"0","title":"1111","content":"阿斯达四大","createTime":"2018-09-10 17:57:35","updateTime":"2018-09-10 17:57:44","expTime":"2018-12-10 17:55:09","msgPublishStatus":"00","msgPublishType":"01","msgPublishChannel":"02","contentFilter":"阿斯达四大"}],"unreadMessageCount":"0"}}
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
         * MFID : NnROcmFiY3B4NlRVa25lOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYC
         * appVersion : 1.6.49
         * ReqTime : 1538187607626
         * SMID : WXJicjEwLmQ3N2Y2NjU5N2U1YWFTY085VmR3OjAwMDAxMDcxMjYvYXBwp
         * isShare : 1
         * pbname : PBIFE_messagemanage_listMessage
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
         * data : {"pageInfo":{"pageSize":"2147483647","pageIndex":"1","pageCount":"1","totalCount":"102"},"messageListList":[{"id":"54849","sender":"浙金中心消息推送","msgType":"0","msgStatus":"0","readStatus":"0","title":"1111","content":"阿斯达四大","createTime":"2018-09-10 17:57:35","updateTime":"2018-09-10 17:57:44","expTime":"2018-12-10 17:55:09","msgPublishStatus":"00","msgPublishType":"01","msgPublishChannel":"02","contentFilter":"阿斯达四大"}],"unreadMessageCount":"0"}
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
             * pageInfo : {"pageSize":"2147483647","pageIndex":"1","pageCount":"1","totalCount":"102"}
             * messageListList : [{"id":"54849","sender":"浙金中心消息推送","msgType":"0","msgStatus":"0","readStatus":"0","title":"1111","content":"阿斯达四大","createTime":"2018-09-10 17:57:35","updateTime":"2018-09-10 17:57:44","expTime":"2018-12-10 17:55:09","msgPublishStatus":"00","msgPublishType":"01","msgPublishChannel":"02","contentFilter":"阿斯达四大"}]
             * unreadMessageCount : 0
             */

            private PageInfoBean pageInfo;
            private String unreadMessageCount;
            private List<MessageListListBean> messageListList;

            public PageInfoBean getPageInfo() {
                return pageInfo;
            }

            public void setPageInfo(PageInfoBean pageInfo) {
                this.pageInfo = pageInfo;
            }

            public String getUnreadMessageCount() {
                return unreadMessageCount;
            }

            public void setUnreadMessageCount(String unreadMessageCount) {
                this.unreadMessageCount = unreadMessageCount;
            }

            public List<MessageListListBean> getMessageListList() {
                return messageListList;
            }

            public void setMessageListList(List<MessageListListBean> messageListList) {
                this.messageListList = messageListList;
            }

            public static class PageInfoBean {
                /**
                 * pageSize : 2147483647
                 * pageIndex : 1
                 * pageCount : 1
                 * totalCount : 102
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

            public static class MessageListListBean {
                /**
                 * id : 54849
                 * sender : 浙金中心消息推送
                 * msgType : 0
                 * msgStatus : 0
                 * readStatus : 0
                 * title : 1111
                 * content : 阿斯达四大
                 * createTime : 2018-09-10 17:57:35
                 * updateTime : 2018-09-10 17:57:44
                 * expTime : 2018-12-10 17:55:09
                 * msgPublishStatus : 00
                 * msgPublishType : 01
                 * msgPublishChannel : 02
                 * contentFilter : 阿斯达四大
                 */

                private String id;
                private String sender;
                private String msgType;
                private String msgStatus;
                private String readStatus;
                private String title;
                private String content;
                private String createTime;
                private String updateTime;
                private String expTime;
                private String msgPublishStatus;
                private String msgPublishType;
                private String msgPublishChannel;
                private String contentFilter;
                private Boolean isCheck = false;

                public Boolean getCheck() {
                    return isCheck;
                }

                public void setCheck(Boolean check) {
                    isCheck = check;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSender() {
                    return sender;
                }

                public void setSender(String sender) {
                    this.sender = sender;
                }

                public String getMsgType() {
                    return msgType;
                }

                public void setMsgType(String msgType) {
                    this.msgType = msgType;
                }

                public String getMsgStatus() {
                    return msgStatus;
                }

                public void setMsgStatus(String msgStatus) {
                    this.msgStatus = msgStatus;
                }

                public String getReadStatus() {
                    return readStatus;
                }

                public void setReadStatus(String readStatus) {
                    this.readStatus = readStatus;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }

                public String getExpTime() {
                    return expTime;
                }

                public void setExpTime(String expTime) {
                    this.expTime = expTime;
                }

                public String getMsgPublishStatus() {
                    return msgPublishStatus;
                }

                public void setMsgPublishStatus(String msgPublishStatus) {
                    this.msgPublishStatus = msgPublishStatus;
                }

                public String getMsgPublishType() {
                    return msgPublishType;
                }

                public void setMsgPublishType(String msgPublishType) {
                    this.msgPublishType = msgPublishType;
                }

                public String getMsgPublishChannel() {
                    return msgPublishChannel;
                }

                public void setMsgPublishChannel(String msgPublishChannel) {
                    this.msgPublishChannel = msgPublishChannel;
                }

                public String getContentFilter() {
                    return contentFilter;
                }

                public void setContentFilter(String contentFilter) {
                    this.contentFilter = contentFilter;
                }
            }
        }
    }
}
