package com.app.cikmon.market.service;

import com.app.cikmon.market.model.User;

/**
 * Service class for {@link com.app.cikmon.market.model.User}
 *
 * @author cikmon
 * @version 1.0
 */

public interface UserService {

    void save(User user);

    User findByUsername(String username);
}
