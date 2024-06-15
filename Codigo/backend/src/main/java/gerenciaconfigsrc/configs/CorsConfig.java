package gerenciaconfigsrc.configs;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsConfig extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String reqOrigin = request.getHeader(HttpHeaders.ORIGIN);
        String reqMethods = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        String reqHeaders = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);

        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, reqOrigin);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, reqMethods);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, reqHeaders);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        filterChain.doFilter(request, response);
    }
}

