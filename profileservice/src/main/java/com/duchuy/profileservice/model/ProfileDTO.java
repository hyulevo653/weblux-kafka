package com.duchuy.profileservice.model;

import com.duchuy.profileservice.data.Profile;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private long id;
    private String email;
    private String status;
    private double initialBalance;
    private String name;
    private String role;
    public static Profile dtoToEntity(ProfileDTO profileDTO){
        Profile profile = new Profile();
        profile.setId(profileDTO.getId());
        profile.setName(profileDTO.getName());
        profile.setRole(profileDTO.getRole());
        profile.setStatus(profileDTO.getStatus());
        profile.setEmail(profileDTO.getEmail());
        return profile;
    }
    public static ProfileDTO entityToDto(Profile profile){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setStatus(profile.getStatus());
        profileDTO.setName(profile.getName());
        profileDTO.setRole(profile.getRole());
        return profileDTO;
    }
}
