package com.anton.bgu.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Document;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.RequestsModel;
import com.anton.bgu.model.Speciality;

/**
 * @author Q-APE
 */
public class RequestListPageParser {

    public RequestsModel parse(Document free, Document pay) {
        List<Faculty> freeFaculties = new FreeRequestListPageParser().parse(free);
        List<Faculty> payFaculties = new PayRequestListPageParser().parse(pay);

        List<Faculty> faculties = merge(freeFaculties, payFaculties);

        return new RequestsModel(free, faculties);
    }

    private List<Faculty> merge(List<Faculty> freeFaculties, List<Faculty> payFaculties) {
        ArrayList<Faculty> result = new ArrayList<>();

        for (Faculty freeFaculty : freeFaculties) {
            Optional<Faculty> payFaculty = ParserUtils.findFaculty(payFaculties, freeFaculty);

            if (payFaculty.isPresent()) {
                result.add(merge(freeFaculty, payFaculty.get()));
            }
            else {
                ParserUtils.log("Не найден факультет '%s'", freeFaculty.getName());
            }

        }

        return result;
    }

    private Faculty merge(Faculty freeFaculty, Faculty payFaculty) {

        for (Speciality freeSpeciality : freeFaculty.getSpecialities()) {

            Optional<Speciality> paySpeciality = payFaculty.getSpeciality(freeSpeciality.getName());

            if (paySpeciality.isPresent()) {
                freeSpeciality.setPayRequests(paySpeciality.get().getPayRequests());
            }
            else {
                ParserUtils.log("Не найдена специальность '%s'", freeSpeciality.getName());
            }
        }

        return freeFaculty;
    }

}
