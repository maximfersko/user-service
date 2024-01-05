package com.aston.shop.users.mapper;

import com.aston.shop.users.model.dto.UserDto;
import com.aston.shop.users.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDto toDto(User entity);

	User fromDto(UserDto dto);

}
