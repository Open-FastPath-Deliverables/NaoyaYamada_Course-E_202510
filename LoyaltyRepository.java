package com.example.loyaltyprogram.repository;

import com.example.loyaltyprogram.model.PointEntity;
import com.example.loyaltyprogram.model.StageEntity;
import com.example.loyaltyprogram.model.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * LoyaltyRepository
 * ロイヤルティプログラムのデータを管理するためのインターフェース。
 * PointEntity, StageEntity, BenefitEntityと密接に連携します。
 * ポイント、ステージ、特典情報の検索・管理をサポートします。
 */
@Repository
public interface LoyaltyRepository extends JpaRepository<Object, Long> {

    // --- ポイント管理 ---

    /**
     * ユーザーIDでポイント残高を取得。
     *
     * @param userId ユーザーID
     * @return PointEntity (ポイント情報)
     */
    Optional<PointEntity> findPointByUserId(Long userId);

    /**
     * ユーザーのポイント履歴を取得。
     *
     * @param userId ユーザーID
     * @return ユーザーのポイント履歴のリスト
     */
    List<PointEntity> findPointHistoryByUserId(Long userId);

    // --- ステージ管理 ---

    /**
     * ユーザーの現在のステージを取得。
     *
     * @param userId ユーザーID
     * @return StageEntity (ステージ情報)
     */
    Optional<StageEntity> findStageByUserId(Long userId);

    /**
     * 全てのステージ情報を取得。
     *
     * @return ステージ情報のリスト
     */
    List<StageEntity> findAllStages();

    // --- 特典管理 ---

    /**
     * 特典IDで特定の特典情報を取得。
     *
     * @param benefitId 特典ID
     * @return BenefitEntity (特典情報)
     */
    Optional<BenefitEntity> findBenefitById(Long benefitId);

    /**
     * ユーザーが利用可能な特典一覧を取得。
     *
     * @param userId ユーザーID
     * @return ユーザーが利用可能な特典のリスト
     */
    List<BenefitEntity> findBenefitsByUserId(Long userId);

    // --- デフォルトCRUDメソッド ---
    // JpaRepository により以下が利用可能:
    // save(T entity): エンティティの保存
    // deleteById(ID id): IDでエンティティを削除
    // findById(ID id): IDでエンティティを取得
}