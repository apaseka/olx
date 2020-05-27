package com.example.olx.model;

import com.example.olx.internal.PriceAdapter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AdvertisementDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;
    private String price;
    private Integer priceUAH;
    private String advTitle;

    private String url;
    private int loop;
    private LocalDateTime parsingDate;

    private String baseURI;
    private String searchUrl;

    @ManyToOne
    private AdvertisementDetail advertisementDetail;

    public AdvertisementDashboard() {
    }

    public AdvertisementDashboard(String externalId, String price, String advTitle, String url, int loop, AdvertisementDetail advertisementDetail, String baseURI, String searchUrl) {
        this.externalId = externalId;
        this.price = price;
        this.priceUAH = PriceAdapter.adapt(price);
        this.advTitle = advTitle;

        this.url = url;
        this.loop = loop;
        this.advertisementDetail = advertisementDetail;
        this.parsingDate = LocalDateTime.now();

        this.baseURI = baseURI;
        this.searchUrl = searchUrl;
    }

    public String getAdvTitle() {
        return advTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setAdvertisementDetail(AdvertisementDetail advertisementDetail) {
        this.advertisementDetail = advertisementDetail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPriceUAH() {
        return priceUAH;
    }

    public void setPriceUAH(Integer priceUAH) {
        this.priceUAH = priceUAH;
    }

    public void setAdvTitle(String advTitle) {
        this.advTitle = advTitle;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public LocalDateTime getParsingDate() {
        return parsingDate;
    }

    public void setParsingDate(LocalDateTime parsingDate) {
        this.parsingDate = parsingDate;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public AdvertisementDetail getAdvertisementDetail() {
        return advertisementDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvertisementDashboard that = (AdvertisementDashboard) o;
        return Objects.equals(externalId, that.externalId) &&
                Objects.equals(price, that.price) &&
                Objects.equals(advTitle, that.advTitle) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, price, advTitle, url);
    }

    public static final class Builder {
        private String externalId;
        private String price;
        private String advTitle;

        private String url;
        private int loop;
        private AdvertisementDetail advertisementDetail;
        private String baseURI;
        private String searchUrl;

        private Builder() {
        }

        public static Builder anAdvertisementDashboard() {
            return new Builder();
        }

        public Builder externalId(String externalId) {
            this.externalId = externalId;
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

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder loop(int loop) {
            this.loop = loop;
            return this;
        }

        public Builder baseURI(String baseURI) {
            this.baseURI = baseURI;
            return this;
        }

        public Builder searchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
            return this;
        }

        public Builder advertisementDetail(AdvertisementDetail advertisementDetail) {
            this.advertisementDetail = advertisementDetail;
            return this;
        }

        public AdvertisementDashboard build() {
            return new AdvertisementDashboard(externalId, price, advTitle, url, loop, advertisementDetail, baseURI, searchUrl);
        }
    }
}
