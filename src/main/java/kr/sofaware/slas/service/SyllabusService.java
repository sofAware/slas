package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    /**
     * @author 김수헌
     */
    public List<Syllabus> findAllBySyllabusNameOrProfessorName(String syllabusName, String professorName)
    {
        return syllabusRepository.findAllByNameOrProfessor_Name(syllabusName, professorName);
    }

    /**
     * @author 김수헌
     */
    public List<Syllabus> findAllByProfessorId(String professorId)
    {
        return syllabusRepository.findAllByProfessor_Id(professorId);
    }

    /**
     * @author 김수헌
     */
    public Optional<Syllabus> findBySyllabusId(String syllabusId)
    {
        return syllabusRepository.findById(syllabusId);
    }

    /**
     * @author 김수헌
     */
    @Transactional
    public Syllabus saveByPost(Syllabus syllabus){
        return syllabusRepository.save(syllabus);
    }
}
