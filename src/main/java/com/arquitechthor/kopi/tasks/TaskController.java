package com.arquitechthor.kopi.tasks;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("groupedTasks", taskService.getTasksGroupedByCategory());
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        return taskService.getTaskById(id)
                .map(task -> {
                    model.addAttribute("task", task);
                    return "tasks/view";
                })
                .orElse("redirect:/tasks");
    }

    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/form";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model) {
        return taskService.getTaskById(id)
                .map(task -> {
                    model.addAttribute("task", task);
                    return "tasks/form";
                })
                .orElse("redirect:/tasks");
    }

    @PostMapping
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/form";
        }
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/move/{direction}")
    public String moveTask(@PathVariable Long id, @PathVariable String direction) {
        taskService.moveTask(id, direction);
        return "redirect:/tasks";
    }
}
