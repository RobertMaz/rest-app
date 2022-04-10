package ru.demo.app.restapp.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.dto.UserFullResponse;

@Mapper(uses = PhoneMapper.class)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserFullResponse userToFullUserResponse(User person);
}