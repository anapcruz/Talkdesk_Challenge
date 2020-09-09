package talkdesk.challenge.calls.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

/**
 * Class Call
 * Save all calls and related information into the database
 */
@Entity
@Table(name = "CALL")
public class Call {
    /**
     * primary key of call
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * the phone number of the caller
     */
    @NotEmpty(message = "Caller number not be empty")
    private String callerNumber;

    /**
     * the phone number of the callee
     */
    @NotEmpty(message = "Callee number not be empty")
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
    private String type;

    /**
     * status of the call
     */
    private String status;

    /**
     * Returns the call id
     * @return call id
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the phone number of the caller
     * @return  phone number of the caller
     */
    public String getCallerNumber() {
        return callerNumber;
    }

    /**
     * Updates caller number
     * @param callerNumber caller number
     */
    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    /**
     * Returns the phone number of the callee
     * @return phone number of the callee
     */
    public String getCalleeNumber() {
        return calleeNumber;
    }

    /**
     * Updates callee number
     * @param calleeNumber callee number
     */
    public void setCalleeNumber(String calleeNumber) {
        this.calleeNumber = calleeNumber;
    }

    /**
     * Returns start timestamp of the call
     * @return start timestamp of the call
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Updates the start timestamp of the call at the beginning of the call
     * @param startTime start timestamp of the call
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns end timestamp of the call
     * @return end timestamp of the call
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Updates the end timestamp of the call at the end of the call
     * @param endTime end timestamp of the call
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the type of the call
     * @return call type
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the call
     * @param type type of the call
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the status of the call
     * @return call status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the type of the call
     * @param status call status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
