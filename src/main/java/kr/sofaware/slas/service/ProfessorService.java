package kr.sofaware.slas.service;

import kr.sofaware.slas.dto.ProfessorDto;
import kr.sofaware.slas.entity.Professor;
import kr.sofaware.slas.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfessorService implements UserDetailsService {

    @Autowired
    private final ProfessorRepository professorRepository;

    @Override
    public Professor loadUserByUsername(String id) throws UsernameNotFoundException {
        return professorRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
    }

    public String save(ProfessorDto professorDto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        professorDto.setPassword(encoder.encode(professorDto.getPassword())); //패스워드 인코딩

        return professorRepository.save(Professor.builder()
                .id(professorDto.getId())
                .password(professorDto.getPassword())
                .name(professorDto.getName())
                .major(professorDto.getMajor())
                .auth(professorDto.getAuth()).build()
        ).getId();
    }

    /*
        내일 아침에 일어나면
        
        1. templates 수정 + (각 항목 / URL)
        2. 컨트롤러 수정
     */
}