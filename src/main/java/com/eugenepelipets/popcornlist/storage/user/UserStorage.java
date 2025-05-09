package com.eugenepelipets.popcornlist.storage.user;

import com.eugenepelipets.popcornlist.model.User;

public interface UserStorage {

    User create(User user);

    User update(User user);

    User getUserById(int id);

}