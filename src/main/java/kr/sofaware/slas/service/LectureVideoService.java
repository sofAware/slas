package kr.sofaware.slas.service;

import kr.sofaware.slas.repository.LectureVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureVideoService {

    private final LectureVideoRepository lectureVideoRepository;
}
