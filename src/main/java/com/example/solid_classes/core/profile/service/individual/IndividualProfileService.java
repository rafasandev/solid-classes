package com.example.solid_classes.core.profile.service.individual;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.profile.interfaces.IndividualProfilePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndividualProfileService {
    
    private final IndividualProfilePort individualPort;
}
