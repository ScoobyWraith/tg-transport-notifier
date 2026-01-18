package com.softpunk.dao;

import com.softpunk.dao.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByCompleteFalse();

    @Query(nativeQuery = true, value = """
       SELECT count(sh.id) FROM schedule sh
       WHERE sh.user_id = ?1 AND sh.complete = false
       """)
    long countByUserIdAndCompleteFalse(long userId);

    @Query(nativeQuery = true, value = """
       SELECT count(sh.id) FROM schedule sh
       WHERE sh.user_id = ?1 AND sh.complete = false AND sh.transport_id = ?2
       """)
    long countByUserIdAndTransportIdCompleteFalse(long userId, long transportId);

    @Query(nativeQuery = true, value = """
       SELECT * FROM schedule
       WHERE user_id = ?1 AND complete = false
       ORDER BY id DESC
       OFFSET ?2
       LIMIT ?3
       """)
    List<Schedule> findAllActiveScheduleForUserPage(long userId, long offset, int size);


}
