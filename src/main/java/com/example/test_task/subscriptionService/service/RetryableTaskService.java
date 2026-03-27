package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.model.entity.Invoices;
import com.example.test_task.subscriptionService.model.entity.RetryableTask;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskStatus;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskType;
import com.example.test_task.subscriptionService.mapper.RetryableTaskMapper;
import com.example.test_task.subscriptionService.repository.RetryableTaskRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryableTaskService {
    private final RetryableTaskRepository retryableTaskRepository;
    private final RetryableTaskMapper retryableTaskMapper;
    @Value("${retryabletask.limit}")
    Integer limit;
    @Value("${retryabletask.timeoutInSeconds}")
    Integer timeoutInSeconds;

    @Transactional
    public void createRetryableTask(Invoices invoice, RetryableTaskType type) {
        RetryableTask retryableTask = retryableTaskMapper.toRetryableTask(invoice, type);
        retryableTaskRepository.save(retryableTask);
    }

    @Transactional
    public List<RetryableTask> createRetryableTasks(List<Invoices> invoices, RetryableTaskType type) {
        var retryableTasks = invoices.stream()
                .map(o->retryableTaskMapper.toRetryableTask(o, type))
                .toList();
        return retryableTaskRepository.saveAll(retryableTasks);
    }

    @Transactional
    public List<RetryableTask> getRetryableTasksForProcessing(RetryableTaskType type) {
        var currentTime = Instant.now();
        Pageable pageable = PageRequest.of(0, limit);
        List<RetryableTask> retryableTasks = retryableTaskRepository.findRetryableTaskForProcessing(
                type,
                Instant.now(),
                RetryableTaskStatus.IN_PROGRESS,
                pageable
        );
        for (RetryableTask retryableTask : retryableTasks) {
            retryableTask.setRetryTime(currentTime.plus(Duration.ofSeconds(timeoutInSeconds)));
        }
        return retryableTasks;
    }

    @Transactional
    public void markRetryableTasksAsCompleted(List<RetryableTask> retryableTasks) {
        for (RetryableTask retryableTask : retryableTasks) {
            retryableTask.setStatus(RetryableTaskStatus.SUCCESS);
        }
        retryableTaskRepository.saveAll(retryableTasks);
    }
}
