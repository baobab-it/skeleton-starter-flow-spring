package org.vaadin.example;

import java.io.Serializable;

import org.springframework.stereotype.Service;

@Service
public class GreetService implements Serializable {

    private static final long serialVersionUID = 4550108635780881020L;

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return "Hello anonymous user";
        } else {
            return "Hello " + name;
        }
    }

    public String theme(String themeName) {
        if (themeName == null || themeName.isEmpty()) {
            return "Lumo";
        }
        return "Material";
    }

    public String variant(String variant) {
        if (variant == null || variant.isEmpty()) {
            return "light";
        }
        return "dark";
    }

}
