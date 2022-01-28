package kr.sofaware.slas.service;

import kr.sofaware.slas.dto.StudentDto;
import kr.sofaware.slas.entity.Student;
import kr.sofaware.slas.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class StudentService implements UserDetailsService {

    @Autowired
    private final StudentRepository studentRepository;

    @Override
    public Student loadUserByUsername(String id) throws UsernameNotFoundException {
        return studentRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
    }

    @Transactional
    public String save(StudentDto studentDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        studentDto.setPassword(encoder.encode(studentDto.getPassword())); //패스워드 인코딩

        return studentRepository.save(Student.builder()
                .id(studentDto.getId())
                .password(studentDto.getPassword())
                .name(studentDto.getName())
                .major(studentDto.getMajor())
                .semester(studentDto.getSemester())
                .auth(studentDto.getAuth()).build()
        ).getId();
    }
}
