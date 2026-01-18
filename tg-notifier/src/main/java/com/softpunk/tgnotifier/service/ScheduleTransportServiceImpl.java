package com.softpunk.tgnotifier.service;

import com.softpunk.dao.ScheduleRepository;
import com.softpunk.dao.TransportRepository;
import com.softpunk.dao.model.Schedule;
import com.softpunk.dao.model.Transport;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ScheduleTransportServiceImpl implements ScheduleTransportService {
    @Autowired
    @Setter
    private ScheduleRepository scheduleRepository;

    @Autowired
    @Setter
    private TransportRepository transportRepository;

    @Override
    public void scheduleTransport(long userId, long transportId, int delayMinutes) {
        Transport transport = transportRepository.findById(transportId)
                .orElseThrow(() -> new RuntimeException(String.format("No transport with id %d.", transportId)));

        Schedule schedule = new Schedule();

        schedule.setTs(Instant.now());
        schedule.setDelay(delayMinutes);
        schedule.setUserId(userId);
        schedule.setTransport(transport);

        scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAllCompleteFalse() {
        return scheduleRepository.findAllByCompleteFalse();
    }

    @Override
    public void saveAll(List<Schedule> list) {
        scheduleRepository.saveAll(list);
    }
}
