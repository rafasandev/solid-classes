package com.example.solid_classes.core.profile.service.individual;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.ports.IndividualProfilePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndividualProfileService {
    
    private final IndividualProfilePort individualPort;

    public IndividualProfile registerProfile(IndividualProfile newProfile) {
        return individualPort.save(newProfile);
    }
}
