/*
 * Copyright (c) 2019 xtecuan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    xtecuan - initial API and implementation and/or initial documentation
 */
package com.livejournal.xtecuan.microprofile.web.beans.utils;

import com.livejournal.xtecuan.microprofile.dto.UsersDTO;
import com.livejournal.xtecuan.microprofile.facade.UsersFacade;
import static com.livejournal.xtecuan.microprofile.web.beans.utils.XBaseBean.getLogger;
import com.livejournal.xtecuan.microprofile.web.constants.WebConstants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author xtecuan
 */
@Named
@ViewScoped
public class MainBean extends XBaseBean implements Serializable {

    @Inject
    private UsersFacade usersFacade;

    private List<UsersDTO> users;

    private List<String> roles;

    private UsersDTO formUser;

    private UsersDTO selectedUser;

    @Inject
    private UserSessionBean userInSession;

    @PostConstruct
    @Override
    public void init() {
        this.setUsers(usersFacade.findAllUsers());
        getLogger().info("UsersCount: " + getUsers().size());
        this.setRoles(Arrays.asList(WebConstants.ROLES));
        getLogger().info("RolesCount: " + getRoles().size());
        this.setSelectedUser(new UsersDTO());
        this.setFormUser(new UsersDTO());
    }

    public String dbconsole() {
        return "/app/admin/index.jsf?faces-redirect=true";
    }

    public void clearForm() {
        this.formUser.setEmail(null);
        this.formUser.setFirstname(null);
        this.formUser.setLastname(null);
        this.formUser.setMiddlename(null);
        this.formUser.setPassword(null);
        this.formUser.setUserid(null);
        this.formUser.setWrole(null);
    }

    public void save() {
        if (formUser != null) {
            String plainPass = formUser.getPassword();
            formUser.setPassword(usersFacade.getMyHashFromH2(plainPass));
            usersFacade.saveUser(formUser);
            String email = formUser.getEmail();
            setUsers(usersFacade.findAllUsers());
            clearForm();
            addMessage("User created", "With email: " + email);
        }
    }

    public void update() {
        if (formUser != null) {
            getLogger().info("Current Updated Password:      <<" + formUser.getPassword() + ">>");
            getLogger().info("Current Updated Password HASH: " + usersFacade.getMyHashFromH2(formUser.getPassword()));
            getLogger().info("Current Password HASH:         " + getUserInSession().getPasswordChange());
            if (formUser.getPassword() != null && !formUser.getPassword().equals("")) {
                String plainPass = formUser.getPassword();
                formUser.setPassword(usersFacade.getMyHashFromH2(plainPass));
                getUserInSession().setPasswordChange(null);
            } else {
                formUser.setPassword(getUserInSession().getPasswordChange());
                getUserInSession().setPasswordChange(null);
            }
            usersFacade.saveUser(formUser);
            String email = formUser.getEmail();
            setUsers(usersFacade.findAllUsers());
            clearForm();
            addMessage("User Updated", "With email: " + email);
        }
    }

    public void delete() {
        if (selectedUser != null) {
            String email = selectedUser.getEmail();
            usersFacade.deleteUser(selectedUser);
            setUsers(usersFacade.findAllUsers());
            clearForm();
            addMessage("User deleted", "With email: " + email);
        }
    }

    public void onRowSelect(SelectEvent event) {       
        UsersDTO u = (UsersDTO) event.getObject();
        if (u != null) {
            this.setFormUser(u);
            this.getUserInSession().setPasswordChange(u.getPassword());
            getLogger().info("To update: " + getFormUser());
        }
    }

    public void onRowUnselect(UnselectEvent event) {

    }

    public List<UsersDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDTO> users) {
        this.users = users;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UsersDTO getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UsersDTO selectedUser) {
        this.selectedUser = selectedUser;
    }

    public UsersFacade getUsersFacade() {
        return usersFacade;
    }

    public void setUsersFacade(UsersFacade usersFacade) {
        this.usersFacade = usersFacade;
    }

    public UsersDTO getFormUser() {
        return formUser;
    }

    public void setFormUser(UsersDTO formUser) {
        this.formUser = formUser;
    }

    public UserSessionBean getUserInSession() {
        return userInSession;
    }

    public void setUserInSession(UserSessionBean userInSession) {
        this.userInSession = userInSession;
    }

}
