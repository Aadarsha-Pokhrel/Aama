package org.learncode.aama.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private Long userID;
    private String name;
    private String phonenumber;
    private depositDto depositDto;
}
