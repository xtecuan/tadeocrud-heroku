/*
 * Copyright (c) 2018 xtecuan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    xtecuan - initial API and implementation and/or initial documentation
 */
package com.livejournal.xtecuan.microprofile.startup.service;

import com.livejournal.xtecuan.microprofile.sql.utils.QueryLoader;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 *
 * @author xtecuan
 */
@Startup
@Singleton
public class DBStartupService {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DBStartupService.class);

    @Resource(lookup = "java:app/jdbc/H2SampleDB")
    private DataSource dataSource;

    @Inject
    private QueryLoader queryLoader;

    private int countIfDataExists() {
        int result = 0;
        try (Connection connection = dataSource.getConnection()) {
            String countQuery = queryLoader.getQueryUsingFilename("USERS_COUNT.sql");
            Statement sta = connection.createStatement();
            ResultSet rset = sta.executeQuery(countQuery);
            while (rset.next()) {
                result = rset.getInt(1);
            }
            rset.close();
            connection.close();
        } catch (Exception ex) {
            logger.error("Error in countIfDataExists: ", ex);
        }

        return result;
    }

    private void createTable() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            String createTable = queryLoader.getQueryUsingFilename("USERS.sql");
            System.out.println(createTable);
            PreparedStatement psta = connection.prepareStatement(createTable);
            int result = psta.executeUpdate();
            logger.info("result: " + result);
            connection.commit();
            psta.close();
            logger.info(connection.getAutoCommit());
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createSampleData() {
        try (Connection connection = dataSource.getConnection()) {
            String createData = queryLoader.getQueryUsingFilename("USERS_DATA.sql");
            PreparedStatement psta = connection.prepareStatement(createData);
            int result = psta.executeUpdate();
            logger.info("result: " + result);
            psta.close();
            connection.close();

        } catch (Exception e) {
            logger.error("Error int createSampleData: ", e);
        }
    }

    public void tryConnection() {
        try (Connection connection = dataSource.getConnection()) {
            out.println("METADATA: "
                    + connection.getMetaData().getDatabaseProductName() + "-"
                    + connection.getCatalog()
            );
        } catch (SQLException e) {

        }
    }

    @PostConstruct
    public void init() {
        try {
            tryConnection();
            createTable();
            if (countIfDataExists() == 0) {
                createSampleData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
