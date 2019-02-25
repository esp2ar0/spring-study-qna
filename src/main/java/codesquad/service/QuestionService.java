package codesquad.service;

import codesquad.domain.*;
import codesquad.dto.QuestionSaveRequestDto;
import codesquad.exception.QuestionNotFoundException;
import codesquad.exception.WriterNotEqualException;
import codesquad.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

    @Transactional
    public Question save(QuestionSaveRequestDto questionSaveRequestDto, HttpSession httpSession) {
        User user = HttpSessionUtils.getSessionedUser(httpSession);

        questionSaveRequestDto.setWriter(user);
        return questionRepository.save(questionSaveRequestDto.toEntity());
    }

    @Transactional
    public void delete(Long id) {
        Question question = this.findById(id);

        if (question.getAnswersCount() != 0) {
            checkWriter(question);
        }

        question.setDeleted(true);
        deleteAnswer(question, id);
        questionRepository.save(question);
    }

    @Transactional
    public void deleteAnswer(Question question, Long id) {
        List<Answer> answers = answerRepository.findByQuestionId(id).orElse(new ArrayList<>());
        for (Answer answer : answers) {
            question.removeAnswer(answer);
            answer.setDeleted(true);
            answerRepository.save(answer);
        }
    }

    @Transactional
    public void update(Long id, QuestionSaveRequestDto modifiedQuestion) {
        Question question = this.findById(id);
        question.update(modifiedQuestion.toEntity());
        questionRepository.save(question);
    }

    public boolean match(Long id, HttpSession httpSession) {
        return HttpSessionUtils.getSessionedUser(httpSession).equals(this.findById(id).getWriter());
    }

    public boolean isSameWriter(Question question, List<Answer> answers) {
        User writer = question.getWriter();
        for (Answer answer : answers) {
            if (!writer.equals(answer.getWriter())) {
                return false;
            }
        }
        return true;
    }

    public void checkWriter(Question question) {
        if (!isSameWriter(question, question.getAnswers())) {
            throw new WriterNotEqualException();
        }
    }
}
