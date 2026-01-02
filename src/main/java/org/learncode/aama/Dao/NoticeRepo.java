package org.learncode.aama.Dao;

import org.learncode.aama.entites.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepo extends JpaRepository<Notice, Long> {
    Notice getNoticeByPurpose(String purpose);
}
