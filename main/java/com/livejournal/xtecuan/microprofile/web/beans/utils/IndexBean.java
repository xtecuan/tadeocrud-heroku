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

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author xtecuan
 */
@Named
@ViewScoped
public class IndexBean extends XBaseBean implements Serializable {

    @PostConstruct
    @Override
    public void init() {

    }
    
     public void console() {
         redirectToUrl("/console/");
     }
     

}
