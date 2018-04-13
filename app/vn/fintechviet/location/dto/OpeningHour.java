package vn.fintechviet.location.dto;

import java.util.List;

/**
 * Created by tungn on 4/13/2018.
 */
public class OpeningHour {
    private boolean openNow;
    private List<Period> periods;

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }
}
