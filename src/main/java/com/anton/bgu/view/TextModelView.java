package com.anton.bgu.view;

import java.util.List;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.RequestsModel;

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
        render("%s - %d / %d", faculty.getName(), faculty.getPlanTotal(), faculty.getRequestTotal());

//        faculty.validate();

        faculty.getSpecialities().forEach(speciality -> {
            render("    План [%3d/%3d], Заявок [%3d/%3d], Баллы %s / %s - %s",
                speciality.getPlanFree(), speciality.getPlanPay(),
                speciality.getRequestFreeTotal(), speciality.getRequestPayTotal(),
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
