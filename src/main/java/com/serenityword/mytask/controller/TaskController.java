package com.serenityword.mytask.controller;

import com.serenityword.mytask.domain.Task;
import com.serenityword.mytask.dto.TaskDTO;
import com.serenityword.mytask.exception.domain.TaskNotFoundException;
import com.serenityword.mytask.exception.domain.UserNotFoundException;
import com.serenityword.mytask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {




    private TaskService taskService;


    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;


    }
    @GetMapping("/list")
    public List<Task> findAllTasks() throws TaskNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // L'utilisateur est authentifié
            // Effectuez les opérations nécessaires
            return taskService.findAllTasks();
        } else {
            // L'utilisateur n'est pas authentifié, renvoyez un message d'erreur ou effectuez une action appropriée
            throw new TaskNotFoundException("Unauthorized");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id) {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /*
    @PostMapping("/add")
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) throws UserNotFoundException {
        String username = authentication.getName(); // Récupérer le nom d'utilisateur de l'authentification
        Task createdTask = taskService.createTask(task, username);
        System.out.println(username);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

     */

    @PostMapping("/add")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO, Authentication authentication) throws UserNotFoundException {
        String username = authentication.getName(); // Récupérer le nom d'utilisateur de l'authentification

        // Créer une instance de la classe Task à partir des données de TaskDTO
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDateTask(taskDTO.getDateTask());

        // Appeler votre service pour créer la tâche
        Task createdTask = taskService.createTask(task, username);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @RequestBody TaskDTO taskUpdateDTO) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(taskUpdateDTO.getTitle());
        task.setDescription(taskUpdateDTO.getDescription());
        task.setDateTask(taskUpdateDTO.getDateTask());
        // autres mises à jour si nécessaire

        Task updatedTask = taskService.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Task>> getAllTasksByUsername(@PathVariable("username") String username) {
        List<Task> tasks = taskService.getAllTasksByUsername(username);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


}