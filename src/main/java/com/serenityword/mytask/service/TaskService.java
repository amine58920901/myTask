package com.serenityword.mytask.service;

import com.serenityword.mytask.domain.Task;
import com.serenityword.mytask.domain.User;
import com.serenityword.mytask.exception.domain.DuplicateTaskException;
import com.serenityword.mytask.exception.domain.TaskNotFoundException;
import com.serenityword.mytask.exception.domain.UserNotFoundException;
import com.serenityword.mytask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class TaskService {


    private TaskRepository taskRepository;
    private UserService userService;

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }


    @Autowired
    public TaskService(TaskRepository taskRepository , UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }



    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }




    public Task createTask(Task task, String username) throws UserNotFoundException {
        // Vérifier si une tâche avec le même titre existe déjà
        if (taskRepository.findTaskByTitle(task.getTitle()) != null) {
            throw new DuplicateTaskException("Task with title " + task.getTitle() + " already exists");
        }

        // Définir la date de création
        task.setCreatedAt(LocalDateTime.now());

        // Récupérer l'utilisateur actuellement authentifié
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Récupérer l'utilisateur à partir du nom d'utilisateur
        User user = userService.findUserByUsername(username);

        // Vérifier si l'utilisateur existe
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        // Associer l'utilisateur à la tâche
        task.setUser(user);
        task.setUsername(username);

        return taskRepository.save(task);
    }



    public Task updateTask(Task task) {
        // Vérifier si la tâche existe
        Task existingTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + task.getId()));

        // Mettre à jour les propriétés de la tâche existante
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setUpdatedAt(LocalDateTime.now());
        existingTask.setDateTask(task.getDateTask());

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        // Vérifier si la tâche existe
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }

    public List<Task> getAllTasksByUsername(String username) {
        // ...
        return taskRepository.findTasksByUsername(username);
    }

}
