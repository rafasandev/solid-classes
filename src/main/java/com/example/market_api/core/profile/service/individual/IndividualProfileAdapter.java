package com.example.market_api.core.profile.service.individual;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.ports.IndividualProfilePort;
import com.example.market_api.core.profile.repository.jpa.IndividualProfileRepository;

@Component
public class IndividualProfileAdapter extends NamedCrudAdapter<IndividualProfile, IndividualProfileRepository> implements IndividualProfilePort {
    
    public IndividualProfileAdapter(IndividualProfileRepository individualProfileRepository) {
        super(individualProfileRepository, "Perfil Individual");
    }

    @Override
    public IndividualProfile getByCpf(String cpf) {
        return repository.findByCpf(cpf).orElseThrow(this::throwEntityNotFound);
    }
}
