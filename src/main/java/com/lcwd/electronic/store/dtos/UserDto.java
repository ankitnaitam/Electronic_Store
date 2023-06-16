package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserDto extends CustomFieldsDto {

    private String userId;

    @Size(min = 3, max = 30, message = "Invalid name !!")
    private String name;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]+@(.+)$", message = "Invalid email address !!")
    private String email;

    @NotBlank
    @Size(min = 5, max = 15, message = "Password should contain min 5 and max 15 characters !!")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImageNameValid
    private String imageName;
}
