package com.branch.chat.datalayer;

import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import com.branch.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserDataAccess {
    @Autowired
    UserRepository userRepository;

    public BranchUser saveUser(BranchUser user){
        return userRepository.save(user);
    }
    public  BranchUser getUser(String uname){
        return userRepository.findByUsername(uname);
    }

    public Set<String> getUsersQuestioning(){
        return userRepository.getUsersQuestioning(true,true, UserType.CUSTOMER);
    }

}
