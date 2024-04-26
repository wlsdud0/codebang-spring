package com.example.project.user.service;

import com.example.project.user.dto.UserDTO;
import com.example.project.user.entity.UserEntity;
import com.example.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    // 생성자 주입
    private final UserRepository userRepository;

    // Entity에 저장
    public void save(UserDTO userDTO) {
        // DTO -> entity
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);

        // repository의 save메서드 호출
        userRepository.save(userEntity);
    }

    // 로그인
    public UserDTO login(UserDTO userDTO) {
        // id을 DB에서 조회
        Optional<UserEntity> byUserId = userRepository.findByUserId(userDTO.getUserId());

        // 조회한 비밀번호가 사용자가 입력한 값과 일치하는지 확인
        if (byUserId.isPresent()) {
            // 조회 결과가 있을 때
            UserEntity userEntity = byUserId.get();
            if (userEntity.getUserPassword().equals(userDTO.getUserPassword())) {
                // 비밀번호 일치
                // entity -> DTO
                return UserDTO.toUserDTO(userEntity);
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과가 없을 때
            return null;
        }
    }

    // user 조회
    public List<UserDTO> findAll() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            userDTOList.add(UserDTO.toUserDTO(userEntity));
        }

        return userDTOList;
    }

    // user 상세조회
    public UserDTO findById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            return UserDTO.toUserDTO((optionalUserEntity.get()));
        } else {
            return null;
        }
    }

    // user 상세조회 (session)
    public UserDTO findById(String myUserId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(myUserId);
        if (optionalUserEntity.isPresent()) {
            return UserDTO.toUserDTO(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    // 업데이트
    public void update(UserDTO userDTO) {
        userRepository.save(UserEntity.toUpdateUserEntity(userDTO));
    }

    // 삭제
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // userId-check
    public String userIdCheck(String userId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(userId);
        if (optionalUserEntity.isPresent()) {
            // 조회결과가 있으면 사용할 수 없도록
            return null;
        } else {
            return "ok";
        }
    }
}
