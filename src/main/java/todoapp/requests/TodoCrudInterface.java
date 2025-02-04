package todoapp.requests;

import todoapp.models.Todo;

import java.util.List;

public interface TodoCrudInterface {
    void create(Todo todo);
    List<Todo> read();
    List<Todo> read(Integer offset, Integer limit);
    void update(String id, Todo todo);
    void delete(String id);
}