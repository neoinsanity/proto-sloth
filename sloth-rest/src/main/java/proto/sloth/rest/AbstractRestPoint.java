package proto.sloth.rest;

import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRestPoint {

    private SessionWorldContainer sessionWorldContainer;

    protected SessionWorldContainer getSessionWorldContainer() {

        return sessionWorldContainer;
    }

    @Required
    public void setSessionWorldContainer(SessionWorldContainer sessionWorldContainer) {

        this.sessionWorldContainer = sessionWorldContainer;
    }
}
