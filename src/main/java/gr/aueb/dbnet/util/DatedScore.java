package gr.aueb.dbnet.util;

import java.util.Calendar;

public class DatedScore {         
    public double score;
    public Calendar date_time;

    public DatedScore(double s, Calendar d) {         
        this.score= s;
        this.date_time= d;
     }
 }