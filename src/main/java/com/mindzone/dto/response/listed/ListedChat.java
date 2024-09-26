package com.mindzone.dto.response.listed;

import com.mindzone.enums.Profession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListedChat implements Serializable {

    private String id;
    private Set<String> usersIds;
}
