package com.example.demo.api;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CustomUserDetailsService;
import com.example.demo.services.TaskService;
import com.example.demo.utils.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskRESTController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Set<Task>> listTasks(@RequestParam(required = false) Priority priority, Principal principal) {
        Set<Task> tasks = taskService.getTasks(userRepository.findByUsername(principal.getName()), priority);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTaskById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        try {
            taskService.changeStatus(id);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/changePriority/{id}")
    public ResponseEntity<?> changePriority(@PathVariable Long id, @RequestParam Priority priority) {
        try {
            taskService.changePriority(id, priority);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody Task task, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        task.setUser(user);
        task.setCompleted(false);
        taskRepository.save(task);
        return ResponseEntity.ok().build();
    }
}