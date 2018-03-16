package xigua.daily.info.hub.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.springframework.data.annotation.Id;

import java.time.Instant;

public class ExpenditureRecord {
    public static class Properties {
        public static final String ID = "id";
        public static final String DATE = "date";
        public static final String AMOUNT_IN_CENT = "amountInCent";
        public static final String ITEM = "item";
    }

    @Id
    @JsonProperty(Properties.ID)
    private String id;

    @JsonProperty(Properties.DATE)
    private Instant date;

    @JsonProperty(Properties.AMOUNT_IN_CENT)
    private Integer amountInCent;

    @JsonProperty(Properties.ITEM)
    private String item;

    @JsonCreator
    private ExpenditureRecord(@JsonProperty(Properties.ID) String id, @JsonProperty(Properties.DATE) @JsonDeserialize Instant date,
            @JsonProperty(Properties.AMOUNT_IN_CENT) Integer amountInCent, @JsonProperty(Properties.ITEM) String item) {
        this.id = id;
        this.date = date;
        this.amountInCent = amountInCent;
        this.item = item;
    }

    public ExpenditureRecord(Instant date, Integer amountInCent, String item) {
        this.date = date;
        this.amountInCent = amountInCent;
        this.item = item;
    }

    public ExpenditureRecord() {
        // We still need this even if we are using MongoDB, because the database driver is still Hibernate.
    }

    public String getId() {
        return id;
    }

    public Instant getDate() {
        return date;
    }

    public Integer getAmountInCent() {
        return amountInCent;
    }

    public String getItem() {
        return item;
    }
}
