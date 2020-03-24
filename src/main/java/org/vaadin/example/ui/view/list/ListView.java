package org.vaadin.example.ui.view.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.vaadin.example.backend.entity.Company;
import org.vaadin.example.backend.entity.Contact;
import org.vaadin.example.backend.service.CompanyService;
import org.vaadin.example.backend.service.ContactService;
import org.vaadin.example.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
@CssImport("./styles/shared-styles.css")
public class ListView extends VerticalLayout {

    private static final long serialVersionUID = -5948005587412478945L;

    private final ContactForm form;
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    private final ContactService contactService;

    public ListView(final ContactService contactService, CompanyService companyService) {

        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new ContactForm(companyService.findAll());
        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent evt) {
        contactService.delete(evt.getContact());
        updateList();
        closeEditor();

    }

    private void saveContact(ContactForm.SaveEvent evt) {
        contactService.save(evt.getContact());
        updateList();
        closeEditor();
    }

    private void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassNames("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact", click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status");
        grid.addColumn(contact -> {
            final Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
    }

    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }

}