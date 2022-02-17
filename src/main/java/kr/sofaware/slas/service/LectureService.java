package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;

    /**
     * 학생 학번으로 들었던 수업들 '<년도>-<학기>' 형식으로 분류 후 HashMap 으로 반환해줌
     * @author 양경호
     * @param studentId
     * @return {21-2=[{기업과경영}, {분석화학}, {생화학}, {열전달}, {유기화학}], 21-1=[{경영전략론}, {마케팅원론}]}
     */
    public Map<String, List<Syllabus>> mapAllByStudentId(String studentId) {
        Map<String, List<Syllabus>> map = new TreeMap<>(Collections.reverseOrder());

        lectureRepository.findAllByStudent_Id(studentId).forEach(lecture -> {
            String yearSemester = lecture.getSyllabus().getId().substring(0, 4);

            if (!map.containsKey(yearSemester))
                map.put(yearSemester, new ArrayList<>());

            map.get(yearSemester).add(lecture.getSyllabus());
        });

        // 강의명으로 정렬해서 반환
        map.forEach((s, syllabi) -> syllabi.sort(Comparator.comparing(Syllabus::getName)));
        return map;
    }
}
