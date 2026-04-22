package hello.entity;

public class Result {
    String status;
    String msg;
    boolean isLogin;
    Object data;

    public static Result fail(String msg) {
        return new Result("fail", msg, false);
    }

    // 成功（无数据）
    public static Result ok(String msg, boolean isLogin) {
        return new Result("ok", msg, isLogin, null);
    }

    // 成功（带数据，如用户信息）
    public static Result ok(String msg, boolean isLogin, Object data) {
        return new Result("ok", msg, isLogin, data);
    }

    public Result(String status, String msg, boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    private Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}