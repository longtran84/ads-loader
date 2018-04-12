package vn.fintechviet.user.model;

import javax.persistence.*;

/**
 * Created by tungn on 10/2/2017.
 */
@Entity
@Table(name = "reward_category")
public class RewardCategory {
    private int id;
    private String rewardCode;
    private String rewardName;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "rewardCode")
    public String getRewardCode() {
        return rewardCode;
    }

    public void setRewardCode(String rewardCode) {
        this.rewardCode = rewardCode;
    }

    @Basic
    @Column(name = "rewardName")
    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RewardCategory that = (RewardCategory) o;

        if (id != that.id) return false;
        if (rewardCode != null ? !rewardCode.equals(that.rewardCode) : that.rewardCode != null) return false;
        if (rewardName != null ? !rewardName.equals(that.rewardName) : that.rewardName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rewardCode != null ? rewardCode.hashCode() : 0);
        result = 31 * result + (rewardName != null ? rewardName.hashCode() : 0);
        return result;
    }
}
