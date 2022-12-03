package com.tsemkalo.homework7;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NotNullNullableValidation")
public final class SecurityHandlerBuilder {
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_GUEST = "GUEST";

    private final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

    public ConstraintSecurityHandler build(LoginService loginService) {
        securityHandler.setLoginService(loginService);

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        constraintMappings.addAll(constraintFullMapping(
                buildConstraint(ROLE_MANAGER),
                Collections.singletonList("/products")
        ));

        constraintMappings.addAll(constraintGetMapping(
                buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                Collections.singletonList("/products")
        ));

        securityHandler.setConstraintMappings(constraintMappings);
        securityHandler.setAuthenticator(new BasicAuthenticator());
        securityHandler.setDenyUncoveredHttpMethods(true);
        return securityHandler;
    }

    private static Constraint buildConstraint(String... userRoles) {
        Constraint starterConstraint = new Constraint();
        starterConstraint.setName(Constraint.__BASIC_AUTH);
        starterConstraint.setRoles(userRoles);
        starterConstraint.setAuthenticate(true);
        return starterConstraint;
    }

    private static Collection<ConstraintMapping> constraintGetMapping(Constraint constraint,
                                                                      Collection<String> paths) {
        return constraintMapping(constraint, paths, "GET");
    }

    private static Collection<ConstraintMapping> constraintFullMapping(Constraint constraint,
                                                                       Collection<String> paths) {
        return constraintMapping(constraint, paths, "*");
    }

    private static Collection<ConstraintMapping> constraintMapping(Constraint constraint,
                                                                   Collection<String> paths,
                                                                   String method) {
        return paths.stream()
                .map(path -> {
                            ConstraintMapping mapping = new ConstraintMapping();
                            mapping.setConstraint(constraint);
                            mapping.setPathSpec(path);
                            mapping.setMethod(method);
                            return mapping;
                        }
                ).collect(Collectors.toList());
    }
}
