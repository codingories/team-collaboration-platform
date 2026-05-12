package hello.result;

import hello.entity.User;

public class LoginResult extends Result<User>{
    boolean isLogin;

    protected LoginResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static Result success(String msg, boolean isLogin) {
        return success(msg, isLogin, null);
    }

    public static Result success(String msg, boolean isLogin, User user) {
        return new LoginResult("ok", msg, user, isLogin);
    }

    public Boolean isLogin() {
        return isLogin;
    }

    public static Result fail(String msg) {
        return new LoginResult("fail", msg, null, false);
    }
}
