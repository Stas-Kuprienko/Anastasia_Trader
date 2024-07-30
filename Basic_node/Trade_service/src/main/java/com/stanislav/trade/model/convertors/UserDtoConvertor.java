package com.stanislav.trade.model.convertors;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.model.UserDto;
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
