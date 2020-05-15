/*
 * This file is generated by jOOQ.
 */
package uk.co.markg.bertrand.db.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Channels implements Serializable {

    private static final long serialVersionUID = 57146353;

    private Long    channelid;
    private Boolean readPerm;
    private Boolean writePerm;

    public Channels() {}

    public Channels(Channels value) {
        this.channelid = value.channelid;
        this.readPerm = value.readPerm;
        this.writePerm = value.writePerm;
    }

    public Channels(
        Long    channelid,
        Boolean readPerm,
        Boolean writePerm
    ) {
        this.channelid = channelid;
        this.readPerm = readPerm;
        this.writePerm = writePerm;
    }

    public Long getChannelid() {
        return this.channelid;
    }

    public void setChannelid(Long channelid) {
        this.channelid = channelid;
    }

    public Boolean getReadPerm() {
        return this.readPerm;
    }

    public void setReadPerm(Boolean readPerm) {
        this.readPerm = readPerm;
    }

    public Boolean getWritePerm() {
        return this.writePerm;
    }

    public void setWritePerm(Boolean writePerm) {
        this.writePerm = writePerm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Channels (");

        sb.append(channelid);
        sb.append(", ").append(readPerm);
        sb.append(", ").append(writePerm);

        sb.append(")");
        return sb.toString();
    }
}
