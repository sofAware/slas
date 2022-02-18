package kr.sofaware.slas.service;

import kr.sofaware.slas.auth.MemberDto;
import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    public Member loadUserByUsername(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
    }

    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
    }

    @Transactional
    public String save(MemberDto memberDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberDTO.setPassword(encoder.encode(memberDTO.getPassword())); //패스워드 인코딩

        return userRepository.save(Member.from(memberDTO)).getId();
    }
}
