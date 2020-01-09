package com.moonturns.batuhanaydoner.whereareyoutrump.FirebaseDatabase;

import io.realm.internal.Keep;

@Keep
public class Leader {

    private String player_uid;
    private String leader_username;
    private String leader_score;
    private String reward;
    private String week;

    public String getPlayer_uid() {
        return player_uid;
    }

    public void setPlayer_uid(String player_uid) {
        this.player_uid = player_uid;
    }

    public String getLeader_username() {
        return leader_username;
    }

    public void setLeader_username(String leader_username) {
        this.leader_username = leader_username;
    }

    public String getLeader_score() {
        return leader_score;
    }

    public void setLeader_score(String leader_score) {
        this.leader_score = leader_score;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
