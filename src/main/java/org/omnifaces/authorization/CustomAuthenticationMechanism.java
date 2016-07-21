package org.omnifaces.authorization;

import static javax.security.identitystore.CredentialValidationResult.Status.VALID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.authentication.mechanism.http.HttpMessageContext;
import javax.security.identitystore.CredentialValidationResult;
import javax.security.identitystore.IdentityStore;
import javax.security.identitystore.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestScoped
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {
    
    @Inject
    private IdentityStore identityStore;

    @Override
    public AuthStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthException {

        if (request.getParameter("name") != null && request.getParameter("password") != null) {

            CredentialValidationResult result = identityStore.validate(
                new UsernamePasswordCredential(request.getParameter("name"), request.getParameter("password")));

            if (result.getStatus() == VALID) {
                return httpMessageContext.notifyContainerAboutLogin(
                    result.getCallerPrincipal(), result.getCallerGroups());
            } else {
                throw new AuthException("Login failed");
            }
        } 

        return httpMessageContext.doNothing();
    }
    
}
