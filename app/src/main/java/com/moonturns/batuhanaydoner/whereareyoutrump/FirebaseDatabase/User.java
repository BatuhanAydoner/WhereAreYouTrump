package com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase;

import io.realm.internal.Keep;

@Keep
public class User {

    private String user_name;
    private String email;
    private String total_score;
    private String user_uid;
    private String game_ticket;
    private String adv;
    private String user_password;
    private String day; //yenileme işlemi olduğunda bu değer 1 artar
    private String total_playing;
    private String high_score;
    private String week_number;
    private String won;

    public User() {
    }

    public User(String user_name, String email, String total_score, String game_ticket, String adv) {
        this.user_name = user_name;
        this.email = email;
        this.total_score = total_score;
        this.game_ticket = game_ticket;
        this.adv = adv;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getGame_ticket() {
        return game_ticket;
    }

    public void setGame_ticket(String game_ticket) {
        this.game_ticket = game_ticket;
    }

    public String getAdv() {
        return adv;
    }

    public void setAdv(String adv) {
        this.adv = adv;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotal_playing() {
        return total_playing;
    }

    public void setTotal_playing(String total_playing) {
        this.total_playing = total_playing;
    }

    public String getHigh_score() {
        return high_score;
    }

    public void setHigh_score(String high_score) {
        this.high_score = high_score;
    }

    public String getWeek_number() {
        return week_number;
    }

    public void setWeek_number(String week_number) {
        this.week_number = week_number;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }
}
