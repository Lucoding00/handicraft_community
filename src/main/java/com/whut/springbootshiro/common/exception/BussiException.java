package com.whut.springbootshiro.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BussiException extends RuntimeException {

    private Integer code; // 异常码

    private String message; // 异常信息

}
