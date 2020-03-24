package org.vaadin.example.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.vaadin.example.ui.view.login.LoginView;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    private static final long serialVersionUID = -1415474798033148670L;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    /**
     * Rerout the users if they're not authorized to access to view
     * 
     * @param event before navigation event with event details
     */
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}