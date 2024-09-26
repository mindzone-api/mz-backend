package com.mindzone.util.converters;

import com.mindzone.dto.response.listed.ListedProfessional;
import com.mindzone.model.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Date;

public class UserToListedProfessional implements Converter<User, ListedProfessional> {
    @Override
    public ListedProfessional convert(MappingContext<User, ListedProfessional> context) {
        User source = context.getSource();
        ListedProfessional destination = new ListedProfessional();
        destination.setName(source.getName());
        destination.setId(source.getId());
        destination.setActive(source.getProfessionalInfo().getActiveUntil().after(new Date()));
        destination.setProfilePictureURL(source.getProfilePictureURL());
        destination.setProfession(source.getProfessionalInfo().getProfession());
        destination.setTherapyModalities(source.getProfessionalInfo().getTherapyModalities());
        destination.setPricePerSession(source.getProfessionalInfo().getPricePerSession());

        return destination;
    }
}

