/*
 * This file is generated by jOOQ.
 */
package uk.co.markg.mimic.db.tables.records;


import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import uk.co.markg.mimic.db.tables.Usage;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsageRecord extends UpdatableRecordImpl<UsageRecord> implements Record4<Integer, String, Long, LocalDateTime> {

    private static final long serialVersionUID = 1919512563;

    /**
     * Setter for <code>usage.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>usage.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>usage.command</code>.
     */
    public void setCommand(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>usage.command</code>.
     */
    public String getCommand() {
        return (String) get(1);
    }

    /**
     * Setter for <code>usage.serverid</code>.
     */
    public void setServerid(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>usage.serverid</code>.
     */
    public Long getServerid() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>usage.usagetime</code>.
     */
    public void setUsagetime(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>usage.usagetime</code>.
     */
    public LocalDateTime getUsagetime() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, Long, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Integer, String, Long, LocalDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Usage.USAGE.ID;
    }

    @Override
    public Field<String> field2() {
        return Usage.USAGE.COMMAND;
    }

    @Override
    public Field<Long> field3() {
        return Usage.USAGE.SERVERID;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Usage.USAGE.USAGETIME;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getCommand();
    }

    @Override
    public Long component3() {
        return getServerid();
    }

    @Override
    public LocalDateTime component4() {
        return getUsagetime();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getCommand();
    }

    @Override
    public Long value3() {
        return getServerid();
    }

    @Override
    public LocalDateTime value4() {
        return getUsagetime();
    }

    @Override
    public UsageRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public UsageRecord value2(String value) {
        setCommand(value);
        return this;
    }

    @Override
    public UsageRecord value3(Long value) {
        setServerid(value);
        return this;
    }

    @Override
    public UsageRecord value4(LocalDateTime value) {
        setUsagetime(value);
        return this;
    }

    @Override
    public UsageRecord values(Integer value1, String value2, Long value3, LocalDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UsageRecord
     */
    public UsageRecord() {
        super(Usage.USAGE);
    }

    /**
     * Create a detached, initialised UsageRecord
     */
    public UsageRecord(Integer id, String command, Long serverid, LocalDateTime usagetime) {
        super(Usage.USAGE);

        set(0, id);
        set(1, command);
        set(2, serverid);
        set(3, usagetime);
    }
}
