package org.learncode.aama.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContactResponseDto {
    private long totalUsers;
    private List<ContactDto> contacts;

    public ContactResponseDto(long totalUsers, List<ContactDto> contacts) {
        this.totalUsers = totalUsers;
        this.contacts = contacts;
    }

}
