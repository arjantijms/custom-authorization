package org.omnifaces.authorization;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.omnifaces.jaccprovider.jacc.AuthorizationRules.getRequiredRoles;

import java.security.Permission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.omnifaces.jaccprovider.cdi.AuthorizationMechanism;
import org.omnifaces.jaccprovider.jacc.Caller;
import org.omnifaces.jaccprovider.jacc.SecurityConstraints;

@ApplicationScoped
public class CustomAuthorizationMechanism implements AuthorizationMechanism {
    
    private Map<String, Set<String>> callerNamesToRoles = new HashMap<>();
    
    public CustomAuthorizationMechanism() {
        // Mock role lookup via a Map, in reality may check back-end system
        callerNamesToRoles.put("test", new HashSet<>(asList("foo", "bar", "kaz")));
        callerNamesToRoles.put("user1", new HashSet<>(asList("foo", "zak", "oof")));
    }
    
    @Override
    public Boolean preAuthenticatePreAuthorize(Permission requestedPermission, SecurityConstraints securityConstraints) {
        out.println("preAuthenticatePreAuthorize called. Requested permission: " + requestedPermission);
        
        return null;
    }

    @Override
    public Boolean postAuthenticatePreAuthorizeByRole(Permission requestedPermission, Caller caller, SecurityConstraints securityConstraints) {
        
        out.println("postAuthenticatePreAuthorizeByRole called. Requested permission: " + requestedPermission);
        
        return getRequiredRoles(securityConstraints.getPerRolePermissions(), requestedPermission)
                .stream()
                .anyMatch(role -> isInRole(caller.getCallerPrincipal().getName(), role));
    }
    
    boolean isInRole(String callerName, String role) {
        return callerNamesToRoles
                    .getOrDefault(callerName, emptySet())
                    .contains(role);
    }

}
