package lucas0204.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskDto, UUID> {
    List<TaskDto> findAllByUserId(UUID userId);
}
