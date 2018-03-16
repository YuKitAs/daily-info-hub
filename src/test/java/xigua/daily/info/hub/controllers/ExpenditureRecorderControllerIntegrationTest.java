package xigua.daily.info.hub.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import xigua.daily.info.hub.models.ExpenditureRecord;
import xigua.daily.info.hub.repositories.ExpenditureRecordRepository;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ExpenditureRecorderControllerIntegrationTest {
    private static final Instant DATE1 = LocalDateTime.of(2016, 8, 8, 7, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();
    private static final Instant DATE2 = LocalDateTime.of(2016, 10, 7, 0, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();

    private static final int AMOUNT1 = 200;
    private static final int AMOUNT2 = 600;

    private static final String ITEM1 = "Ice Cream";
    private static final String ITEM2 = "Big Ice Cream";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ExpenditureRecordRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private String recordId;

    @Before
    public void setup() {
        testRestTemplate = new TestRestTemplate(testRestTemplate.getRestTemplate(), "testuser", "nopassword");

        repository.deleteAll();
        ExpenditureRecord record = new ExpenditureRecord(DATE1, AMOUNT1, ITEM1);
        recordId = repository.save(record).getId();
    }

    @Test
    public void getAllExpenditures_WithCorrectData() throws IOException {
        String jsonString = testRestTemplate.getForObject("/expenditure_recorder/expenditures", String.class);
        List<ExpenditureRecord> expenditureRecords = objectMapper.readValue(jsonString, new TypeReference<List<ExpenditureRecord>>() {
        });

        assertThat(expenditureRecords).extracting(ExpenditureRecord::getId).containsExactly(recordId);
        assertThat(expenditureRecords).extracting(ExpenditureRecord::getDate).containsExactly(DATE1);
        assertThat(expenditureRecords).extracting(ExpenditureRecord::getAmountInCent).containsExactly(AMOUNT1);
        assertThat(expenditureRecords).extracting(ExpenditureRecord::getItem).containsExactly(ITEM1);
    }

    @Test
    public void postExpenditure_WithCorrectData() {
        ExpenditureRecord expenditureRecord = new ExpenditureRecord(DATE2, AMOUNT2, ITEM2);
        ExpenditureRecord returnedRecord = testRestTemplate.postForObject("/expenditure_recorder/expenditures", expenditureRecord,
                ExpenditureRecord.class);
        ExpenditureRecord savedRecord = repository.findOne(returnedRecord.getId());

        System.out.println(savedRecord.getDate().getClass().getName());

        assertThat(savedRecord.getId()).isEqualTo(returnedRecord.getId());
        assertThat(savedRecord.getDate()).isEqualTo(returnedRecord.getDate()).isEqualTo(DATE2);
        assertThat(savedRecord.getAmountInCent()).isEqualTo(returnedRecord.getAmountInCent()).isEqualTo(AMOUNT2);
        assertThat(savedRecord.getItem()).isEqualTo(returnedRecord.getItem()).isEqualTo(ITEM2);
    }

    @Test
    public void deleteExpenditure_WithSuccessfullyDeletedRecord() {
        testRestTemplate.delete("/expenditure_recorder/expenditures/{id}", recordId);

        assertThat(repository.findAll().iterator().hasNext()).isFalse();
    }
}
