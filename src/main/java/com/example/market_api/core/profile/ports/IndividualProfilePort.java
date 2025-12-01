package com.example.market_api.core.profile.ports;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

public interface IndividualProfilePort extends NamedCrudPort<IndividualProfile> {

    IndividualProfile getByCpf(String cpf);
}
