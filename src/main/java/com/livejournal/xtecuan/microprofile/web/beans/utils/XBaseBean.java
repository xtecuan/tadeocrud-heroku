package com.livejournal.xtecuan.microprofile.web.beans.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author xtecuan
 */
public abstract class XBaseBean implements Serializable {

    public static final Integer ENG = 1;
    public static final Integer SPA = 2;
    public static final String JSF = ".jsf";
    public static final String FACES_REDIRECT = "?faces-redirect=true&params=true";
    private static final Logger LOGGER = Logger.getLogger(XBaseBean.class);

    public void addMessage(String summary, String detail) {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                summary, detail));
    }

    public void addError(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                summary, detail));
    }

    public void setParam(String name, String value) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put(name, value);
    }

    public String getParam(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public Integer getIntegerParam(String name) {
        return Integer.valueOf(getParam(name));
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public void setSessionAttribute(String name, Object value) {
        getSession().put(name, value);
    }

    public Object getSessionAttribute(String name) {
        return getSession().get(name);
    }

    public Map<String, Object> getSession() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    public void removeSessionAttribute(String name) {
        getSession().remove(name);
    }

    /**
     * Postconstruct init method
     */
    public abstract void init();

    public void invalidateSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public void setFlashParam(String name, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put(name, value);
    }

    public Object getFlashParam(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(name);
    }

    public Integer getFlashIntegerParam(String name) {
        return (Integer) getFlashParam(name);
    }

    public String getFlashStringParam(String name) {
        return (String) getFlashParam(name);
    }

    public void redirectToUrl(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {
            ec.redirect(url);
        } catch (IOException ex) {
            getLogger().error("Error redirecting to " + url, ex);
        }
    }
}
