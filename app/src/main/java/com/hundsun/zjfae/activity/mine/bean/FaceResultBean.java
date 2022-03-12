package com.hundsun.zjfae.activity.mine.bean;

public class FaceResultBean {

    /**
     * fh : VIMGMZJ000000J00
     * eh : {"p":"and","MFID":"NUNTb2FiY29ISFpHT21lOmFwcHVzZXJzbG9naW46LTAwMDAxMDMxMTk5","SMID":"VEhDcDAuMDkxZWFjYTE1YjQzVDhNRlJNOjAwMDAxMDMxMTkvYXBwj","isShare":"1","scene":"verify"}
     * body : {"returnCode":"F4002","returnMsg":"人脸识别失败，请稍后操作或者联系客服处理~","name":null,"id_card_number":null,"valid_date":null,"number":null,"request_id":"1540548337,e9a44df8-1046-426a-8d48-bba5e264806a","time_used":"0","address":null,"gender":null,"side":null,"error_message":"INVALID_NAME_FORMAT","error":null}
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
         * MFID : NUNTb2FiY29ISFpHT21lOmFwcHVzZXJzbG9naW46LTAwMDAxMDMxMTk5
         * SMID : VEhDcDAuMDkxZWFjYTE1YjQzVDhNRlJNOjAwMDAxMDMxMTkvYXBwj
         * isShare : 1
         * scene : verify
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
         * returnCode : F4002
         * returnMsg : 人脸识别失败，请稍后操作或者联系客服处理~
         * name : null
         * id_card_number : null
         * valid_date : null
         * number : null
         * request_id : 1540548337,e9a44df8-1046-426a-8d48-bba5e264806a
         * time_used : 0
         * address : null
         * gender : null
         * side : null
         * error_message : INVALID_NAME_FORMAT
         * error : null
         */

        private String returnCode;
        private String returnMsg;
        private Object name;
        private Object id_card_number;
        private Object valid_date;
        private Object number;
        private String request_id;
        private String time_used;
        private Object address;
        private Object gender;
        private Object side;
        private String error_message;
        private Object error;

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

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getId_card_number() {
            return id_card_number;
        }

        public void setId_card_number(Object id_card_number) {
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

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getSide() {
            return side;
        }

        public void setSide(Object side) {
            this.side = side;
        }

        public String getError_message() {
            return error_message;
        }

        public void setError_message(String error_message) {
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
