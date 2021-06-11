package server.api;

public class MsgChangePassword {
    private long token;
    private String password;

    public MsgChangePassword(long token, String password) {
        this.token = token;
        this.password = password;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
