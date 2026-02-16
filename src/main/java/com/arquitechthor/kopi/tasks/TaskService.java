package com.arquitechthor.kopi.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByPriorityAsc();
    }

    public Map<String, List<Task>> getTasksGroupedByCategory() {
        return taskRepository.findAllByOrderByPriorityAsc().stream()
                .collect(Collectors.groupingBy(Task::getCategory));
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        if (task.getPriority() == null) {
            // Assign lowest priority (end of list)
            Integer maxPriority = taskRepository.findAllByOrderByPriorityAsc().stream()
                    .mapToInt(t -> t.getPriority() != null ? t.getPriority() : 0)
                    .max()
                    .orElse(0);
            task.setPriority(maxPriority + 1);
        }
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void moveTask(Long id, String direction) {
        Task task = taskRepository.findById(id).orElseThrow();
        List<Task> allTasks = taskRepository.findAllByOrderByPriorityAsc();
        int index = allTasks.indexOf(task);

        if ("up".equals(direction) && index > 0) {
            swap(task, allTasks.get(index - 1));
        } else if ("down".equals(direction) && index < allTasks.size() - 1) {
            swap(task, allTasks.get(index + 1));
        }
    }

    private void swap(Task t1, Task t2) {
        Integer temp = t1.getPriority();
        t1.setPriority(t2.getPriority());
        t2.setPriority(temp);
        taskRepository.save(t1);
        taskRepository.save(t2);
    }
}
