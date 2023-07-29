package com.urahimli.todolist.ui.todo_list

import com.urahimli.todolist.data.Todo

sealed class TodoListEvent {

    data class OnDeleteTodoClick(val todo: Todo): TodoListEvent()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()
    data class OnTodoClick(val todo: Todo): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    object OnAddTodoClick: TodoListEvent()
}