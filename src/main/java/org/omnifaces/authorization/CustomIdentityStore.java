package org.omnifaces.authorization;

import static javax.security.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

import javax.enterprise.context.RequestScoped;
import javax.security.CallerPrincipal;
import javax.security.identitystore.CredentialValidationResult;
import javax.security.identitystore.IdentityStore;
import javax.security.identitystore.credential.Credential;
import javax.security.identitystore.credential.UsernamePasswordCredential;

@RequestScoped
public class CustomIdentityStore implements IdentityStore {
    
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            return validate((UsernamePasswordCredential) credential);
        }

        return NOT_VALIDATED_RESULT;
    }
    
    public CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {
        
        if (usernamePasswordCredential.getCaller().equals("test") && 
            usernamePasswordCredential.getPassword().compareTo("secret1")) {
            
            return new CredentialValidationResult(
                new CallerPrincipal("test"), 
                null // no static groups, dynamically handled via authorization mechanism 
            );
        }
        
        return INVALID_RESULT;
    }
    
}
