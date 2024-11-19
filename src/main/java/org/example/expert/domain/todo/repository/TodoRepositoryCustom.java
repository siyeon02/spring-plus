package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearch;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepositoryCustom {
    Optional<Todo> findByIdWithUser(Long todoId);
    Page<Todo> findByWeather(@Param("weather") String weather, Pageable pageable);
    Page<TodoSearch> searchTodos(
            String title,
            String email,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
