package com.hardboiled.csgoteamcreator;

/**
 * Created by jonah on 12/3/17.
 */

public class User {

    private String username;
    private int rankId;
    private String role;
    private String eseaRank;

    public User(String username, int rankId, String role, String eseaRank) {
        this.username = username;
        this.rankId = rankId;
        this.role = role;
        this.eseaRank = eseaRank;
    }

    public String getUsername() {
        return username;
    }

    public int getRankId() {
        return rankId;
    }

    public String getRole() {
        return role;
    }

    public String getEseaRank() {
        return eseaRank;
    }
}
