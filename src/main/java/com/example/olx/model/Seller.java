package com.example.olx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
import java.time.LocalDateTime;

@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String sellerAddress;

    private String externalId;

    private String sellerSinceOnOlx;
    private LocalDateTime parsingDate;


    public Seller() {
    }

    public Seller(String name, String sellerAddress, String externalId, String sellerSinceOnOlx) {
        this.name = name;
        this.sellerAddress = sellerAddress;
        this.externalId = externalId;
        this.sellerSinceOnOlx = sellerSinceOnOlx;
        this.parsingDate=LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(name, seller.name) &&
                Objects.equals(sellerAddress, seller.sellerAddress) &&
                Objects.equals(externalId, seller.externalId) &&
                Objects.equals(sellerSinceOnOlx, seller.sellerSinceOnOlx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sellerAddress, externalId, sellerSinceOnOlx);
    }

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sellerAddress='" + sellerAddress + '\'' +
                ", externalId='" + externalId + '\'' +
                ", sellerSinceOnOlx='" + sellerSinceOnOlx + '\'' +
                '}';
    }
}
