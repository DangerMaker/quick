package com.hundsun.zjfae.activity.mine.bean;

public class FaceIdCardBean {

    /**
     * fh : VIMGMZJ000000J00
     * eh : {"p":"and","MFID":"TjhCeGFiY3BhdHlJT3plOmFwcHVzZXJzbG9naW46LTAwMDAxMjA0OTUP","SMID":"YkFRZjkuMmZkMjU2MzcwNzgxcGxMMHpJVWU6MDAwMDEyMDQ5NS9hcHAG","isShare":"1","scene":"certificate"}
     * body : {"returnCode":null,"returnMsg":null,"name":"*****","id_card_number":"*****","valid_date":null,"number":null,"request_id":"***********","time_used":"322","address":"*******","gender":"男","side":"front","error_message":null,"error":null}
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
         * MFID : TjhCeGFiY3BhdHlJT3plOmFwcHVzZXJzbG9naW46LTAwMDAxMjA0OTUP
         * SMID : YkFRZjkuMmZkMjU2MzcwNzgxcGxMMHpJVWU6MDAwMDEyMDQ5NS9hcHAG
         * isShare : 1
         * scene : certificate
         */

        private String p;
        private String MFID;
        private String SMID;
        private String isShare;
        private String scene;

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

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }
    }

    public static class BodyBean {
        /**
         * returnCode : null
         * returnMsg : null
         * name : ****
         * id_card_number : ******
         * valid_date : null
         * number : null
         * request_id : ******
         * time_used : ***
         * address : ******
         * gender : 男
         * side : front
         * error_message : null
         * error : null
         */

        private Object returnCode;
        private Object returnMsg;
        private String name;
        private String id_card_number;
        private Object valid_date;
        private Object number;
        private String request_id;
        private String time_used;
        private String address;
        private String gender;
        private String side;
        private Object error_message;
        private Object error;

        public Object getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(Object returnCode) {
            this.returnCode = returnCode;
        }

        public Object getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(Object returnMsg) {
            this.returnMsg = returnMsg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId_card_number() {
            return id_card_number;
        }

        public void setId_card_number(String id_card_number) {
            this.id_card_number = id_card_number;
        }

        public Object getValid_date() {
            return valid_date;
        }

        public void setValid_date(Object valid_date) {
            this.valid_date = valid_date;
        }

        public Object getNumber() {
            return number;
        }

        public void setNumber(Object number) {
            this.number = number;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getTime_used() {
            return time_used;
        }

        public void setTime_used(String time_used) {
            this.time_used = time_used;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public Object getError_message() {
            return error_message;
        }

        public void setError_message(Object error_message) {
            this.error_message = error_message;
        }

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }
    }
}
