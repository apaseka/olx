package com.example.olx.model;

import com.example.olx.internal.DateAdapter;
import com.example.olx.internal.PriceAdapter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Entity
public class AdvertisementDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String externalId2;

    @Column(length = 10000)
    private String itemDescription;

    private String url;
    private Integer priceUAH;
    private String price;
    private String advTitle;

    private String location;
    private String publishedGadget;
    private String publishedDate;

    @Column(length = 3000)
    private String advCategoryAndOther;
    private int viewsNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images;

    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    private LocalDateTime parsingDate;
    private LocalDateTime olxDate;
    private String timeInOlx;
    private Integer averageViewsPerDay;

    private String searchingUrl;
    private String mainImage;


    public AdvertisementDetail() {
    }

    public AdvertisementDetail(String externalId2, String itemDescription, String url, String price, String advTitle, String location, String publishedGadget, String publishedDate, String advCategoryAndOther, int viewsNumber, List<Image> images, Seller seller, String searchingUrl, String mainImage) {
        this.externalId2 = externalId2;
        this.itemDescription = itemDescription;
        this.url = url;
        this.searchingUrl = searchingUrl;
        this.price = price;
        this.advTitle = advTitle;
        this.location = location;
        this.publishedGadget = publishedGadget;
        this.publishedDate = publishedDate.replace(" Добавлено: в ", "").replace(" в ", "");
        this.advCategoryAndOther = advCategoryAndOther;
        this.viewsNumber = viewsNumber;
        this.images = images;
        this.mainImage = mainImage;
        this.seller = seller;
        this.priceUAH = PriceAdapter.adapt(price);
        this.parsingDate = LocalDateTime.now();
        this.olxDate = DateAdapter.adapt(this.publishedDate);
        this.timeInOlx = DateAdapter.timeInOlx(olxDate, parsingDate);
        this.averageViewsPerDay = olxDate.until(parsingDate, ChronoUnit.DAYS) > 0 ?
                viewsNumber / (int) olxDate.until(parsingDate, ChronoUnit.DAYS) :
                viewsNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExternalId2(String externalId2) {
        this.externalId2 = externalId2;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPriceUAH(Integer priceUAH) {
        this.priceUAH = priceUAH;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAdvTitle(String advTitle) {
        this.advTitle = advTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPublishedGadget(String publishedGadget) {
        this.publishedGadget = publishedGadget;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setAdvCategoryAndOther(String advCategoryAndOther) {
        this.advCategoryAndOther = advCategoryAndOther;
    }

    public void setViewsNumber(int viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setParsingDate(LocalDateTime parsingDate) {
        this.parsingDate = parsingDate;
    }

    public void setOlxDate(LocalDateTime olxDate) {
        this.olxDate = olxDate;
    }

    public void setTimeInOlx(String timeInOlx) {
        this.timeInOlx = timeInOlx;
    }

    public void setAverageViewsPerDay(Integer averageViewsPerDay) {
        this.averageViewsPerDay = averageViewsPerDay;
    }

    public void setSearchingUrl(String searchingUrl) {
        this.searchingUrl = searchingUrl;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Long getId() {
        return id;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getUrl() {
        return url;
    }

    public Integer getPriceUAH() {
        return priceUAH;
    }

    public String getPrice() {
        return price;
    }

    public String getAdvTitle() {
        return advTitle;
    }

    public String getLocation() {
        return location;
    }

    public String getPublishedGadget() {
        return publishedGadget;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAdvCategoryAndOther() {
        return advCategoryAndOther;
    }

    public int getViewsNumber() {
        return viewsNumber;
    }

    public List<Image> getImages() {
        return images;
    }

    public Seller getSeller() {
        return seller;
    }

    public LocalDateTime getOlxDate() {
        return olxDate;
    }

    public String getTimeInOlx() {
        return timeInOlx;
    }

    public Integer getAverageViewsPerDay() {
        return averageViewsPerDay;
    }

    public LocalDateTime getParsingDate() {
        return parsingDate;
    }

    public String getExternalId2() {
        return externalId2;
    }

    public String getSearchingUrl() {
        return searchingUrl;
    }

    public String getMainImage() {
        return mainImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvertisementDetail that = (AdvertisementDetail) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(externalId2, that.externalId2) &&
                Objects.equals(itemDescription, that.itemDescription) &&
                Objects.equals(url, that.url) &&
                Objects.equals(price, that.price) &&
                Objects.equals(advTitle, that.advTitle) &&
                Objects.equals(location, that.location) &&
                Objects.equals(publishedDate, that.publishedDate) &&
                Objects.equals(advCategoryAndOther, that.advCategoryAndOther);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId2, itemDescription, url, price, advTitle, location, publishedDate, advCategoryAndOther);
    }

    public static final class Builder {
        private String externalId2;
        private String itemDescription;
        private String url;
        private String searchingUrl;
        private String price;
        private String advTitle;
        private String location;
        private String publishedGadget;
        private String publishedDate;
        private String advCategoryAndOther;
        private int viewsNumber;
        private List<Image> images;
        private Seller seller;
        private String mainImage;

        private Builder() {
        }

        public static Builder anAdvertisementDetail() {
            return new Builder();
        }

        public Builder externalId2(String externalId2) {
            this.externalId2 = externalId2;
            return this;
        }

        public Builder itemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
            return this;
        }

        public Builder mainImage(String mainImage) {
            this.mainImage = mainImage;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder price(String price) {
            this.price = price;
            return this;
        }

        public Builder advTitle(String advTitle) {
            this.advTitle = advTitle;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder publishedGadget(String publishedGadget) {
            this.publishedGadget = publishedGadget;
            return this;
        }

        public Builder publishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
            return this;
        }

        public Builder advCategoryAndOther(String advCategoryAndOther) {
            this.advCategoryAndOther = advCategoryAndOther;
            return this;
        }

        public Builder viewsNumber(int viewsNumber) {
            this.viewsNumber = viewsNumber;
            return this;
        }

        public Builder images(List<Image> images) {
            this.images = images;
            return this;
        }

        public Builder searchingUrl(String searchingUrl) {
            this.searchingUrl = searchingUrl;
            return this;
        }

        public Builder seller(Seller seller) {
            this.seller = seller;
            return this;
        }

        public AdvertisementDetail build() {
            return new AdvertisementDetail(externalId2, itemDescription, url, price, advTitle, location, publishedGadget, publishedDate, advCategoryAndOther, viewsNumber, images, seller, searchingUrl, mainImage);
        }
    }
}
