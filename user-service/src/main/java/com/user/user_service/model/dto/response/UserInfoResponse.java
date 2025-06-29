package com.user.user_service.model.dto.response;

import com.user.user_service.model.domain.User;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private String email;
    private String name;

    public UserInfoResponse(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
    }
}