package com.example.springserver.models.mapper;

import com.example.springserver.dto.UserDto;
import com.example.springserver.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default User userDtoToUser(UserDto userDto){
        User user = User.builder().
                fullName(userDto.getFullName()).
                username(userDto.getUsername()).
                emailAddress(userDto.getEmailAddress()).
                password(userDto.getPassword()).
                build();
        return user;
    }
}
