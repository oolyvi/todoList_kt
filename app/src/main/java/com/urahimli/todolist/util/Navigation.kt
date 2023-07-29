package com.urahimli.todolist.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.urahimli.todolist.ui.add_edit_todo.AddEditTodoScreen
import com.urahimli.todolist.ui.todo_list.TodoListScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.TODO_LIST
    ) {
        composable(Routes.TODO_LIST) {
            TodoListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
            arguments = listOf(
                navArgument(name = "todoId") {
                    type = NavType.IntType
                    defaultValue =
                        -1             //hansi ki add_edit_viewmodel'da da -1 olma logicini yazmisdiq
                }
            )
        ) {
            AddEditTodoScreen(
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }
    }
}