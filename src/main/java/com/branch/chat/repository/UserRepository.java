package com.branch.chat.repository;

import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<BranchUser,Long> {
    BranchUser findByUsername(String userName);

    @Query("SELECT u.username FROM BranchUser u  WHERE u.isOnline = ?1 and u.isQuestioning = ?2 and u.userType = ?3")
    Set<String> getUsersQuestioning(Boolean isOnline, Boolean isQuestionning, UserType userType);
}
