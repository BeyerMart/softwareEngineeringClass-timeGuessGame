package at.qe.skeleton.controller;

import at.qe.skeleton.model.VirtualUser;
import at.qe.skeleton.model.VirtualUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.text.ParseException;


@Controller
public class VirtualUserController {
    @Autowired
    ModelMapper modelMapper;

    public VirtualUserDto convertToDto(VirtualUser user) {
        return modelMapper.map(user, VirtualUserDto.class);
    }

    public VirtualUser convertToEntity(VirtualUserDto userDto) throws ParseException {
        return modelMapper.map(userDto, VirtualUser.class);
    }
}
