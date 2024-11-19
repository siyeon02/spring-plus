package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TodoSearch {
    private String title;
    private String email;

    public TodoSearch(String title, String email) {
        this.title = title;
        this.email = email;
    }
}
