package cn.hgy.readfundus.common;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String info;

    private T data;

    public static <T> Result<T> success(T object) {
        Result<T> r = new Result<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.info = msg;
        r.code = 0;
        return r;
    }
}
