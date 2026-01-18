package com.softpunk.dao;

import com.softpunk.dao.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(nativeQuery = true, value = """
       SELECT count(*) from transports
       """)
    long getTransportCount();

    @Query(nativeQuery = true, value = """
       SELECT t.*
       FROM transports t
       OFFSET ?1
       LIMIT ?2
       """)
    List<Transport> getTransportPageable(long offset, int size);
}
