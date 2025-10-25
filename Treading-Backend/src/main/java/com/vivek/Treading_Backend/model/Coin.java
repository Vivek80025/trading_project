package com.vivek.Treading_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
@Entity
@Data
public class Coin {
    @Id
    private String id;

    private String symbol;
    private String name;
    private String image;

    private Double currentPrice;
    private Long marketCap;
    private Integer marketCapRank;
    private Long fullyDilutedValuation;
    private Long totalVolume;

    private Double high24h;
    private Double low24h;
    private Double priceChange24h;
    private Double priceChangePercentage24h;

    private Double marketCapChange24h;
    private Double marketCapChangePercentage24h;

    private Long circulatingSupply;
    private Long totalSupply;
    private Long maxSupply;

    private Double ath;
    private Double athChangePercentage;
    private Date athDate;

    private Double atl;
    private Double atlChangePercentage;
    private Date atlDate;

    @JsonIgnore
    private String roi; // nullable
    private Date lastUpdated;
}
