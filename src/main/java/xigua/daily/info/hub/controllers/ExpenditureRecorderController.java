package xigua.daily.info.hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xigua.daily.info.hub.models.ExpenditureRecord;
import xigua.daily.info.hub.services.ExpenditureRecordService;

import java.util.List;

@RestController
@RequestMapping("/expenditure_recorder")
public class ExpenditureRecorderController {
    @Autowired
    private ExpenditureRecordService expenditureRecordService;

    @RequestMapping(value = "/expenditures", method = RequestMethod.GET)
    public List<ExpenditureRecord> getAllExpenditures() {
        return expenditureRecordService.getAllExpenditureRecords();
    }

    @RequestMapping(value = "/expenditures", method = RequestMethod.POST)
    public ExpenditureRecord postExpenditure(@RequestBody ExpenditureRecord expenditureRecord) {
        return expenditureRecordService.createExpenditureRecord(expenditureRecord);
    }

    @RequestMapping(value = "/expenditures/{id}", method = RequestMethod.DELETE)
    public void deleteExpenditure(@PathVariable String id) {
        expenditureRecordService.deleteExpenditureRecord(id);
    }
}