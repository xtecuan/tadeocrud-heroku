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
package com.livejournal.xtecuan.microprofile.facade;

import com.livejournal.xtecuan.microprofile.entities.Users;
import com.livejournal.xtecuan.microprofile.sql.utils.QueryLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 *
 * @author xtecuan
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "tadeocrudPU")
    private EntityManager em;

    @Resource(lookup = "java:app/jdbc/H2SampleDB")
    private DataSource dataSource;

    @Inject
    private QueryLoader queryLoader;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    public String getPasswordHash(String blankPassword) {

        return (String) getEntityManager()
                .createNamedQuery("Users.getPasswordHash")
                .setParameter(1, blankPassword)
                .getSingleResult();
    }

    public String getMyHashFromH2(String plainPassword) {
        String result = null;
        try (Connection connection = dataSource.getConnection()) {
            String hash = queryLoader.getQueryUsingFilename("hash.sql");
            PreparedStatement psta = connection.prepareStatement(hash);
            psta.setString(1, plainPassword);
            ResultSet rset = psta.executeQuery();
            while (rset.next()) {
                result = rset.getString(1);
            }
            rset.close();
            connection.close();
        } catch (Exception ex) {
            getLogger().error("Error in getMyHashFromH2: ", ex);
        }

        return result;
    }

    public Users findByEmailAndPass(String email, String password) {
        Users user = null;

        List<Users> users = getEntityManager()
                .createNamedQuery("Users.findByEmailAndPassword")
                .setParameter("email", email)
                .setParameter("password", getMyHashFromH2(password))
                .getResultList();

        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    public String getCompleteName(Users user) {
        return user.getFirstname()
                + " "
                + (user.getMiddlename() != null && user.getMiddlename().length() > 0 ? user.getMiddlename() : "")
                + " "
                + user.getLastname();

    }

}
