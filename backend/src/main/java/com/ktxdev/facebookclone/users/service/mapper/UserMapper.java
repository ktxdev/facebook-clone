package com.ktxdev.facebookclone.users.service.mapper;

import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    User userCreateDTOToUser(UserCreateDTO userCreateDTO);
}
