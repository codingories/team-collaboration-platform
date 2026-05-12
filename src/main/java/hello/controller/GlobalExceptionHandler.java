package hello.controller;

import hello.result.ErrorResult;
import hello.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("参数错误");

        return ErrorResult.fail(errorMsg);  // ✅ 用已有的具体子类
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleNotReadableException(HttpMessageNotReadableException ex) {
        return ErrorResult.fail("请求参数格式错误");
    }
}