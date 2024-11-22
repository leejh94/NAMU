package com.namu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class StatusDTO {
    private int code;
    private String message;
    private Object data;
}
