package org.udevcommunity.utasbih.utasbih;

import java.sql.Date;

/**
 * DayInfo
 *
 * This Class is used to get and send data to the database as Objects
 *
 * @package :
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 * @license :
 * @link : https://github.com/ztickm/UTasbih
 */
public class DayInfo {
    private int id; // id
    private int mode; // mode of tasbih
    private Date day; // the date
    private int number; // number of tasbih

    public String toString()
    {
        return Integer.toString(getId()) + "/" + Integer.toString(getMode()) + "/" + getDay().toString() + "/" + Integer.toString(getNumber()) + "\n";
    }

    // Constuctors //TODO SAFELY Remove the unused stuff, like this constructor
    public DayInfo()
    {

    }
    public DayInfo(int id, int mode, Date day, int number)
    {
        this.id = id;
        this.mode = mode;
        this.day = day;
        this.number = number;
    }

    // Setters
    public void setId(int id)
    {
        this.id = id;
    }
    public void setMode(int mode)
    {
        this.mode = mode;
    }
    public void setDay(Date day)
    {
        this.day = day;
    }
    public void setNumber(int number)
    {
        this.number = number;
    }

    // Getters
    public int getId()
    {
        return this.id;
    }
    public int getMode()
    {
        return this.mode;
    }
    public Date getDay()
    {
        return this.day;
    }
    public int getNumber()
    {
        return this.number;
    }
}
