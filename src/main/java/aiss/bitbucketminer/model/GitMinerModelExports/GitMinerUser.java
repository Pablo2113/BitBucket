package aiss.bitbucketminer.model.GitMinerModelExports;

public class GitMinerUser {
    private String id;

    private String username;

    private String name;

    private String avatar_url;

    private String web_url;

    private String nickname;

    private String accountStatus;


    public GitMinerUser() {
    }

    public GitMinerUser(String id, String username, String name,
                        String state, String avatar_url, String web_url,  String nickname, String accountStatus) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatar_url = avatar_url;
        this.web_url = web_url;
        this.nickname = nickname;
        this.accountStatus = accountStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}

    public String getAccountStatus() {return accountStatus;}
    public void setAccountStatus(String accountStatus) {this.accountStatus = accountStatus;}

}
