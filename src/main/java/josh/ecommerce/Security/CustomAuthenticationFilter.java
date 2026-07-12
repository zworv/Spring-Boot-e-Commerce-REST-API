package josh.ecommerce.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    @NullMarked
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String role = request.getParameter("role");
        request.getSession().setAttribute("role", role);
        return super.attemptAuthentication(request, response);
    }

}
