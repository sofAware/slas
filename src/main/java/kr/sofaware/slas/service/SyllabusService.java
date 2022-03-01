package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SyllabusService {
    private final SyllabusRepository syllabusRepository;

    /**
     * 교수 학번으로 개설한 수업들 '<년도>-<학기>' 형식으로 분류 후 HashMap 으로 반환해줌
     * @author 김수헌
     * @param professorId
     * @return {21-2=[{기업과경영}, {분석화학}, {생화학}, {열전달}, {유기화학}], 21-1=[{경영전략론}, {마케팅원론}]}
     */
    public Map<String, List<Syllabus>> mapAllByProfessorId(String professorId) {
        Map<String, List<Syllabus>> map = new TreeMap<>(Collections.reverseOrder());

        syllabusRepository.findAllByProfessor_Id(professorId).forEach(syllabus -> {
            String yearSemester = syllabus.getId().substring(0, 4);
            
            if(!map.containsKey(yearSemester)) //만약 해당 학기에 대한 키가 없다면
                map.put(yearSemester, new ArrayList<>());

            map.get(yearSemester).add(syllabus);
        });

        // 강의명으로 정렬해서 반환
        map.forEach((s, syllabi) -> syllabi.sort(Comparator.comparing(Syllabus::getName)));
        return map;
    }
}
