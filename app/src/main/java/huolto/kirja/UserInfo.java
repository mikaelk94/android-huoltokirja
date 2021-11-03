package huolto.kirja;

public class UserInfo {

    String username, userpassword;

    public UserInfo() {

    }

    public UserInfo(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
}
