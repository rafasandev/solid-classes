package com.example.solid_classes.core.profile.interfaces;

import com.example.solid_classes.common.interfaces.NamedCrudPort;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;

public interface IndividualProfilePort extends NamedCrudPort<IndividualProfile> {

    IndividualProfile getByCpf(String cpf);
}
