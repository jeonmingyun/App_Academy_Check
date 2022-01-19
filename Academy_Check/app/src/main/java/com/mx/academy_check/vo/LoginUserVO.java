package com.mx.academy_check.vo;

import com.mx.academy_check.util.JsonConvertible;

public class LoginUserVO extends JsonConvertible {
    private String login_academy;
    private String login_code;

    public LoginUserVO(String login_academy, String login_code) {
        this.login_academy = login_academy;
        this.login_code = login_code;
    }

    public String getLogin_academy() {
        return login_academy;
    }

    public void setLogin_academy(String login_academy) {
        this.login_academy = login_academy;
    }

    public String getLogin_code() {
        return login_code;
    }

    public void setLogin_code(String login_code) {
        this.login_code = login_code;
    }
}
