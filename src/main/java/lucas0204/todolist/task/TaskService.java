package lucas0204.todolist.task;

import lucas0204.todolist.Utils.TodolistUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    public TaskDto createTask(TaskDto taskDto) throws HttpClientErrorException {
        validateTaskDto(taskDto);
        return taskRepository.save(taskDto);
    }

    public List<TaskDto> getUserTasks(UUID userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public TaskDto updateTask(TaskDto taskDto, UUID userId) throws HttpClientErrorException, NoSuchElementException {
        validateTaskDto(taskDto);
        var savedTask = taskRepository.findById(taskDto.getId()).orElse(null);

        if (savedTask == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Task to edit not found");
        }
        if (!savedTask.getUserId().equals(userId)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User does not have permission to edit this task");
        }

        TodolistUtils.copyNonNullValues(taskDto, savedTask);
        return taskRepository.save(savedTask);
    }

    private void validateTaskDto(TaskDto taskDto) throws HttpClientErrorException {
        var currentDate = LocalDateTime.now();
        var startAt = taskDto.getStartAt();
        var endAt = taskDto.getEndAt();
        if (startAt != null && currentDate.isAfter(startAt)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Start date cannot be before current date!");
        }
        if (endAt != null && currentDate.isAfter(endAt)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "End date cannot be before current date!");
        }
        if (startAt != null && endAt != null && startAt.isAfter(endAt)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "End date cannot be before start date!");
        }
    }
}
