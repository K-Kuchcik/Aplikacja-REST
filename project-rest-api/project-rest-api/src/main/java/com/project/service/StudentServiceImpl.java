package com.project.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.model.Student;
import com.project.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> getStudent(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public Optional<Student> getByNrIndeksu(String nrIndeksu) {
        return studentRepository.findByNrIndeksu(nrIndeksu);
    }

    @Override
    public Student setStudent(Student student) {
        logger.info("Zapisywanie studenta: {} {}", student.getImie(), student.getNazwisko());
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Integer studentId) {
        logger.info("Usuwanie studenta o ID={}", studentId);
        studentRepository.deleteById(studentId);
    }

    @Override
    public Page<Student> getStudenci(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Page<Student> searchByNrIndeksu(String nrIndeksu, Pageable pageable) {
        return studentRepository.findByNrIndeksuStartsWith(nrIndeksu, pageable);
    }

    @Override
    public Page<Student> searchByNazwisko(String nazwisko, Pageable pageable) {
        return studentRepository.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

}
