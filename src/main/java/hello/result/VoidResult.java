package hello.result;

public class VoidResult extends Result<Void> {
    private VoidResult(String status, String msg) {
        super(status, msg, null);
    }

    public static VoidResult ok(String msg) {
        return new VoidResult("ok", msg);
    }

    public static VoidResult fail(String msg) {
        return new VoidResult("fail", msg);
    }
}