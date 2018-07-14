package com.anton.bgu.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Document;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.Range;
import com.anton.bgu.model.Requests;
import com.anton.bgu.model.Speciality;

/**
 * @author Q-APE
 */
public class RequestListPageParser {

    public Requests parse(Document free, Document pay) {
        List<Faculty> freeFaculties = new FreeRequestListPageParser().parse(free);
        List<Faculty> payFaculties = new PayRequestListPageParser().parse(pay);

        return new Requests(merge(freeFaculties, payFaculties));
    }

    private List<Faculty> merge(List<Faculty> freeFaculties, List<Faculty> payFaculties) {
        ArrayList<Faculty> result = new ArrayList<>();

        for (Faculty freeFaculty : freeFaculties) {
            Optional<Faculty> payFaculty =
                payFaculties.stream().filter(f -> f.isSame(freeFaculty))
                    .findFirst();

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
