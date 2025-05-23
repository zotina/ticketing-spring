package mg.itu.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import mg.itu.exception.JwtAuthenticationException;
import mg.itu.service.UserDetailsServiceImpl;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/"); 
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);

            if (token == null) {
                logger.debug("No JWT token found");
                filterChain.doFilter(request, response);
                return;
            }

            String username = tokenProvider.getUsernameFromJWT(token);

            if (StringUtils.hasText(username)) {
                logger.debug("Processing authentication for user: {}", username);

                if (tokenProvider.validateToken(token)) {
                    authenticateUser(request, username);
                    logger.debug("Authentication successful for user: {}", username);
                } else {
                    String refreshToken = tokenProvider.getStoredRefreshToken(username);
                    if (refreshToken != null && tokenProvider.validateRefreshToken(refreshToken, username)) {
                        logger.debug("JWT expired, attempting to refresh for user: {}", username);
                        String newToken = tokenProvider.refreshToken(refreshToken, username);
                        request.getSession().setAttribute("jwtToken", newToken);
                        authenticateUser(request, username);
                        logger.debug("Token refreshed and authentication successful for user: {}", username);
                    } else {
                        logger.warn("Invalid or expired JWT token and no valid refresh token for user: {}", username);
                        throw new JwtAuthenticationException("JWT invalide ou expiré. Veuillez vous reconnecter.");
                    }
                }
            } else {
                logger.debug("Could not extract username from JWT token");
            }

            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException ex) {
            logger.error("JWT Authentication failed: {}", ex.getMessage());
            sendAuthenticationError(response, ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error during JWT authentication", ex);
            sendAuthenticationError(response, "Une erreur s'est produite lors de l'authentification");
        }
    }

    private void authenticateUser(HttpServletRequest request, String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendAuthenticationError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(String.format("{\"error\": \"%s\"}", errorMessage));
        response.getWriter().flush();
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return (String) request.getSession().getAttribute("jwtToken");
    }
}