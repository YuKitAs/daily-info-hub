package xigua.daily.info.hub.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ExpenditureRecordTest {
    private static final String ID = "507f1f77bcf86cd799439011";
    private static final Instant DATE = LocalDateTime.of(2016, 8, 8, 7, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();
    private static final int AMOUNT_IN_CENT = 200;
    private static final String ITEM = "Ice Cream";

    private JacksonTester<ExpenditureRecord> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void deserialize_WithCorrectJson() throws IOException {
        ExpenditureRecord expenditureRecord = json.readObject(getClass().getResourceAsStream("ExpenditureRecord.json"));

        assertThat(expenditureRecord.getId()).isEqualTo(ID);
        assertCommonFields(expenditureRecord);
    }

    @Test
    public void deserialize_WithCorrectJsonWithoutId() throws IOException {
        ExpenditureRecord expenditureRecord = json.readObject(getClass().getResourceAsStream("ExpenditureRecord_WithoutId.json"));

        assertThat(expenditureRecord.getId()).isNull();
        assertCommonFields(expenditureRecord);
    }

    @Test
    public void serialize_WithCorrectValues() throws IOException {
        ExpenditureRecord expenditureRecord = json.readObject(getClass().getResourceAsStream("ExpenditureRecord.json"));

        assertThat(json.write(expenditureRecord)).isEqualToJson("ExpenditureRecord.json");
    }

    private void assertCommonFields(ExpenditureRecord expenditureRecord) {
        assertThat(expenditureRecord.getDate()).isEqualTo(DATE);
        assertThat(expenditureRecord.getAmountInCent()).isEqualTo(AMOUNT_IN_CENT);
        assertThat(expenditureRecord.getItem()).isEqualTo(ITEM);
    }
}