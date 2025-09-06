package com.portal.user;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.portal.security.TokenService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOG = Logger.getLogger(UserResource.class);

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @Inject
    io.smallrye.jwt.auth.principal.JWTParser jwtParser;

    // Injected from application.properties
    @ConfigProperty(name = "app.frontend.base-url")
    String frontendBaseUrl;

    public static class SignupReq {

        public String email;
        public String password;
    }

    public static class LoginReq {

        public String email;
        public String password;
    }

    @POST
    @Path("/signup")
    public Response signup(SignupReq req) {
        if (req == null || req.email == null || req.password == null) {
            LOG.warn("Signup failed: missing fields");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Email and password are required"))
                    .build();
        }

        try {
            userService.register(req.email, req.password);
            LOG.infof("Signup success for email: %s", req.email);
            return Response.ok(Map.of("message", "Signup OK. Check your email to verify account")).build();
        } catch (IllegalArgumentException dup) {
            LOG.warnf("Signup failed: email %s already exists", req.email);
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("message", "Email already registered"))
                    .build();
        } catch (Exception e) {
            LOG.error("Error during signup", e);
            return Response.serverError().entity(Map.of("message", "Internal error during signup")).build();
        }
    }

    @GET
    @Path("/verify/{token}")
    public Response verify(@PathParam("token") String token) {
        boolean ok = userService.verifyToken(token);

        String redirectUrl = frontendBaseUrl + "/login";

        if (!ok) {
            LOG.warnf("Email verification failed: invalid token %s", token);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "Invalid or expired token"))
                    .build();
        }

        LOG.info("Email verification successful, redirecting to login page");
        return Response.seeOther(java.net.URI.create(redirectUrl)).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginReq req, @Context HttpHeaders headers) {
        if (req == null || req.email == null || req.password == null) {
            LOG.warn("Login failed: missing fields");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Email and password are required"))
                    .build();
        }

        User u = userService.authenticate(req.email, req.password);
        if (u == null) {
            LOG.warnf("Login failed: invalid credentials for %s", req.email);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Invalid email or password"))
                    .build();
        }

        if (!u.emailVerified) {
            LOG.warnf("Login blocked: email not verified for %s", u.email);
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("message", "Check your email and verify your account before login"))
                    .build();
        }

        String jwt = tokenService.issueToken(u.email, true);

        LOG.infof("Login success for %s (verified)", u.email);

        NewCookie cookie = new NewCookie.Builder("session")
                .value(jwt)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite(NewCookie.SameSite.NONE)
                .maxAge(60 * 60)
                .build();

        return Response.ok(Map.of(
                "isValidated", true,
                "message", "Login successful. Welcome to the portal"
        ))
                .cookie(cookie)
                .build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        LOG.info("User logged out");
        NewCookie cookie = new NewCookie("session", "", "/", null, null, 0, false, true);
        return Response.ok(Map.of("message", "Logged out")).cookie(cookie).build();
    }

    @GET
    @Path("/me")
    public Response me(@CookieParam("session") String token) {
        if (token == null || token.isBlank()) {
            LOG.debug("Access to /me without token");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Not authenticated"))
                    .build();
        }
        try {
            var jwt = jwtParser.parse(token);
            String email = jwt.getClaim("upn").toString();

            Object verifiedClaim = jwt.getClaim("verified");
            boolean verified = false;
            if (verifiedClaim instanceof Boolean b) {
                verified = b;
            } else if (verifiedClaim != null) {
                verified = Boolean.parseBoolean(verifiedClaim.toString());
            }

            LOG.infof("Fetched profile for %s (verified=%s)", email, verified);
            return Response.ok(Map.of("email", email, "isValidated", verified)).build();
        } catch (Exception ex) {
            LOG.warn("Invalid or expired token at /me", ex);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Invalid session"))
                    .build();
        }
    }

    

}
