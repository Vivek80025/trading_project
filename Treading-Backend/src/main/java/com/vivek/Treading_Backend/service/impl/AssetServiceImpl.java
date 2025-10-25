package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.model.Asset;
import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.AssetRepository;
import com.vivek.Treading_Backend.service.AssetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);

        return optionalAsset
                .orElseThrow(() -> new Exception("Asset not found..."));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        Asset oldAsset = getAssetById(assetId);
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());
        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);
    }
}
