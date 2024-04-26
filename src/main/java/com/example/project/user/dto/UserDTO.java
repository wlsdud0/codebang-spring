package com.example.project.user.dto;

import com.example.project.user.entity.UserEntity;
import com.example.project.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.User;

@Getter
@Setter
@NoArgsConstructor
@ToString
// DTO: 필드로 저장한 데이터를 전달
public class UserDTO {
    private Long id;

    @NotBlank(message = "아이디가 비어있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String userPassword;
    private String userPasswordCheck;

    private String userName;
    private String userEmail;
    private String userPhone;

    private UserRole role;


    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setUserPassword(userEntity.getUserPassword());
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setUserEmail(userEntity.getUserEmail());
        userDTO.setUserPhone(userEntity.getUserPhone());

        return userDTO;
    }
}
