package com.anastasia.trade.model.convertors;

import com.anastasia.trade.entities.user.User;
import com.anastasia.trade.model.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConvertor implements Converter<User, UserDto> {

    @Override
    public UserDto convert(@NonNull User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getRole().toString(),
                user.getName());
    }
}
