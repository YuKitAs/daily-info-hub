package xigua.daily.info.hub.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import xigua.daily.info.hub.models.ExpenditureRecord;
import xigua.daily.info.hub.repositories.ExpenditureRecordRepository;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpenditureRecordServiceTest {
    private static final String ID1 = "507f1f77bcf86cd799439011";
    private static final Instant DATE1 = LocalDateTime.of(2016, 8, 8, 7, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();
    private static final int AMOUNT1 = 200;
    private static final String ITEM1 = "Ice Cream";

    private static final String ID2 = "507f1f77bcf86cd799439012";
    private static final Instant DATE2 = LocalDateTime.of(2016, 10, 7, 0, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();
    private static final int AMOUNT2 = 600;
    private static final String ITEM2 = "Big Ice Cream";

    @Mock
    private ExpenditureRecordRepository repository;

    @InjectMocks
    @Autowired
    private ExpenditureRecordService service;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<ExpenditureRecord> json;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testGetAllExpenditureRecords() {

    }

    @Test
    public void createExpenditureRecord_WithCorrectData() throws IOException {
        ExpenditureRecord expenditureRecord = new ExpenditureRecord(DATE1, AMOUNT1, ITEM1);

        when(repository.save(expenditureRecord)).
                thenReturn(json.readObject(ExpenditureRecord.class.getResourceAsStream("ExpenditureRecord.json")));

        ExpenditureRecord createdExpenditureRecord = service.createExpenditureRecord(expenditureRecord);
        assertThat(createdExpenditureRecord.getId()).isEqualTo(ID1);
        assertThat(createdExpenditureRecord.getDate()).isEqualTo(DATE1);
        assertThat(createdExpenditureRecord.getAmountInCent()).isEqualTo(AMOUNT1);
        assertThat(createdExpenditureRecord.getItem()).isEqualTo(ITEM1);
    }

    @Test
    public void deleteExpenditureRecord_WithCalledMethod() {
        service.deleteExpenditureRecord(ID1);
        verify(repository, times(1)).delete(ID1);
    }

    @Test
    public void getAllExpenditureRecords_WithCorrectData() throws IOException {
        List<ExpenditureRecord> records = new ArrayList<>();
        records.add(json.readObject(ExpenditureRecord.class.getResourceAsStream("ExpenditureRecord.json")));
        records.add(json.readObject(ExpenditureRecord.class.getResourceAsStream("ExpenditureRecord_Alternative.json")));

        when(repository.findAll()).thenReturn(records);

        List<ExpenditureRecord> result = service.getAllExpenditureRecords();
        assertThat(result).extracting(ExpenditureRecord::getId).containsExactlyInAnyOrder(ID1, ID2);
        assertThat(result).extracting(ExpenditureRecord::getDate).containsExactlyInAnyOrder(DATE1, DATE2);
        assertThat(result).extracting(ExpenditureRecord::getAmountInCent).containsExactlyInAnyOrder(AMOUNT1, AMOUNT2);
        assertThat(result).extracting(ExpenditureRecord::getItem).containsExactlyInAnyOrder(ITEM1, ITEM2);
    }
}