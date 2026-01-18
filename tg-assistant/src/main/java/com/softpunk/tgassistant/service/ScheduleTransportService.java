package com.softpunk.tgassistant.service;

import com.softpunk.dao.model.Schedule;

import java.util.List;

public interface ScheduleTransportService {
    void scheduleTransport(long userId, long transportId, int delayMinutes);

    List<Schedule> findAllCompleteFalse();

    void saveAll(List<Schedule> list);
}
