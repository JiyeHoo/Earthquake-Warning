package com.jiyehoo.informationentry.bean;

import java.util.Date;
import java.util.List;
/**
 * @author JiyeHoo
 * @description: 通用接口查询到的历史 DP 数据
 */
public class HistoryBean {

    private int total;
    private List<String> dpc;
    private List<Dps> dps;
    private boolean hasNext;
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public void setDpc(List<String> dpc) {
        this.dpc = dpc;
    }
    public List<String> getDpc() {
        return dpc;
    }

    public void setDps(List<Dps> dps) {
        this.dps = dps;
    }
    public List<Dps> getDps() {
        return dps;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    public boolean getHasNext() {
        return hasNext;
    }


    public static class Dps {

        private long timeStamp;
        private int dpId;
        private String timeStr;
        private String value;

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }
        public long getTimeStamp() {
            return timeStamp;
        }

        public void setDpId(int dpId) {
            this.dpId = dpId;
        }
        public int getDpId() {
            return dpId;
        }

        public void setTimeStr(String timeStr) {
            this.timeStr = timeStr;
        }
        public String getTimeStr() {
            return timeStr;
        }

        public void setValue(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

    }

}