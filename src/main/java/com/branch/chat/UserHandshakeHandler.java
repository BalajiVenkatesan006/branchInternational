package com.branch.chat;

import com.branch.chat.datalayer.UserDataAccess;
import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);



    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        LOG.info("User with ID '{}' opened the page", randomId);
//        UserDataAccess userDataAccess = new UserDataAccess();
//        userDataAccess.saveUser(getCustomerUserData(randomId));
        return new UserPrincipal(randomId);
    }

    private BranchUser getCustomerUserData(String username){
        BranchUser u = new BranchUser();
        u.setUsername(username);
        u.setUserType(UserType.CUSTOMER);
        u.setCreatedAt(new Date());
        u.setModifiedAt(new Date());
        u.setIsOnline(true);
        return  u;
    }
}
