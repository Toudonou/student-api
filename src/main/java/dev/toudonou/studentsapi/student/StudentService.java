package dev.toudonou.studentsapi.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.StubNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getStudents() {
        return this.studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("Email should be unique");
        }
        studentRepository.save(student);
    }

    @Transactional
    public void updateStudent(Integer studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student " + studentId + " doesn't exists"));

        if (name != null && !name.isEmpty() && !Objects.equals(name, student.getName())) {
            student.setName(name);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(email, student.getEmail())) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("This email has been taken");
            }
            student.setEmail(email);
        }

    }

    public void deleteStudent(Integer studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student " + studentId + " doesn't exist");
        }
        studentRepository.deleteById(studentId);
    }
}
