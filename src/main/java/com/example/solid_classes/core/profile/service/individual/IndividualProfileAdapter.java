package com.example.solid_classes.core.profile.service.individual;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.ports.IndividualProfilePort;
import com.example.solid_classes.core.profile.repository.IndividualProfileRepository;

@Service
public class IndividualProfileAdapter extends NamedCrudAdapter<IndividualProfile, IndividualProfileRepository> implements IndividualProfilePort {
    
    public IndividualProfileAdapter(IndividualProfileRepository individualProfileRepository) {
        super(individualProfileRepository, "Perfil Individual");
    }

    @Override
    public IndividualProfile getByCpf(String cpf) {
        return repository.findByCpf(cpf).orElseThrow(this::throwEntityNotFound);
    }
}
