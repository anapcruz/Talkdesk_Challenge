package talkdesk.challenge.calls.model;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Auxiliary class
 *
 */

public class Calls {

    /**
     * The phone number of the caller
     */
    private String callerNumber;

    /**
     * The phone number of the callee
     */
    private String calleeNumber;

    /**
     * Initial time of the call
     */
    private Instant startTime;

    /**
     * Type of the call: Inbound or Outbound
     */
    private String callType;

    /**
     * Status of the call
     */
    private String callStatus;

    /**
     * Calls constructor - creates new call
     * @param callerNumber number of the caller
     * @param calleeNumber number of the callee
     * @param callType type of the call
     */
    public Calls(String callerNumber, String calleeNumber, String callType){
        this.callerNumber = callerNumber;
        this.calleeNumber = calleeNumber;
        this.startTime = Instant.now();
        this.callType = callType;
        this.callStatus = "IN_CALL";
    }

    /**
     * Returns the phone number of the caller
     * @return number of the caller
     */
    public String getCallerNumber() {
        return callerNumber;
    }

    /**
     * Returns the phone number of the callee
     * @return  number of the callee
     */
    public String getCalleeNumber() {
        return calleeNumber;
    }

    /**
     * Returns the instant of time that call starts
     * @return instant of time of the call
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * Returns the type of the call
     * @return type of the call
     */
    public String getCallType() {
        return callType;
    }

    /**
     * Returns the status of the call
     * @return status of the call
     */
    public String getCallStatus() {
        return callStatus;
    }
}
