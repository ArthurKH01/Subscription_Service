package com.example.test_task.subscriptionservice.repository;

import com.example.test_task.subscriptionservice.model.entity.RetryableTask;
import com.example.test_task.subscriptionservice.model.enums.retryableTask.RetryableTaskStatus;
import com.example.test_task.subscriptionservice.model.enums.retryableTask.RetryableTaskType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RetryableTaskRepository extends JpaRepository<RetryableTask, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RetryableTask  r WHERE r.type = :type " +
            "AND r.retryTime <= :retryTime " +
            "AND r.status = :status " +
            "ORDER BY r.retryTime ASC")
    List<RetryableTask> findRetryableTaskForProcessing(@Param("type") RetryableTaskType type,
                                                       @Param("retryTime") Instant retryTime,
                                                       @Param("status") RetryableTaskStatus status,
                                                       Pageable pageable);
}
