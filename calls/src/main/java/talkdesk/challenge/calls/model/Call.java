package talkdesk.challenge.calls.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Call class
 * All the information related to a call will be save into a database
 */
@Entity
@Table(name = "CALL")
public class Call implements Serializable {

    /**
     * Types of the call
     */
    private enum callType{
        INBOUND,
        OUTBOUND
    }

    /**
     * primary key of call
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * the phone number of the caller
     */
    private String callerNumber;

    /**
     * the phone number of the callee
     */
    private String calleeNumber;

    /**
     * start timestamp of the call
     */
    private Timestamp startTime;

    /**
     * end timestamp of the call
     */
    private Timestamp endTime;

    /**
     * type of the call
     */
    private callType type;

    /**
     * Call constructor
     * @param callerNumber the phone number of the caller
     * @param calleeNumber the phone number of the callee
     * @param startTime start timestamp of the call
     * @param endTime end timestamp of the call
     * @param type type of the call
     */
    public Call(String callerNumber, String calleeNumber, Timestamp startTime, Timestamp endTime, callType type) {
        this.callerNumber = callerNumber;
        this.calleeNumber = calleeNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    /**
     * Returns the phone number of the caller
     * @return  phone number of the caller
     */
    public String getCallerNumber() {
        return callerNumber;
    }

    /**
     * Returns the phone number of the callee
     * @return phone number of the callee
     */
    public String getCalleeNumber() {
        return calleeNumber;
    }

    /**
     * Returns start timestamp of the call
     * @return start timestamp of the call
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Returns end timestamp of the call
     * @return end timestamp of the call
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Returns the type of the call
     * @return type of the  call
     */
    public callType getType() {
        return type;
    }

}
