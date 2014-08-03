import org.hamcrest.core.Is;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertThat;
public class SolutionID3Test {

    @Test
    public void shouldReturnCorrectOutput() throws IOException {
        SolutionID3 solution = new SolutionID3();
        String[] testDataFromString = getTestDataFromString("apple\nfruit\ncomputer\nsteve\nvitamin\nipod\niphone\nmacbook\n");
        String[] strings = solution.predictAll(testDataFromString);
        assertThat(strings[0], Is.is("computer-company"));
        assertThat(strings[1], Is.is("fruit"));
        assertThat(strings[2], Is.is("computer-company"));
        assertThat(strings[3], Is.is("computer-company"));
        assertThat(strings[4], Is.is("fruit"));
        assertThat(strings[5], Is.is("computer-company"));
        assertThat(strings[6], Is.is("computer-company"));
        assertThat(strings[7], Is.is("computer-company"));
    }

    @Test
    public void shouldReturnCorrectOutputForThis() throws IOException {
        SolutionID3 solution = new SolutionID3();
        String[] testDataFromString = getTestDataFromString("calcium\ncompany\n");
        String[] strings = solution.predictAll(testDataFromString);
        assertThat(strings[0], Is.is("fruit"));
        assertThat(strings[1], Is.is("computer-company"));
    }

    @Test
    public void shouldReturnCorrectOutputForThisAlso() throws IOException {
        SolutionID3 solution = new SolutionID3();
        String[] testDataFromString = getTestDataFromString(Solution.getDataFromFile("C:\\Users\\User\\IdeaProjects\\ByteTheApple\\NaiveBayes\\src\\input.txt"));
        String[] strings = solution.predictAll(testDataFromString);
        assertThat(strings,Is.is(getTestDataFromString(Solution.getDataFromFile("C:\\Users\\User\\IdeaProjects\\ByteTheApple\\NaiveBayes\\src\\output.txt"))));
    }

    private String[] getTestDataFromString(String string){
        return string.split("[\r\n]+");
    }

}