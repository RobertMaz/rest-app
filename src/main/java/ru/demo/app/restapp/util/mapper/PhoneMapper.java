package ru.demo.app.restapp.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.web.dto.PhoneDto;

@Mapper
public interface PhoneMapper {

  PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

  PhoneDto phoneToPhoneDto(Phone phone);
}
