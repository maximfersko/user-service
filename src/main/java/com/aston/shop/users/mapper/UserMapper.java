package com.aston.shop.users.mapper;

import com.aston.shop.users.dto.UserDto;
import com.aston.shop.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id")
	@Mapping(target = "username")
	@Mapping(target = "password")
	@Mapping(target = "birthday")
	@Mapping(target = "email")
	UserDto toDto(User entity);

	@Mapping(target = "id")
	@Mapping(target = "username")
	@Mapping(target = "password")
	@Mapping(target = "birthday")
	@Mapping(target = "email")
	User fromDto(UserDto dto);

	@Mapping(target = "id")
	@Mapping(target = "username")
	@Mapping(target = "password")
	@Mapping(target = "birthday")
	@Mapping(target = "email")
	List<UserDto> toDto(List<User> entity);

	@Mapping(target = "id")
	@Mapping(target = "username")
	@Mapping(target = "password")
	@Mapping(target = "birthday")
	@Mapping(target = "email")
	List<User> fromDto(List<UserDto> dto);

}
