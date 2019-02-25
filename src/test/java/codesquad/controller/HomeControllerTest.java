package codesquad.controller;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QuestionRepository questionRepository;

    @Test
    public void indexTest() throws Exception {
        Question[] questions = {
                new Question(null, "제목", "내용"),
                new Question(null, "제목2", "내용2")
        };

        when(questionRepository.findAll()).thenReturn(Arrays.asList(questions));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("questions"))
                .andExpect(model().attribute("questions", IsCollectionWithSize.hasSize(2)))
                .andDo(print());

        assertThat(questionRepository.findAll().get(0).getTitle()).isEqualTo("제목");
    }

}