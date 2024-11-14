package spring.security.practice;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // Override method onAuthenticationSuccess untuk mengatur tindakan setelah login berhasil
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        // Mengecek apakah user yang login memiliki peran/role ADMIN
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));

        // Jika user memiliki role ADMIN
        if (isAdmin) {
            // Mengatur URL tujuan setelah login berhasil ke halaman admin
            setDefaultTargetUrl("/admin/home"); // Admin diarahkan ke halaman admin
        } else {
            // Jika user bukan admin, diarahkan ke halaman user
            setDefaultTargetUrl("/user/home"); // User biasa diarahkan ke halaman user
        }

        // Memanggil method parent untuk melanjutkan proses setelah otentikasi berhasil
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
