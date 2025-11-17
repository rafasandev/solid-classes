package com.example.solid_classes.core.profile.ports;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;

public interface IndividualProfilePort extends NamedCrudPort<IndividualProfile> {

    IndividualProfile getByCpf(String cpf);
}
