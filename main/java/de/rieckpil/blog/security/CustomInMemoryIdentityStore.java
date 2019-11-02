package de.rieckpil.blog.security;

import com.livejournal.xtecuan.microprofile.entities.Users;
import com.livejournal.xtecuan.microprofile.facade.UsersFacade;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;
import javax.inject.Inject;
import org.apache.log4j.Logger;

@ApplicationScoped
public class CustomInMemoryIdentityStore implements IdentityStore {

    private static final Logger LOGGER = Logger.getLogger(CustomInMemoryIdentityStore.class);

    @Inject
    private UsersFacade usersFacade;

    @Override
    public CredentialValidationResult validate(Credential credential) {

        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;
        String email = login.getCaller();
        String password = login.getPasswordAsString();

        getLogger().info("Caller: " + email);
        getLogger().info("blankPassword: " + password);
        getLogger().info("hashedPassword: " + usersFacade.getMyHashFromH2(password));

        Users user = usersFacade.findByEmailAndPass(email, password);

        if (user != null) {
            return new CredentialValidationResult(user.getEmail(), new HashSet<>(Arrays.asList(user.getWrole())));
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

//        if (login.getCaller().equals("admin@mail.com") && login.getPasswordAsString().equals("ADMIN1234")) {
//            return new CredentialValidationResult("admin", new HashSet<>(Arrays.asList("ADMIN")));
//        } else if (login.getCaller().equals("user@mail.com") && login.getPasswordAsString().equals("USER1234")) {
//            return new CredentialValidationResult("user", new HashSet<>(Arrays.asList("USER")));
//        } else {
//
//        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
