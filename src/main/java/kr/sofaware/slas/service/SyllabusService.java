package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SyllabusService {
    private final SyllabusRepository syllabusRepository;

    /**
     * 교수가 강의하는(했던) 수업들 '<년도>-<학기>' 형식으로 분류 후 HashMap 으로 반환해줌
     * @author 양경호
     * @param professorId 교번
     * @return {21-2=[{기업과경영}], 21-1=[{마케팅원론}]}
     */
    public Map<String, List<Syllabus>> mapAllByProfessorId(String professorId) {
        Map<String, List<Syllabus>> map = new TreeMap<>(Collections.reverseOrder());

        syllabusRepository.findAllByProfessor_Id(professorId).forEach(syllabus -> {
            // <년도>-<학기> 문자열 추출
            String yearSemester = syllabus.getId().substring(0, 4);

            // 최초 대입 시 키 생성
            if (!map.containsKey(yearSemester))
                map.put(yearSemester, new ArrayList<>());

            // 값 대입
            map.get(yearSemester).add(syllabus);
        });

        // 강의명으로 정렬해서 반환
        map.forEach((s, syllabi) -> syllabi.sort(Comparator.comparing(Syllabus::getName)));
        return map;
    }

    /**
     * 교수가 해당 학정번호의 강의를 하는지 여부
     * @author 양경호
     * @param syllabusId 학정번호
     * @param professorId 교번
     * @return 내용의 참 거짓 값
     */
    public boolean existsByIdAndProfessor_Id(String syllabusId, String professorId) {
        return syllabusRepository.existsByIdAndProfessor_Id(syllabusId, professorId);
    }

    /**
     * 교수가 강의 한 것 들중 가장 최근에 강의 한 것 하나 반환
     * @author 양경호
     * @param professorId 교번
     * @return Syllabus
     */
    public Optional<Syllabus> findFirstByProfessor_IdOrderByIdDesc(String professorId) {
        return syllabusRepository.findFirstByProfessor_IdOrderByIdDesc(professorId);
    }

    /**
     * 학정번호로 강의 가져오기
     * @author 양경호
     * @param syllabusId 학정번호
     * @return
     */
    public Optional<Syllabus> findById(String syllabusId) {
        return syllabusRepository.findById(syllabusId);
    }
}
