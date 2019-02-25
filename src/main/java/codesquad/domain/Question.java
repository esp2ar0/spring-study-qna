package codesquad.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_to_writer"))
    private User writer;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    private LocalDateTime createDate = LocalDateTime.now();

    private boolean deleted = false;

    @Builder
    public Question(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public String getFormattedCreateDate() {
        if (createDate == null) {
            return "";
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public int getAnswersCount() {
        return answers.size();
    }

    public void update(Question modifiedQuestion) {
        this.title = modifiedQuestion.title;
        this.contents = modifiedQuestion.contents;
    }

    public void addAnswer(Answer answer) {
        this.getAnswers().add(answer);
        answer.setQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        this.getAnswers().remove(answer);
        answer.setQuestion(null);
    }

}
