package com.lcwd.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class CustomFieldsDto {

    private String isActive;

    private String createdBy;

    private LocalDateTime createdOn;

    private String modifiedBy;

    private LocalDateTime modifiedOn;
}
