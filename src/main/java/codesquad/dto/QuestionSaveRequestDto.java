package codesquad.dto;

import codesquad.domain.Question;
import codesquad.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSaveRequestDto {
    private User writer;
    private String title;
    private String contents;

    public Question toEntity() {
        return Question.builder()
                .writer(writer)
                .title(title)
                .contents(contents)
                .build();
    }
}
