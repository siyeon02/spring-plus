package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

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
}
