package com.example.solid_classes.core.profile.service.company;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.profile.interfaces.CompanyProfilePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfilePort companyPort;

}
