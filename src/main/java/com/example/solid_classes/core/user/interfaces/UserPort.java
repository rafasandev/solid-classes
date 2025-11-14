package com.example.solid_classes.core.user.interfaces;

import com.example.solid_classes.common.interfaces.NamedCrudPort;
import com.example.solid_classes.core.user.model.User;

public interface UserPort extends NamedCrudPort<User> {

    User getByEmail(String email);

    User getLoggedInUser();

}
