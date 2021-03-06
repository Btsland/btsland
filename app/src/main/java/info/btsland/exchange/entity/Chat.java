package info.btsland.exchange.entity;

import java.util.Date;

public class Chat {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column chat.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column chat.from_user
     *
     * @mbggenerated
     */
    private String fromUser="";

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column chat.to_user
     *
     * @mbggenerated
     */
    private String toUser="";

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column chat.context
     *
     * @mbggenerated
     */
    private String context="";

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column chat.time
     *
     * @mbggenerated
     */
    private Date time;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column chat.id
     *
     * @return the value of chat.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column chat.id
     *
     * @param id the value for chat.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column chat.from_user
     *
     * @return the value of chat.from_user
     *
     * @mbggenerated
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column chat.from_user
     *
     * @param fromUser the value for chat.from_user
     *
     * @mbggenerated
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser == null ? null : fromUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column chat.to_user
     *
     * @return the value of chat.to_user
     *
     * @mbggenerated
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column chat.to_user
     *
     * @param toUser the value for chat.to_user
     *
     * @mbggenerated
     */
    public void setToUser(String toUser) {
        this.toUser = toUser == null ? null : toUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column chat.context
     *
     * @return the value of chat.context
     *
     * @mbggenerated
     */
    public String getContext() {
        return context;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column chat.context
     *
     * @param context the value for chat.context
     *
     * @mbggenerated
     */
    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column chat.time
     *
     * @return the value of chat.time
     *
     * @mbggenerated
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column chat.time
     *
     * @param time the value for chat.time
     *
     * @mbggenerated
     */
    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", context='" + context + '\'' +
                ", time=" + time +
                '}';
    }
}