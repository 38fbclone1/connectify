package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Messenger;
import com.connectify.connectify.enums.EMessengerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessengerRepository extends JpaRepository<Messenger, String> {
    @Query(value = "SELECT m.* " +
            "FROM messengers m " +
            "JOIN accounts_messengers am1 ON m.id = am1.messenger_id " +
            "JOIN accounts mem1 ON am1.account_id = mem1.id " +
            "JOIN accounts_messengers am2 ON m.id = am2.messenger_id " +
            "JOIN accounts mem2 ON am2.account_id = mem2.id " +
            "WHERE mem1.id = :member2 " +
            "AND mem2.id = :member1 " +
            "AND m.type = 'PERSONAL'",
            nativeQuery = true)
    Optional<Messenger> findPersonalByMembers(@Param("member1") String member1, @Param("member2") String member2);


}
