package com.example.market_api.core.user.ports;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.user.model.User;

public interface UserPort extends NamedCrudPort<User> {

    User getByEmail(String email);

    User getLoggedInUser();

}
