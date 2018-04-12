package vn.fintechviet.content.model;

import java.util.Date;

import javax.persistence.*;

/**
 * Created by tungn on 9/12/2017.
 */
@Entity
@Table(name = "news")
public class News {
    private long id;
    private String title;
    private String shortDescription;
    private String link;
    private String imageLink;
    private NewsCategory newsCategory;
    private Integer noOfLike;
    private String status = "ACTIVE";
    private Date createdDate;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "shortDescription")
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Basic
    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Basic
    @Column(name = "imageLink")
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @ManyToOne
    @JoinColumn(name = "newsCategoryId")
    public NewsCategory getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(NewsCategory newsCategory) {
        this.newsCategory = newsCategory;
    }

    @Basic
    @Column(name = "noOfLike")
    public Integer getNoOfLike() {
        return noOfLike;
    }

    public void setNoOfLike(Integer noOfLike) {
        this.noOfLike = noOfLike;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP) 
    public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (id != news.id) return false;
        if (title != null ? !title.equals(news.title) : news.title != null) return false;
        if (shortDescription != null ? !shortDescription.equals(news.shortDescription) : news.shortDescription != null)
            return false;
        if (link != null ? !link.equals(news.link) : news.link != null) return false;
        if (noOfLike != null ? !noOfLike.equals(news.noOfLike) : news.noOfLike != null) return false;
        if (status != null ? !status.equals(news.status) : news.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (noOfLike != null ? noOfLike.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
