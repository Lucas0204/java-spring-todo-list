package lucas0204.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lucas0204.todolist.user.IUserRepository;
import lucas0204.todolist.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = request.getHeader("Authorization");
        var authBase64 = auth.substring("Basic".length()).trim();
        String decodedAuth = new String(Base64.getDecoder().decode(authBase64));

        String[] credentials = decodedAuth.split(":");
        String username = credentials[0];
        String password = credentials[1];

        UserDto user = userRepository.findByUsername(username);
        if (user == null) {
            response.sendError(400, "Invalid user!");
            return;
        }

        BCrypt.Result validPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
        if (!validPassword.verified) {
            response.sendError(401);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
