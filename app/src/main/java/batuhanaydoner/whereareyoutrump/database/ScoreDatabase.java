package com.moonturns.batuhanaydoner.whereareyoutrump.database;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class ScoreDatabase extends RealmObject {

    private int score;
    private int playing;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlaying() {
        return playing;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }
}
