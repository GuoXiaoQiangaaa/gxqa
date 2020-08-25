package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("input_oa_send_message")
public class InputOASendMessage implements Serializable {

    //OA系统传至税务系统字段
    private Integer id;

    private String REQUEST_ID; //报销单ID
    private String requestId;

    private String BARCODE; //报销单号

    private Integer SHIFOUDZFP; // 0：全部都是电票   1：并非全部都是电票

    private String DOCIDLIST; //如果字段“SHIFOUDZFP”为0，则为必需，否则，留空。字段格式为：docid1,docid2,……,docidN （报销单附件文件的docid顺次以逗号分隔相连）

    //税务系统传至OA系统字段
    private Integer  RETURN_VALUE;   //返回状态  1成功，2失败，3数据异常（  SHIFOUDZFP值不同，必需字段不一样）
    private Integer returnValue;

    private String RETURN_MSG;  //返回详情
    private String returnMsg;

    private String type;   //调阅的票据类型，1、报账单，2、合同号

    private String evaluation; //是否可以发起影像的重扫，1可以评价，0不可以

    private String canDownPrint; //是否可以下载和打印影像，1可以，0或者其他不可以

    private String commentType;  //批注操作标识：0无权看见批注，默认 1可以看见批注 2可看见且能编辑

    private String userid;  //调阅人账号

    private String showother;  //是否显示关联影像,0不显示，1显示，默认0

    private Date createTime;

    private String WEBSITE;

    private String RETURN_FLAG;
    private String returnFlag;

    private String POST_FLAG;
    private String postFlag;
    private String POST_DATE;
    private String postDate;
    private String isProcessing;
    private String INVOICE_AMOUNT;  //return_value=1 返回总张数
    private String INVOICE_VALID_AMOUNT;  //return_value =1 通过验真的张数
    private String isError;

    private String urlContent;

    private String isPassEntry;
    private String isRefund;
    @TableField(exist = false)
    private String flag; //

    private String errorInfo;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIsPassEntry() {
        return isPassEntry;
    }

    public void setIsPassEntry(String isPassEntry) {
        this.isPassEntry = isPassEntry;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public String getIsError() {
        return isError;
    }

    public void setIsError(String isError) {
        this.isError = isError;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPOST_DATE() {
        return POST_DATE;
    }

    public void setPOST_DATE(String POST_DATE) {
        this.POST_DATE = POST_DATE;
    }

    public String getINVOICE_AMOUNT() {
        return INVOICE_AMOUNT;
    }

    public void setINVOICE_AMOUNT(String INVOICE_AMOUNT) {
        this.INVOICE_AMOUNT = INVOICE_AMOUNT;
    }

    public String getINVOICE_VALID_AMOUNT() {
        return INVOICE_VALID_AMOUNT;
    }

    public void setINVOICE_VALID_AMOUNT(String INVOICE_VALID_AMOUNT) {
        this.INVOICE_VALID_AMOUNT = INVOICE_VALID_AMOUNT;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getPOST_FLAG() {
        return POST_FLAG;
    }

    public void setPOST_FLAG(String POST_FLAG) {
        this.POST_FLAG = POST_FLAG;
    }

    public String getRETURN_FLAG() {
        return RETURN_FLAG;
    }

    public void setRETURN_FLAG(String RETURN_FLAG) {
        this.RETURN_FLAG = RETURN_FLAG;
    }

    public String getWEBSITE() {
        return WEBSITE;
    }

    public void setWEBSITE(String WEBSITE) {
        this.WEBSITE = WEBSITE;
    }

    public String getRETURN_MSG() {
        return RETURN_MSG;
    }

    public void setRETURN_MSG(String RETURN_MSG) {
        this.RETURN_MSG = RETURN_MSG;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public Integer getSHIFOUDZFP() {
        return SHIFOUDZFP;
    }

    public void setSHIFOUDZFP(Integer SHIFOUDZFP) {
        this.SHIFOUDZFP = SHIFOUDZFP;
    }

    public String getDOCIDLIST() {
        return DOCIDLIST;
    }

    public void setDOCIDLIST(String DOCIDLIST) {
        this.DOCIDLIST = DOCIDLIST;
    }

    public Integer getRETURN_VALUE() {
        return RETURN_VALUE;
    }

    public void setRETURN_VALUE(Integer RETURN_VALUE) {
        this.RETURN_VALUE = RETURN_VALUE;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getCanDownPrint() {
        return canDownPrint;
    }

    public void setCanDownPrint(String canDownPrint) {
        this.canDownPrint = canDownPrint;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getShowother() {
        return showother;
    }

    public void setShowother(String showother) {
        this.showother = showother;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Integer returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
    }

    public String getUrlContent() {
        return urlContent;
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
    }
}
