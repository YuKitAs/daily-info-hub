package xigua.daily.info.hub.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xigua.daily.info.hub.models.ExpenditureRecord;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpenditureRecordRepositoryIntegrationTest {
    private static final Instant DATE1 = LocalDateTime.of(2016, 8, 8, 7, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();
    private static final Instant DATE2 = LocalDateTime.of(2016, 10, 7, 0, 0, 0, 0).atZone(ZoneId.of("UTC")).toInstant();

    private static final int AMOUNT1 = 200;
    private static final int AMOUNT2 = 600;

    private static final String ITEM1 = "Ice Cream";
    private static final String ITEM2 = "Big Ice Cream";

    private String recordId;

    @Autowired
    private ExpenditureRecordRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();
        ExpenditureRecord record = new ExpenditureRecord(DATE1, AMOUNT1, ITEM1);
        recordId = repository.save(record).getId();
    }

    @Test
    public void findAll_WithCorrectData() {
        List<ExpenditureRecord> expenditureRecords = new ArrayList<>();
        repository.findAll().forEach(expenditureRecords::add);

        assertThat(expenditureRecords).extracting(ExpenditureRecord::getId).containsExactly(recordId);
        assertThat(expenditureRecords).extracting(ExpenditureRecord::getAmountInCent).containsExactly(AMOUNT1);
        assertThat(expenditureRecords).extracting(ExpenditureRecord::getDate).containsExactly(DATE1);
        assertThat(expenditureRecords).extracting(ExpenditureRecord::getItem).containsExactly(ITEM1);
    }

    @Test
    public void save_WithCorrectData() {
        ExpenditureRecord expenditureRecord = new ExpenditureRecord(DATE2, AMOUNT2, ITEM2);
        ExpenditureRecord returnedRecord = repository.save(expenditureRecord);
        ExpenditureRecord savedRecord = repository.findOne(returnedRecord.getId());

        assertThat(savedRecord.getId()).isEqualTo(returnedRecord.getId());
        assertThat(savedRecord.getDate()).isEqualTo(returnedRecord.getDate()).isEqualTo(DATE2);
        assertThat(savedRecord.getAmountInCent()).isEqualTo(returnedRecord.getAmountInCent()).isEqualTo(AMOUNT2);
        assertThat(savedRecord.getItem()).isEqualTo(returnedRecord.getItem()).isEqualTo(ITEM2);
    }

    @Test
    public void delete_WithSuccessfullyDeletedRecord() {
        repository.delete(recordId);
        assertThat(repository.findAll().iterator().hasNext()).isFalse();
    }
}