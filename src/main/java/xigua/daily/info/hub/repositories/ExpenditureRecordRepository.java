package xigua.daily.info.hub.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import xigua.daily.info.hub.models.ExpenditureRecord;

public interface ExpenditureRecordRepository extends MongoRepository<ExpenditureRecord, String> {
}