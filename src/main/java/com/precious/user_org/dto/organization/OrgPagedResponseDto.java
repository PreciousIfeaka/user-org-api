package com.precious.user_org.dto.organization;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
public class OrgPagedResponseDto {
    private final List<OrganizationResponseDto> organizations;

    private final int page;

    private final int limit;

    private final long total;

    public OrgPagedResponseDto(Page<OrganizationResponseDto> pagedData) {
        this.organizations = pagedData.getContent();
        this.page = pagedData.getNumber() + 1;
        this.limit = pagedData.getSize();
        this.total = pagedData.getTotalElements();
    }

}
