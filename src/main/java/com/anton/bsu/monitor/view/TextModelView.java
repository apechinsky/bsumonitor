package com.anton.bsu.monitor.view;

import java.util.List;

import com.anton.bsu.monitor.model.Faculty;
import com.anton.bsu.monitor.model.Range;
import com.anton.bsu.monitor.model.RequestsModel;

/**
 * @author Q-APE
 */
public class TextModelView implements ModelView {

    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public String render(RequestsModel model) {
        printFaculties(model.getFaculties());

        return stringBuilder.toString();
    }

    private void printFaculties(List<Faculty> faculties) {
        for (Faculty faculty : faculties) {
            printFaculty(faculty);

            render("================================\n");
        }
    }

    private void printFaculty(Faculty faculty) {
        render("# %s", faculty.getName());
        render("Вне конкурса: " + faculty.getPrivilegedRequests());
        render("Бюджет (план/заявок/проходной) : %3d / %3d / %s",
            faculty.getPlanFree(), faculty.getRequestFree(), faculty.getFreePass());
        render("Платное (план/заявок/проходной): %3d / %3d / %s",
            faculty.getPlanPay(), faculty.getRequestPay(), faculty.getPayPass());
        render("Бюджет. Не проходящие : %d", faculty.getRequestsBeyondPassFree());
        render("Платное. Не проходящие : %d", faculty.getRequestsBeyondPassPay());
        render("Заявок ниже 311 б: %d", faculty.getRequestCountBelow311Free());
        render("Заявок ниже 311 п: %d", faculty.getRequestCountBelow311Pay());

//        render("Free Total: " + faculty.getFreeRequestDistribution());
//        render("Pay  Total: " + faculty.getPayRequestDistribution());

        faculty.getSpecialities().forEach(speciality -> {
            render("    План (бюджет/платное) [%3d/%3d], Заявок (бюджет/платное) [%3d/%3d], Баллы (бюджет/платное) %s / %s - %s",
                speciality.getPlanFree(), speciality.getPlanPay(),
                speciality.getRequestFree(), speciality.getRequestPay(),
                speciality.getFreePass(), speciality.getPayPass(),
                speciality.getName()
            );
        });
    }

    private void render(Object pattern, Object... arguments) {
        String line = String.format(pattern + "%n", arguments);
        stringBuilder.append(line);
    }
}
