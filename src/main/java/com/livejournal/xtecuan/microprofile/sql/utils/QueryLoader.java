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
package com.livejournal.xtecuan.microprofile.sql.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.ejb.Singleton;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author xtecuan
 */
@Singleton
public class QueryLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(QueryLoader.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_PACKAGE_FROM = "./queries/";
    private static final String DEFAULT_PACKAGE_HTML = "./html/";

    public String getQueryUsingFilename(String filename) {
        String result = null;
        try {
            result = IOUtils.resourceToString(
                    DEFAULT_PACKAGE_FROM + filename,
                    Charset.forName(DEFAULT_CHARSET),
                    Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            logger.error("Error loading query from file: " + DEFAULT_PACKAGE_FROM + filename, e);
        }
        return result;
    }

    public List<String> getSQLSentencesUsingFilename(String filename) {
        List<String> sql = new ArrayList<>();
        String sqlFull = getQueryUsingFilename(filename);
        int i = 0;
        Scanner s = new Scanner(sqlFull);
        s.useDelimiter(";\n");
        while (s.hasNext()) {
            String c = s.next();
            sql.add(c);
        }
        return sql;
    }

    public String getQueryUsingFilename(String packageName, String filename) {
        String result = null;
        try {
            result = IOUtils.resourceToString(
                    packageName + filename,
                    Charset.forName(DEFAULT_CHARSET),
                    Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            logger.error("Error loading query from file: " + packageName + filename, e);
        }
        return result;
    }

    public String getHtmlTemplate(String filename) {
        return getQueryUsingFilename(DEFAULT_PACKAGE_HTML, filename);
    }

    public String getFilledHtmlTemplate(Map<String, String> data, String filename) {
        String template = getHtmlTemplate(filename);

        for (String key : data.keySet()) {
            template = template.replaceAll("$" + key, data.get(key));
        }

        return template;
    }

}
