package com.eugenepelipets.popcornlist.mapper;

import org.mapstruct.Mapper;
import com.eugenepelipets.popcornlist.dto.UserRequestDto;
import com.eugenepelipets.popcornlist.dto.UserResponseDto;
import com.eugenepelipets.popcornlist.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);
    UserResponseDto toDto(User user);
}
