package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearch;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepositoryImpl implements TodoRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    public TodoRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = queryFactory.selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()  // User를 fetchJoin으로 가져옴
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Todo> findByWeather(String weather, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        List<Todo> todos = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()  // User를 fetchJoin으로 가져옴
                .where(weatherEquals(weather))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(todos, pageable,
                () -> queryFactory.selectFrom(todo).fetchCount());
    }

    private BooleanExpression weatherEquals(String weather) {
        return weather != null ? QTodo.todo.weather.eq(weather) : null;
    }

    @Override
    public Page<TodoSearch> searchTodos(
            String title,
            String email,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ){
        QTodo todo = QTodo.todo;

        List<TodoSearch> results = queryFactory
                .select(Projections.constructor(
                        TodoSearch.class,
                        todo.title,
                        todo.user.email

                ))
                .from(todo)
                .leftJoin(todo.user)
                .where(
                        titleContains(title),
                        emailContains(email),
                        createdAtBetween(startDate, endDate)
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        titleContains(title),
                        emailContains(email),
                        createdAtBetween(startDate, endDate)
                )
                .fetchOne();
        return new PageImpl<>(results, pageable, total);

    }

    private BooleanExpression titleContains(String title){
        return StringUtils.hasText(title) ? QTodo.todo.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression emailContains(String email){
        return (email!=null&& !email.isEmpty()) ? QTodo.todo.user.email.containsIgnoreCase(email) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime startDate, LocalDateTime endDate){

        if(isValid(startDate)&&isValid(endDate)){
            return QTodo.todo.createdAt.between(startDate, endDate);
        } else if(isValid(startDate)){
            return QTodo.todo.createdAt.goe(startDate);
        } else if(isValid(endDate)){
            return QTodo.todo.createdAt.loe(endDate);
        } else{
            return null;
        }
    }

    private boolean isValid(LocalDateTime dateTime) {
        return dateTime != null;
    }
}
