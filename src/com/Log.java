package com;

import java.sql.Date;

/**
 * @date 2020/12/25 12:43
 */
public class Log {
    private Date date_info;
    private String log_info;

    public Date getDate_info() {
        return date_info;
    }

    public void setDate_info(Date date_info) {
        this.date_info = date_info;
    }

    public String getLog_info() {
        return log_info;
    }

    public void setLog_info(String log_info) {
        this.log_info = log_info;
    }
}
