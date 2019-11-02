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
package com.livejournal.xtecuan.microprofile.dto;

import java.io.Serializable;

/**
 *
 * @author xtecuan
 */

public class UsersDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long userid;
    
    private String email;
    
    private String firstname;
    
    private String middlename;
    
    private String lastname;
   
    private String password;
    
    private String wrole;

    public UsersDTO() {
    }

    public UsersDTO(Long userid) {
        this.userid = userid;
    }

    public UsersDTO(Long userid, String email, String firstname, String lastname, String password) {
        this.userid = userid;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWrole() {
        return wrole;
    }

    public void setWrole(String wrole) {
        this.wrole = wrole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersDTO)) {
            return false;
        }
        UsersDTO other = (UsersDTO) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsersDTO[email=" + email + "]";
    }

}
