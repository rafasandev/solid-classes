package com.example.market_api.core.profile.service.individual;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.ports.IndividualProfilePort;
import com.example.market_api.core.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndividualProfileService {
    
    private final IndividualProfilePort individualProfilePort;

    public IndividualProfile getById(UUID profileId) {
        return individualProfilePort.getById(profileId);
    }

    public IndividualProfile getByCpf(String cpf) {
        return individualProfilePort.getByCpf(cpf);
    }

    public IndividualProfile save(IndividualProfile profile) {
        User user = profile.getUser();
        user.setStatus(profile.individualIsAbleToBeActive());
        return individualProfilePort.save(profile);
    }

    public List<IndividualProfile> findAll() {
        return individualProfilePort.findAll();
    }

    public void validateIsActive(IndividualProfile profile) {
        if (!profile.isActive()) {
            throw new BusinessRuleException(
                    "Perfil individual está inativo. Operação negada");
        }
    }
}
