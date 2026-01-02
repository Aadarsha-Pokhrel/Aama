package org.learncode.aama.Dao;

import org.learncode.aama.entites.NotificationReceipent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRecpRepo extends JpaRepository<NotificationReceipent, Long> {

    List<NotificationReceipent> findNotificationReceipentByUser_UserIDAndStatus(Long userUserID, String status);
}
