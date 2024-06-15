package com.example.demo.services;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void deleteTaskById(Long id) {
        taskRepository.delete(loadTask(id));
    }

    public Set<Task> getTasks(User user, Priority priority)
    {
        Set<Task> tasks = user.getTasks();
        if (priority != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getPriority().equals(priority))
                    .collect(Collectors.toSet());
        }
        return tasks;
    }

    public void changeStatus(Long id)
    {
        var task = loadTask(id);
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
    }

    public void changePriority(Long id, Priority priority)
    {
        var task = loadTask(id);
        task.setPriority(priority);
        taskRepository.save(task);
    }

    private Task loadTask(Long id)
    {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isEmpty())
            throw new IllegalArgumentException(String.format("Task %d not found", id));
        return task.get();
    }
}
