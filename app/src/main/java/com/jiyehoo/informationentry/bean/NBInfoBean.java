package com.jiyehoo.informationentry.bean;

/**
 * 扫码得到的 url，请求通用接口得到的 json
 */
public class NBInfoBean {

    private ActionData actionData;
    private String actionName;
    public void setActionData(ActionData actionData) {
        this.actionData = actionData;
    }
    public ActionData getActionData() {
        return actionData;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public String getActionName() {
        return actionName;
    }

    public static class ActionData {

        private String id;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

    }

}


