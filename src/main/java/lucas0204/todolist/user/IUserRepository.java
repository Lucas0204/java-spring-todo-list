package lucas0204.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserDto, UUID> {
    public UserDto findByUsername(String username);
}
