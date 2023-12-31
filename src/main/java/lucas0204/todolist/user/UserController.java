package lucas0204.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @PostMapping()
    public ResponseEntity create(@RequestBody UserDto userDto) {
        var user = userRepository.findByUsername(userDto.getUsername());

        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data to create new user!");
        }

        var passwordHash = BCrypt.withDefaults().hashToString(12, userDto.getPassword().toCharArray());
        userDto.setPassword(passwordHash);

        var userCreated = userRepository.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
