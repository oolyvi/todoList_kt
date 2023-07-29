package com.urahimli.todolist.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.urahimli.todolist.data.Todo
import com.urahimli.todolist.data.TodoRepository
import com.urahimli.todolist.util.Routes
import com.urahimli.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    // todolistscreen'de state olaraq almaq ucun yazdiq burada
    val todos = repository.getTodos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    //recently deleted todo'nu bildirir, undo sohbetinde isimize yarayacaq
    private var deletedTodo: Todo? = null

    // 1 event fun ile butun event sohbetini hell edirik
    // bu function'i da screen'de istifade edirik
    fun onEvent(event: TodoListEvent) {
        when(event) {
            is TodoListEvent.OnTodoClick -> {
                //todo id gonderirik ki add_edit_screen'de bos textfield'ler gormeyek
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnUndoDeleteClick -> {
                // null deyilse sildiyimizi undo edirik
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)    //yeniden database'e qayidir todo
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Todo was deleted",
                        action = "Undo"
                    ))
                }
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        // id'sini saxlayib movcud olani update edir
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        // her 2 ekranda ola bilecek one time eventleri yaziriq, send() teleb edirdi bunu
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}