package org.vaadin.example.ui.examples;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
// import com.vaadin.flow.theme.material.Material;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.GreetService;

@Route("changetheme")
// @NoTheme
// @Theme(value = Material.class, variant = Material.LIGHT)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class ChangeThemeVariant extends VerticalLayout {
    /**
     * Редактор теми Lumo: https://demo.vaadin.com/lumo-editor/
     */
    private static final long serialVersionUID = 5089882795317909399L;

    /**
     * https://github.com/vaadin/flow/issues/5163 public class CustomUI extends UI {
     * 
     * @Override public Optional<ThemeDefinition> getThemeFor(Class<?>
     *           navigationTarget, String path) { User user =
     *           getSession().getAttribute(User.class); return Optional.of(new
     *           ThemeDefinition(user.getThemeClass(), user.getThemeVariant())); } }
     */

    public ChangeThemeVariant(@Autowired GreetService service) {

        final Button changeThemeBtn = new Button("Переключити тему", click -> {
            /*
             * ThemeList themeList = UI.getCurrent().getElement().getThemeList(); if
             * (themeList.contains(Lumo.DARK) || themeList.contains(Lumo.LIGHT)) { //
             * themeList.rem themeList.clear(); themeList.add(Material.LIGHT); } else {
             * themeList.clear(); themeList.add(Lumo.LIGHT); }
             */
            UI.getCurrent().getPage().reload();
        });

        ComboBox<String> changeThemeVariant = new ComboBox<>();
        changeThemeVariant.setItems("Світла", "Темна");
        changeThemeVariant.setValue("Світла");
        changeThemeVariant.setPlaceholder("Вибрати варіант");
        changeThemeVariant.addValueChangeListener(listener -> {
            if (listener.getValue() != null) {
                if (listener.getValue() == "Світла") {
                    service.variant(null);
                    UI.getCurrent().getElement().setAttribute("theme", "light");
                } else {
                    service.variant("dark");
                    UI.getCurrent().getElement().setAttribute("theme", "dark");
                }
            }
        });

        final DatePicker datePicker = new DatePicker("Вибір дати");
        // datePicker.getElement().getStyle().set("--lumo-space-m", "0px");
        datePicker.setLocale(new Locale("uk"));
        datePicker.setI18n(new DatePicker.DatePickerI18n().setWeek("Тиждень").setCalendar("Календар")
                .setClear("Очистить").setToday("Сьогодні").setCancel("Відміна").setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList("Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень",
                        "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"))
                .setWeekdays(Arrays.asList("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота"))
                .setWeekdaysShort(Arrays.asList("нд", "пн", "вт", "ср", "чт", "пт", "сб")));

        datePicker.setWidth("calc(30% - .25rem)");
        datePicker.setClearButtonVisible(true);

        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                int weekday = selectedDate.getDayOfWeek().getValue() % 7;
                String weekdayName = datePicker.getI18n().getWeekdays().get(weekday);

                int month = selectedDate.getMonthValue() - 1;
                String monthName = datePicker.getI18n().getMonthNames().get(month);

                Notification.show("День тижня: " + weekdayName + ", Місяць: " + monthName);
            } else {
                Notification.show("Дата не вибрана!");
            }
        });

        final HorizontalLayout horizontalLayout = new HorizontalLayout(changeThemeBtn, changeThemeVariant, datePicker);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        add(horizontalLayout);
    }

}
