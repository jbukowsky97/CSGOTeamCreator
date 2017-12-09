package com.hardboiled.csgoteamcreator;

/**
 * Created by jonah on 12/3/17.
 */

public class User {

    private String uid;
    private String username;
    private int rankId;
    private String eseaName;
    private String eseaRank;
    private String role;
    private String weapon;
    private String team;
    private boolean leader;
    private String url;

    public User(String uid, String username, int rankId, String eseaName, String eseaRank, String role, String weapon, String team, boolean leader, String url) {
        this.uid = uid;
        this.username = username;
        this.rankId = rankId;
        this.eseaName = eseaName;
        this.eseaRank = eseaRank;
        this.role = role;
        this.weapon = weapon;
        this.team = team;
        this.leader = leader;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public int getRankId() {
        return rankId;
    }

    public String getEseaName() {
        return eseaName;
    }

    public String getEseaRank() {
        return eseaRank;
    }

    public String getRole() {
        return role;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getTeam() {
        return team;
    }

    public boolean isLeader() {
        return leader;
    }

    public String getUrl() {
        return url;
    }
}
