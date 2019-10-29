package de.rieckpil.blog;

import com.livejournal.xtecuan.microprofile.facade.UsersFacade;
import com.livejournal.xtecuan.microprofile.web.beans.utils.UserSessionBean;
import com.livejournal.xtecuan.microprofile.web.beans.utils.XBaseBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class LoginBacking extends XBaseBean {

    @NotEmpty
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotEmpty
    @Email(message = "Please provide a valid e-mail")
    private String email;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    @Inject
    private UsersFacade usersFacade;

    @Inject
    private UserSessionBean currentUser;

    public void submit() throws IOException {

        switch (continueAuthentication()) {
            case SEND_CONTINUE:
                currentUser.setUser(usersFacade.findByEmailAndPass(email, password));
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
                break;
            case SUCCESS:
                currentUser.setUser(usersFacade.findByEmailAndPass(email, password));
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Login succeed", null));
                externalContext.redirect(externalContext.getRequestContextPath() + "/app/index.jsf");
                break;
            case NOT_DONE:
        }
    }

    private AuthenticationStatus continueAuthentication() {
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(email, password))
        );
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String dbconsole() {
        return "/app/admin/index.jsf?faces-redirect=true";
    }

    @PostConstruct
    @Override
    public void init() {

    }

    public UserSessionBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserSessionBean currentUser) {
        this.currentUser = currentUser;
    }
    
    

}
