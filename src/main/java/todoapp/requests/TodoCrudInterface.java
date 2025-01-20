package todoapp.requests;

import todoapp.models.Todo;

import java.util.List;

public interface TodoCrudInterface {
    void create(Todo todo);
    List<Todo> read();
    void update(Todo todo);
    void delete(String id);
}