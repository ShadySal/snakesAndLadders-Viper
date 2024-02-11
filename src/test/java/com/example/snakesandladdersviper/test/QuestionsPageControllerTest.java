//import com.example.snakesandladdersviper.Controller.QuestionsPageController;
//import com.example.snakesandladdersviper.Model.Question;
//import com.example.snakesandladdersviper.Model.SysData;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import java.util.HashMap;
//
//import static javafx.beans.binding.Bindings.when;
//import static javax.management.Query.times;
//import static org.mockito.Mockito.*;
//
//class QuestionsPageControllerTest {
//
//    @InjectMocks
//    private QuestionsPageController questionsPageController;
//
//    @Mock
//    private SysData sysData;
//
//    @Mock
//    private ListView<String> questionsView;
//
//    @Mock
//    private ListView<String> answersView;
//
//    @Mock
//    private TextField questionDifficulty;
//
//    @Mock
//    private TextField correctAnswer;
//
//    @BeforeAll
//    public static void initJFX() {
//        new JFXPanel(); // Initialize JavaFX
//    }
//
//    @BeforeAll
//    public static void setUpHeadlessMode() {
//        System.setProperty("java.awt.headless", "false");
//    }
//
//    @Test
//    void testLoadQuestions() {
//        // Arrange
//        ObservableList<Question> mockQuestions = FXCollections.observableArrayList(
//                new Question("Question1", new HashMap<>(), "Answer1", 1),
//                new Question("Question2", new HashMap<>(), "Answer2", 2)
//        );
//        when(SysData.getQuestions()).thenReturn(mockQuestions);
//
//        // Act
//        questionsPageController.initialize();
//
//        // Assert
//        verify(questionsView, times(2)).getItems().add(anyString());
//    }
//
//    @Test
//    void testDisplayAnswers() {
//        // Arrange
//        ObservableList<Question> mockQuestions = FXCollections.observableArrayList(
//                new Question("Question1", new HashMap<>(), "Answer1", 1),
//                new Question("Question2", new HashMap<>(), "Answer2", 2)
//        );
//        when(sysData.getQuestions()).thenReturn(mockQuestions);
//
//        when(questionsView.getSelectionModel().selectedItemProperty()).thenReturn(null);
//
//        // Act
//        questionsPageController.initialize();
//        questionsPageController.displayAnswers("Question1");
//
//        // Assert
//        verify(answersView).getItems().clear();
//        verify(correctAnswer).setText("Answer1");
//        verify(questionDifficulty).setText("1");
//    }
//
//    @Test
//    void testOnDeleteButtonClicked() {
//        // Arrange
//        when(questionsView.getSelectionModel().getSelectedItem()).thenReturn("Question1");
//
//        // Act
//        questionsPageController.onDeleteButtonClicked(null);
//
//        // Assert
//        verify(SysData.removeQuestion("Question1"));
//        verify(questionsView).getItems();
//    }
//
//    // Add more tests for other functionalities (onEditButtonClicked, onAddButtonClicked) following a similar structure
//}
