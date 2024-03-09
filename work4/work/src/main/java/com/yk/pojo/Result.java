package com.yk.pojo;

//统一响应结果


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结果实体类
 * @author 12080
 * @param <T> 泛型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 业务状态码 0-成功 1-失败
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应总数
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 快速返回操作成功响应结果（带响应数据）
     * @param data 响应数据
     * @param <E> 泛型
     * @return 操作成功
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 快速返回操作成功响应结果
     * @return 操作成功响应结果
     */
    public static Result success() {
        return new Result<>(0, "操作成功", null);
    }

    /**
     * 快速返回操作失败响应
     * @param message 信息
     * @return 返回操作失败响应
     */
    public static Result error(String message) {
        return new Result<>(1, message, null);
    }


    /**
     * 返回带有数据和总数的成功响应
     * @param data 数据
     * @param total 总数
     * @param <E> 泛型
     * @return 返回带有数据和总数的成功响应
     */
    public static <E> Result successWithTotal(E data, int total) {
        return new Result<>(0, "操作成功", data, total);
    }
}
