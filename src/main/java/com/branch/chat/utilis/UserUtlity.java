package com.branch.chat.utilis;

import com.branch.chat.datalayer.UserDataAccess;
import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import com.branch.chat.models.input.UserInput;
import com.branch.chat.models.output.ApiOutput;
import com.branch.chat.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class UserUtlity {

    @Autowired
    UserDataAccess userDataAccess;

    public ApiOutput registerUser(UserInput user){
        ApiOutput output = new ApiOutput();
        BranchUser branchUser = new BranchUser();
        branchUser.setUsername(user.getUsername());
        branchUser.setUserType(UserType.AGENT);
        branchUser.setIsOnline(true);
        branchUser.setCreatedAt(new Date());
        branchUser.setModifiedAt(new Date());
        userDataAccess.saveUser(branchUser);
        return output;
    }

    public ApiOutput loginUser(UserInput user){
        ApiOutput output = new ApiOutput();

        return output;
    }

    public ApiOutput logoutUser(UserInput userInput){
        ApiOutput output = new ApiOutput();

        return output;
    }
}
