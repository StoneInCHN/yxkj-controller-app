package com.yxkj.controller.beans;


public class CommandRequest {

    private Long commandId;
    private Boolean success;

    private String extMsg;

    public CommandRequest() {
    }


    public CommandRequest(Long commandId, Boolean isSuccess, String extMsg) {
        this.commandId = commandId;
        this.success = isSuccess;
        this.extMsg = extMsg;
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        success = success;
    }

    public String getExtMsg() {
        return extMsg;
    }

    public void setExtMsg(String extMsg) {
        this.extMsg = extMsg;
    }
}
