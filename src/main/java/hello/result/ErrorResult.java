package hello.result;

public class ErrorResult extends Result<Void> {
    protected ErrorResult(String msg) {
        super("fail", msg, null);
    }

    public static ErrorResult fail(String msg) {
        return new ErrorResult(msg);
    }
}