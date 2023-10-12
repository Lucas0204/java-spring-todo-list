package lucas0204.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity createTask(@RequestBody TaskDto taskDto, @RequestAttribute(name = "userId") UUID userId) {
        taskDto.setUserId(userId);
        try {
            var task = taskService.createTask(taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskDto taskDto,
                                     @PathVariable UUID id,
                                     @RequestAttribute(name = "userId") UUID userId)
    {
        taskDto.setId(id);
        taskDto.setUserId(userId);
        try {
            var task = taskService.updateTask(taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity createTask(@RequestAttribute(name = "userId") UUID userId) {
        var userTasks = taskService.getUserTasks(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userTasks);
    }
}
