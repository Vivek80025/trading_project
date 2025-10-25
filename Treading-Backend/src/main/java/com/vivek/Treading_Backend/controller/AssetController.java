package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.model.Asset;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.service.AssetService;
import com.vivek.Treading_Backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
@AllArgsConstructor
public class AssetController {
    private final AssetService assetService;

    private final UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {

        Asset asset = assetService.getAssetById(assetId);

        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@RequestHeader("Authorization") String jwt,
                                                           @PathVariable String coinId) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(),coinId);

        return ResponseEntity.ok(asset);
    }

    @GetMapping()
    ResponseEntity<List<Asset>> getAssetsForUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List<Asset> assets = assetService.getUserAssets(user.getId());
        return ResponseEntity.ok(assets);
    }
}
