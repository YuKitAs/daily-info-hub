package xigua.daily.info.hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xigua.daily.info.hub.models.ExpenditureRecord;
import xigua.daily.info.hub.repositories.ExpenditureRecordRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenditureRecordService {
    @Autowired
    private ExpenditureRecordRepository expenditureRecordRepository;

    public List<ExpenditureRecord> getAllExpenditureRecords() {
        List<ExpenditureRecord> result = new ArrayList<>();
        expenditureRecordRepository.findAll().forEach(result::add);
        return result;
    }

    public ExpenditureRecord createExpenditureRecord(ExpenditureRecord expenditureRecord) {
        return expenditureRecordRepository.save(expenditureRecord);
    }

    public void deleteExpenditureRecord(String id) {
        expenditureRecordRepository.delete(id);
    }
}
