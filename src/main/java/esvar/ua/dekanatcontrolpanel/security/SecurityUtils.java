package esvar.ua.dekanatcontrolpanel.security;

import java.util.Collection;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.ofNullable(authentication.getName());
    }

    public static String getCurrentUserEmailOrNull() {
        return getCurrentUserEmail().orElse(null);
    }

    public static String getCurrentUserEmailOrThrow() {
        return getCurrentUserEmail().orElseThrow(() ->
                new IllegalStateException("No authenticated user found in security context"));
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) {
            return false;
        }
        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return authorities.stream().anyMatch(authority -> roleName.equals(authority.getAuthority()));
    }
}
