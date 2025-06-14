package com.precious.user_org.dto.user;

import com.precious.user_org.dto.organization.OrganizationResponseDto;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class UserPagedResponseDto {
    private final List<UserResponseDto> users;

    private final int page;

    private final int limit;

    private final long total;

    public UserPagedResponseDto(Page<UserResponseDto> pagedData) {
        this.users = pagedData.getContent();
        this.page = pagedData.getNumber() + 1;
        this.limit = pagedData.getSize();
        this.total = pagedData.getTotalElements();
    }
}
