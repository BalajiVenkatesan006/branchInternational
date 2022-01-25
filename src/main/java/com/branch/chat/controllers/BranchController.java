package com.branch.chat.controllers;

import com.branch.chat.datalayer.MessageDataLayer;
import com.branch.chat.datalayer.UserDataAccess;
import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import com.branch.chat.models.data.Message;
import com.branch.chat.models.output.ActiveUsersOutput;
import com.branch.chat.models.output.ApiOutput;
import com.branch.chat.models.output.MessageOutput;
import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
public class BranchController {

    @Autowired
    UserDataAccess userDataAccess;

    @Autowired
    MessageDataLayer messageDataLayer;

    @GetMapping("/registration/{userName}/{userType}")
    public ResponseEntity<ApiOutput> register(@PathVariable String userName, @PathVariable String userType) {
        System.out.println("handling register user request: " + userName);
        ApiOutput output = new ApiOutput();
        try {
            BranchUser u = userDataAccess.getUser(userName);
            if(u == null){
                userDataAccess.saveUser(getUser(userName, userType));
            }
            else{
                output.setSuccess(false);
                output.setErrorMessage("User Exists");
                return ResponseEntity.ok(output);
            }
        } catch (Exception e) {
            output.setSuccess(false);
            output.setErrorMessage("Exception");
            return ResponseEntity.ok(output);
        }
        output.setSuccess(true);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/login/{userName}/{userType}")
    public ResponseEntity<ApiOutput> login(@PathVariable String userName, @PathVariable String userType) {
        System.out.println("handling login user request: " + userName);
        ApiOutput output = new ApiOutput();
        try {
            BranchUser u = userDataAccess.getUser(userName);
            if(u == null){
                output.setSuccess(false);
                output.setErrorMessage("User Does not Exists");
                return ResponseEntity.ok(output);
            }
            else{
                u.setIsOnline(true);
                userDataAccess.saveUser(u);
            }
        } catch (Exception e) {
            output.setSuccess(false);
            output.setErrorMessage("Exception");
            return ResponseEntity.ok(output);
        }
        output.setSuccess(true);
        return ResponseEntity.ok(output);
    }


    @GetMapping("/logout/{userName}/{userType}")
    public ResponseEntity<ApiOutput> logout(@PathVariable String userName, @PathVariable String userType) {
        System.out.println("handling login user request: " + userName);
        ApiOutput output = new ApiOutput();
        try {
            BranchUser u = userDataAccess.getUser(userName);
            if(u == null){
                output.setSuccess(false);
                output.setErrorMessage("User Does not Exists");
                return ResponseEntity.ok(output);
            }
            else{
                u.setIsOnline(false );
                userDataAccess.saveUser(u);
            }
        } catch (Exception e) {
            output.setSuccess(false);
            output.setErrorMessage("Exception");
            return ResponseEntity.ok(output);
        }
        output.setSuccess(true);
        return ResponseEntity.ok(output);
    }


    private BranchUser getUser(String username, String userType){
        BranchUser branchUser = new BranchUser();
        branchUser.setUserType(UserType.valueOf(userType));
        branchUser.setIsOnline(true);
        branchUser.setUsername(username);
        branchUser.setCreatedAt(new Date());
        branchUser.setModifiedAt(new Date());
        return branchUser;
    }

//    @GetMapping("/fetchAllUsers")
//    public Set<String> fetchAll() {
//        return UserStorage.getInstance().getUsers();
//    }

    @GetMapping("/fetchMessages/{userName}")
    public MessageOutput fetchCustomersMessages(@PathVariable String userName){
        MessageOutput messageOutput = new MessageOutput();
        List<Message> messageList = messageDataLayer.getAllMessagesOfUser(userName);
        if(messageList != null && messageList.size() > 0){
            messageOutput.setSuccess(true);
            messageOutput.setMessages(messageList);
            return messageOutput;
        }
        else{
            messageOutput.setSuccess(false);
            return messageOutput;
        }
    }

    @GetMapping("/fetchUsers")
    public ActiveUsersOutput fetchActiveUsers(){
        Set<String> userName = userDataAccess.getUsersQuestioning();
        ActiveUsersOutput activeUsersOutput = new ActiveUsersOutput();
        activeUsersOutput.setSuccess(true);
        activeUsersOutput.setActiveUsers(userName);
        return activeUsersOutput;
    }

    @GetMapping("/updateAgentToUser/{customer}/{agent}")
    public ApiOutput assignAgentToCustomer(@PathVariable String customer, @PathVariable String agent){
        ApiOutput output = new ApiOutput();
        BranchUser u = userDataAccess.getUser(customer);
        if(u.getAssignedAgent()!= null){
            output.setSuccess(false);
        }
        else{
            BranchUser worker = userDataAccess.getUser(agent);
            u.setAssignedAgent(worker.getUsername());
            if(worker.getIsOccupied()== null || !worker.getIsOccupied()){
                worker.setIsOccupied(true);
                userDataAccess.saveUser(u);
                userDataAccess.saveUser(worker);
                output.setSuccess(true);
            }
            else{
                output.setSuccess(false);
            }
        }
        return output;
    }

    @GetMapping("/chatDone/{customer}")
    public ApiOutput fetchActiveUsers(@PathVariable String customer){
        ApiOutput output = new ApiOutput();
        BranchUser u = userDataAccess.getUser(customer);
        BranchUser worker = userDataAccess.getUser(u.getAssignedAgent());
        u.setIsQuestioning(false);
        u.setAssignedAgent(null);
        worker.setIsOccupied(false);
        userDataAccess.saveUser(u);
        userDataAccess.saveUser(worker);
        output.setSuccess(true);
        return output;
    }


}