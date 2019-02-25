package codesquad.service;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.exception.AnswerNotFoundException;
import codesquad.exception.PermissionRestrictException;
import codesquad.exception.QuestionNotFoundException;
import codesquad.exception.UserNotLoggedInException;
import codesquad.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public Answer register(Long questionId, Answer answer, HttpSession httpSession) {
        if (!HttpSessionUtils.isLogin(httpSession)) {
            throw new UserNotLoggedInException();
        }
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));

        question.addAnswer(answer);
        answer.setWriter(HttpSessionUtils.getSessionedUser(httpSession));
        return answerRepository.save(answer);
    }

    @Transactional
    public void delete(Long questionId, Long id, HttpSession httpSession) {
        if (!HttpSessionUtils.isLogin(httpSession) || !this.match(id, httpSession)) {
            throw new PermissionRestrictException();
        }
        Answer answer = this.findById(id);
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));

        question.removeAnswer(answer);
        answer.setDeleted(true);
        answerRepository.save(answer);
    }

    public Answer findById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new AnswerNotFoundException(id));
    }

    public boolean match(Long id, HttpSession httpSession) {
        return HttpSessionUtils.getSessionedUser(httpSession).equals(this.findById(id).getWriter());
    }
}
