package com.namu.auth.provider;

import com.namu.common.dto.StatusDTO;

public interface LoginProvider {
    /**
     * Authorization Code를 사용하여 로그인 처리
     * @param code 인증 코드
     * @return 로그인 처리 결과
     */
    StatusDTO login(String code);
}
