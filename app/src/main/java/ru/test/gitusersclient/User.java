package ru.test.gitusersclient;


public class User {

    private String login;
    private String avatar_url;

    public User( String login, String avatar_url) {
        this.login = login;
        this.avatar_url = avatar_url;
    }

    public String getlogin() {
        return login;
    }

    public void setlogin(String login) {
        this.login = login;
    }

    public String getavatar() {
        return avatar_url;
    }

    public void setavatar(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @Override
    public String toString() {
        return  " login=" + login + ", avatar=" + avatar_url;
    }
}
