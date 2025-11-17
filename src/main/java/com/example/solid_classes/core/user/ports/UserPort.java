package com.example.solid_classes.core.user.ports;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.user.model.User;

public interface UserPort extends NamedCrudPort<User> {

    User getByEmail(String email);

    User getLoggedInUser();

}
